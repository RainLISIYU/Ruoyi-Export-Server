package com.ruoyi.state.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.datasource.annotation.Slave;
import com.ruoyi.state.domain.VoteDetail;
import com.ruoyi.state.domain.VoteOption;
import com.ruoyi.state.domain.VoteSubject;
import com.ruoyi.state.service.VoteDetailService;
import com.ruoyi.state.service.VoteOptionService;
import com.ruoyi.state.service.VoteSubjectService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lsy
 * @description 投票controller
 * @date 2024/4/11
 */
@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteSubjectService voteSubjectService;

    @Autowired
    private VoteOptionService voteOptionService;

    @Autowired
    private VoteDetailService voteDetailService;

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 查询主题列表
     *
     * @return 主题列表
     */
    @GetMapping("subject/list")
    @Slave
    public R getSubjectList(){
        List<VoteSubject> list = voteSubjectService.list();
        return R.ok(list);
    }

    /**
     * 新增或修改主题
     *
     * @param voteSubject 主题信息
     * @return 响应
     */
    @PostMapping("subject/addOrUpdate")
    @Slave
    public R addOrUpdateSubject(@RequestBody VoteSubject voteSubject){
        return R.ok(voteSubjectService.saveOrUpdate(voteSubject));
    }

    @DeleteMapping("subject/remove")
    @Slave
    public R removeSubject(@RequestParam Long id){
        return R.ok(voteSubjectService.removeById(id));
    }

    /**
     * 投票项列表查询
     *
     * @param voteOption
     * @return
     */
    @GetMapping("option/list")
    @Slave
    public R getOptionList(VoteOption voteOption){
        List<VoteOption> list = listVoteOption(voteOption);
        //获取用户id
        Set<Long> userIds = list.stream().map(VoteOption::getUserId).collect(Collectors.toSet());
        Map<Long, String> usernameMap = new HashMap<>();
        if (! userIds.isEmpty()){
            List<LoginUser> userList = remoteUserService.listUser(userIds, SecurityConstants.INNER).getData();
            usernameMap = userList.stream().collect(Collectors.toMap(LoginUser::getUserid, LoginUser::getUsername));
        }
        //获取主题信
        Set<Long> subjectIds = list.stream().map(VoteOption::getSubjectId).collect(Collectors.toSet());
        Map<Long, String> subjectMap = new HashMap<>();
        if (! subjectIds.isEmpty()){
            List<VoteSubject> subjects = listSubject(subjectIds);
            subjectMap = subjects.stream().collect(Collectors.toMap(VoteSubject::getId, VoteSubject::getSubjectName));
        }
        //处理用户信息
        for (VoteOption opt : list){
            opt.setUsername(usernameMap.get(opt.getUserId()));
            opt.setSubjectName(subjectMap.get(opt.getSubjectId()));
        }
        return R.ok(list);
    }

    /**
     * 根据id查询主题
     *
     * @param subjectIds 主题id集合
     * @return 结果集
     */
    @Slave
    public List<VoteSubject> listSubject(Set<Long> subjectIds){
        if (subjectIds.isEmpty()){
            return null;
        }
        return voteSubjectService.list(new LambdaQueryWrapper<VoteSubject>().in(VoteSubject::getId, subjectIds));
    }

    /**
     * 查询投票项
     * @param voteOption 参数
     * @return 结果集
     */
    public List<VoteOption> listVoteOption(VoteOption voteOption){
        return voteOptionService.list();
    }

    /**
     * 新增或修改投票项
     *
     * @param voteOption 投票项信息
     * @return 响应信息
     */
    @PostMapping("option/addOrUpdate")
    @Slave
    public R addOrUpdateOption(@RequestBody VoteOption voteOption){
        return R.ok(voteOptionService.saveOrUpdate(voteOption));
    }

    /**
     * 新增或修改投票明细
     *
     * @param voteDetails 投票明细
     * @return 响应
     */
    @PostMapping("detail/addOrUpdate")
    @Slave
    public R addOrUpdateDetail(@RequestBody List<VoteDetail> voteDetails){
        if (voteDetails.isEmpty()){
            return R.fail("请选择投票项");
        }
        Map<String, Object> voteNumMap = voteDetailService.getVoteNum(voteDetails);
        voteDetailService.vote(voteDetails);
        return R.ok(voteNumMap);
    }

}
