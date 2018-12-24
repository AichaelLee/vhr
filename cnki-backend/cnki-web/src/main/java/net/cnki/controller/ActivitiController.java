package net.cnki.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.cnki.bean.Managers;
import net.cnki.bean.TblTeacherBase;
import net.cnki.bean.activiti.presetation.ProcessInstanceRepresentation;
import net.cnki.bean.activiti.vo.TaskVo;
import net.cnki.rest.runtime.AbstractTaskQueryResource;
import net.cnki.rest.runtime.domain.TaskRequestedParam;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: lizhizhong
 * CreatedDate: 2018/12/14.
 */
@Slf4j
@RestController
@RequestMapping("/system/basic")
public class ActivitiController extends AbstractTaskQueryResource {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;


    @Autowired
    HistoryService historyService;

    /**
     * 学生发起一次新流程
     */
    @GetMapping("startProcess")
    public String startProcess(){
       return getProcessInstance().getId();
    }

    @RequestMapping(value = "query/process-instances", method = RequestMethod.POST)
    public void getProcessInstances(String deploymentKey) {
        HistoricProcessInstanceQuery instanceQuery = historyService.createHistoricProcessInstanceQuery();
//        JsonNode deploymentKeyNode = requestNode.get("deploymentKey");
        List<Deployment> deployments = repositoryService.createDeploymentQuery().deploymentKey(deploymentKey).list();

        List<String> deploymentIds = new ArrayList<String>();
        for (Deployment deployment : deployments) {
            deploymentIds.add(deployment.getId());
        }

        instanceQuery.deploymentIdIn(deploymentIds);
        instanceQuery.unfinished();
        List<HistoricProcessInstance> instances = instanceQuery.list();
        List<ProcessInstanceRepresentation> results = new ArrayList<>();
        instances.forEach(o->{
            ProcessInstanceRepresentation p = new ProcessInstanceRepresentation(o);
            results.add(p);
        });
       // return super.getProcessInstances(requestNode);
    }

    /**
     * 学生开始一次流程,上面的那一个名字起的有问题,上面那个应该是启动给一个新实例,而不是新流程
     */
    @PostMapping("processStart")
    public void newPorcess(){
        TblTeacherBase currentUser = (TblTeacherBase)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(currentUser.getRoles().get(0).getName().equals("ROLE_student")){

            // 开启一次审批(论文提交)
            // 得到所有任务
            List<Task> tasks = taskService.createTaskQuery().list();


        }


    }

    @PostMapping("claim/{processInstanceId}")
    public Map claimTask(@RequestParam Map<String,Object> variables,@PathVariable("processInstanceId")String executinId){



        Map<String,Object> theTable = Maps.newHashMap();

        StringBuilder userRole = new StringBuilder();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof Managers){
            userRole.append("admin");

        }else if(principal instanceof TblTeacherBase){

            if(((TblTeacherBase) principal).getRoles().get(0).getName().equals("ROLE_dean")){
                userRole.append("dean");
            }else if(((TblTeacherBase) principal).getRoles().get(0).getName().equals("ROLE_guideTeacher")){
                userRole.append("teacher");
            }else{
                userRole.append("student");
            }

        }else {
            userRole.append("student");

        }

        // 根据executionID得到当前任务
        Task o = taskService.createTaskQuery().processInstanceId(executinId).singleResult();
        // 得到认证关系
        List<IdentityLink> identitiesList = taskService.getIdentityLinksForTask(o.getId());

        for (IdentityLink p : identitiesList) {

            log.info("all identityList is = {}:",ToStringBuilder
                    .reflectionToString(p, ToStringStyle.JSON_STYLE));

            if (p.getType().equals("candidate") && p.getUserId()
                    .equals(userRole.toString())) {

                theTable = taskService.getVariables(o.getId());

                taskService.setOwner(o.getId(),"student");

                o.setOwner("student");
                log.info(" the task detail is = {}", ToStringBuilder
                        .reflectionToString(o, ToStringStyle.JSON_STYLE));

                taskService.claim(o.getId(),userRole.toString());

                taskService.complete(o.getId(),variables);

                log.info("我是{}完成自己的任务啦!", userRole.toString());

            }

        }

