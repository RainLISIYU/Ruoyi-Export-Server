package com.ruoyi.state.service;

import com.ruoyi.state.domain.VoteDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.state.domain.VoteOption;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface VoteDetailService extends IService<VoteDetail> {

    /**
     * 获取投票数
     *
     * @param voteDetails 投票项
     * @return 投票项信息
     */
    Map<String, Object> getVoteNum(List<VoteDetail> voteDetails);

    /**
     * 保存投票记录
     *
     * @param voteDetails 投票明细
     */
    void vote(List<VoteDetail> voteDetails);
}
