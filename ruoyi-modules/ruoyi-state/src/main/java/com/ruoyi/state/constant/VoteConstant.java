package com.ruoyi.state.constant;

/**
 * @author lsy
 * @description 投票相关变量
 * @date 2024/4/11
 */
public class VoteConstant {

    public static final String VOTE_SUBJECT_KEY = "vote_subject:";

    /**
     * 生产主题redis键
     *
     * @param subjectId 主题id
     * @return 键值
     */
    public static String getVoteSubjectKey(Long subjectId){
        return VOTE_SUBJECT_KEY + subjectId;
    }

}