        return theTable;
    }

    /**
     * 查看提交流程的审批情况，同样
     * 目前这个接口也只是提供了一个简单的最近的一条的审批情况，而且是根据用户id来的
     * @return
     */
    @GetMapping("taskStatus")
    public List getTaskStatus(){
        List<HistoricTaskInstance> result = historyService.
                createHistoricTaskInstanceQuery().
                taskOwner("student").orderByHistoricTaskInstanceStartTime().desc().list();

        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().executionId(result.get(0).getExecutionId()).list();

        tasks.forEach(o->{
            System.out.println("history task is:=========="+ToStringBuilder.reflectionToString(o,ToStringStyle.JSON_STYLE));
        });

        return tasks;
    }

    /**
     * 获得任务
     * @return
     */
    @GetMapping("taskInfo")
    public List<TaskVo>  getTaskInfo(){

        // 判断当前访问者的权限
        StringBuilder taskName = new StringBuilder();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if(principal instanceof TblTeacherBase){

            if(((TblTeacherBase) principal).getRoles().get(0).getName().equals("ROLE_dean")){
                taskName.append("院长意见");
            }else if(((TblTeacherBase) principal).getRoles().get(0).getName().equals("ROLE_guideTeacher")){
                taskName.append("指导教师意见");
            }

        }
        // 得到所有任务
        List<Task> tasks = taskService.createTaskQuery().list();
        List<Task> result = tasks.stream().filter(o->
            o.getName().equals(taskName.toString())
        ).collect(Collectors.toList());

        for (Task task : result) {
            System.out.println("task ===========owner is:"+task.getOwner());

        }

        // the result is
        result.forEach(o-> System.out.println(o.toString()));

        List<TaskVo> list1 = listToBeanVo(result, TaskVo.class, "variables");

        for (TaskVo task : list1) {

            Map<String, Object> variables = taskService.getVariables(task.getId());

            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
            task.setProcessDefinationName(repositoryService
                    .getProcessDefinition(task
                            .getProcessDefinitionId()).getName());
            task.setVariables(variables);

            log.info("ID:" + task.getId()+"variables"+variables + ",任务名称:" + task.getName() + ",接收人:" + task.getAssignee() + ",开始时间:" + task.getCreateTime());
        }

        return list1;


    }
    public boolean isFinishProcess(String processInstanceId) {

        /**判断流程是否结束，查询正在执行的执行对象表*/
        ProcessInstance rpi = runtimeService
                .createProcessInstanceQuery()//创建流程实例查询对象
                .processInstanceId(processInstanceId)
                .singleResult();

        System.out.println("任务完成状态"+rpi);
        return rpi == null;
    }

    private  ProcessInstance getProcessInstance() {

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("thesis");
        System.out.println("==========important"+ToStringBuilder.reflectionToString(processInstance,ToStringStyle.JSON_STYLE));
        log.info("启动流程 [{}] 成功", processInstance.getProcessDefinitionKey());
        System.out.println("-------------startuser id:"+processInstance.getStartUserId());
        return processInstance;
    }


    /***
     * 转化显示Bean
     * @param list 待转化列表
     * @param clazz 显示类
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> List<T> listToBeanVo(List list, Class<T> clazz, String... ignoreProperties) {
        if (list == null)
            return null;

        List<T> rlist = new ArrayList<>();
        try {
            for (Object obj : list) {
                T t = objToBeanVo(obj, clazz, ignoreProperties);
                rlist.add(t);
            }
        } catch (Exception e) {
            log.error("listToBeanVo error:" + e.getMessage());
            e.printStackTrace();
        }
        return rlist;
    }

    /**
     * 复制源对象属性到目标对象
     *
     * @param obj
     * @param clazz
     * @param ignoreProperties
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T objToBeanVo(Object obj, Class<T> clazz, String... ignoreProperties) {
        try {
            T t = (T) Class.forName(clazz.getName()).newInstance();
            BeanUtils.copyProperties(obj, t, ignoreProperties);
            return t;
        } catch (Exception e) {
            log.error("objToBeanVo error:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
