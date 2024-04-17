package com.ruoyi.state.mapper;

import com.ruoyi.state.domain.VoteDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.ruoyi.state.domain.VoteDetail
 */
public interface VoteDetailMapper extends BaseMapper<VoteDetail> {

    /**
     * 根据投票主题id统计每个选项的票数
     *
     * @param subjectId 主题id
     * @return 每项票数
     */
    @MapKey("")
    List<Map> countBySubjectId(Long subjectId);

}




