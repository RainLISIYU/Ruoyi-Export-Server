package com.ruoyi.state.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.state.constant.VoteConstant;
import com.ruoyi.state.domain.VoteDetail;
import com.ruoyi.state.domain.VoteOption;
import com.ruoyi.state.service.VoteDetailService;
import com.ruoyi.state.mapper.VoteDetailMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
@Service
public class VoteDetailServiceImpl extends ServiceImpl<VoteDetailMapper, VoteDetail>
    implements VoteDetailService{

    @Autowired
    private RedisService redisService;

    @Resource
    private VoteDetailMapper voteDetailMapper;

    private ReentrantLock lock = new ReentrantLock();

    @Override
    public Map<String, Object> getVoteNum(List<VoteDetail> voteDetails) {
        Long subjectId = voteDetails.getFirst().getSubjectId();
        String subjectKey = VoteConstant.getVoteSubjectKey(subjectId);
        Boolean hasKey = redisService.hasKey(subjectKey);
        if (!hasKey){
            //投票参数不存在
            try {
                if (lock.tryLock(500, TimeUnit.MILLISECONDS)){
                    if (!redisService.hasKey(subjectKey)){
                        List<Map> optionNumMap = voteDetailMapper.countBySubjectId(subjectId);
                        for (Map map : optionNumMap){
                            redisService.setCacheMapValue(subjectKey, String.valueOf(map.get("optionId")), map.get("optionNum"));
                        }
                    }
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("系统繁忙，请稍候重试");
            }
        }
        for (VoteDetail voteDetail : voteDetails){
            redisService.incrMapValue(subjectKey, String.valueOf(voteDetail.getOptionId()), 1L);
        }
        return redisService.getCacheMap(subjectKey);
    }

    @Async
    @Override
    public void vote(List<VoteDetail> voteDetails) {
        try {
            this.saveOrUpdateBatch(voteDetails);
        }catch (Exception e){
            log.error(e.getMessage());
            //回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //清除缓存
            redisService.deleteObject(VoteConstant.getVoteSubjectKey(voteDetails.getFirst().getSubjectId()));
        }

    }

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> System.out.println(111));
        Runnable runnable = () -> System.out.println(222);
        Callable callable = () -> {System.out.println(333); Thread.sleep(1000); return 2;};
        Object call = callable.call();
        System.out.println(call);
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        futureTask.run();
        thread.start();
        new Thread(runnable).start();
        thread.start();
        new Thread(runnable).start();
        Integer i = futureTask.get();
        System.out.println(i);
    }

}




