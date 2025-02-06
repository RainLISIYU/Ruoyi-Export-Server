package com.ruoyi.business.test;

import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lsy
 * @description Activity测试类
 * @date 2024/11/19
 */
@SpringBootTest
@Slf4j
public class ActivityTest {

    private final ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void initActivityDb() {
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);
    }

    /**
     * 流程部署
     */
    @Test
    public void activityDeploy() {
        // 创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取repositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/leave.bpmn20.xml")
                .addClasspathResource("process/leave.bpmn20.png")
                .enableDuplicateFiltering()
                .name("请假流程")
                .deploy();
        repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        // 输出
        log.info("流程部署ID:{}", deployment.getId());
        log.info("流程部署名称：{}", deployment.getName());
        log.info("流程部署成功");

    }

    /**
     * 启动流程
     */
    @Test
    public void activityStartProcess() {
        // 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 根据流程定义的key启动流程实例，bpmn文件定义时设置
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave.bpmn20");
        // 根据流程定义id启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("leave.bpmn20:1:4");
        // 获取流程实例信息
        log.info("流程定义id:{}", processInstance.getProcessDefinitionId());
        log.info("流程实例id:{}", processInstance.getId());
        log.info("启动流程成功");
    }

    /**
     * 删除流程
     */
    @Test
    public void deleteDeployment() {
        RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        repositoryService.deleteDeployment("1");
    }

    /**
     * 查询当前流程实例信息
     */
    @Test
    public void queryProcess() {
        String processInstanceId = "2501";
        // 获取流程执行服务类对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance == null) {
            log.info("流程已结束！");
        } else {
            log.info("流程定义key：{}", processInstance.getProcessDefinitionKey());
            log.info("流程定义名称：{}", processInstance.getProcessDefinitionName());
            log.info("name:{}", processInstance.getName());
            log.info("当前流程在：{}", processInstance.getActivityId());
            log.info("业务id:{}", processInstance.getBusinessKey());
            log.info("DeploymentId:{}", processInstance.getDeploymentId());
        }
    }

    /**
     * 流程挂起与激活
     */
    @Test
    public void suspend() {
        String processInstanceId = "2501";
        processEngine.getRuntimeService().suspendProcessInstanceById(processInstanceId);
        log.info("挂起成功");
        processEngine.getRuntimeService().activateProcessInstanceById(processInstanceId);
        log.info("激活成功");
    }

    /**
     * 查询待办任务
     */
    @Test
    public void activityTodoTask() {
        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 查询代办业务
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("测试")
                .processDefinitionKey("leave.bpmn20")
                .list();
        // 输出
        for (Task task : taskList) {
            log.info("任务名称：{}", task.getName());
            log.info("任务执行人：{}", task.getAssignee());
            log.info("任务ID:{}", task.getId());
            log.info("流程实例ID：{}", task.getProcessInstanceId());
            log.info("流程定义ID:{}", task.getProcessDefinitionId());
        }
    }

    /**
     * 查询正在执行的任务办理人表
     */
    @Test
    public void findRunPersonTask() {
        String taskId = "2505";
        List<IdentityLink> identityLinksForTask = processEngine.getTaskService().getIdentityLinksForTask(taskId);
        if (identityLinksForTask != null && !identityLinksForTask.isEmpty()) {
            for (IdentityLink identityLink : identityLinksForTask){
                log.info(identityLink.getTaskId() + " " + identityLink.getType() + "   " + identityLink.getUserId());
                log.info("processInstanceId:{}, taskId:{}", identityLink.getProcessInstanceId(), identityLink.getTaskId());
                log.info("identityLinkType:{}, userId:{}", identityLink.getType(), identityLink.getUserId());
            }
        }

    }

    /**
     * 完成当前节点
     */
    @Test
    public void completeTask() {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId("7502").singleResult();
        taskService.setVariable(task.getId(), "isAgree", true);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "同意");
        taskService.complete("7502");
    }

    /**
     * 转办
     */
    @Test
    public void transferAssignee() {
        TaskService taskService = processEngine.getTaskService();
        taskService.setAssignee("5005", "测试");
    }

    /**
     * 结束任务
     */
    @Test
    public void endTask() {
        endTask("5005");
    }

    /**
     * 结束任务
     * @param taskId    当前任务ID
     */
    public void endTask(String taskId) {

        TaskService taskService = processEngine.getTaskService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //  当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(task.getProcessDefinitionId());
        List<EndEvent> endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        FlowNode endFlowNode = endEventList.getFirst();
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List<SequenceFlow> originalSequenceFlowList = new ArrayList<>(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());

        //  可以不用恢复原始方向，不影响其它的流程
//        currentFlowNode.setOutgoingFlows(originalSequenceFlowList);
    }

    /**
     * 查询审批历史
     */
    @Test
    public void findHistory() {
        String processInstanceId = "2501";
        String assignee = "测试";
        HistoryService historyService = processEngine.getHistoryService();
        TaskService taskService = processEngine.getTaskService();
        // 查询历史审核信息
        List<HistoricActivityInstance> userTask = historyService.createHistoricActivityInstanceQuery()
//                .taskAssignee(assignee)
                .activityType("userTask")
                .orderByHistoricActivityInstanceStartTime().desc()
                .list();
        for (HistoricActivityInstance instance : userTask) {
            log.info("流程定义ID = {}", instance.getProcessDefinitionId());
            log.info("流程实例ID = {}", instance.getProcessInstanceId());
            log.info("任务ID = {}", instance.getTaskId());
            System.out.println("任务名称 = " + instance.getActivityName());
            System.out.println("任务开始时间 = " + instance.getStartTime());
            System.out.println("任务结束时间 = " + instance.getEndTime());
            System.out.println("任务耗时 = " + instance.getDurationInMillis());
            // 获取审批批注信息
            List<Comment> taskComments = taskService.getTaskComments(instance.getTaskId());
            if (!taskComments.isEmpty()){
                System.out.println("审批批注 = " + taskComments.getFirst().getFullMessage());
            }
        }
    }


}
