package com.ruoyi.activity;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;

/**
 * @author lsy
 * @description activity工具类
 * @date 2024/11/28
 */
public class ActivityUtils {

    /**
     * 流程部署
     */
    public static void activityDeploy() {
        // 创建引擎对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //
    }

}
