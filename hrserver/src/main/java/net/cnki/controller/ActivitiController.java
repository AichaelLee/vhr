package net.cnki.controller;

import lombok.extern.slf4j.Slf4j;
import net.cnki.bean.Managers;
import net.cnki.bean.TblStudentBase;
import net.cnki.bean.TblTeacherBase;
import net.cnki.bean.activiti.presetation.ProcessInstanceRepresentation;
import net.cnki.bean.activiti.vo.TaskVo;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lizhizhong
 * CreatedDate: 2018/12/14.
 */
@Slf4j
@RestController
@RequestMapping("/system/basic")
public class ActivitiController {

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

    @GetMapping("startProcess")
    public void startProcess(){

        getProcessInstance();

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


    @GetMapping("claim")
    public void claimTask(Map<String,Object> variables){

        // 得到所有任务
        List<Task> tasks = taskService.createTaskQuery().list();

        // 如果账号类型为 [学生] TODO 目前的userType是不对应的 后期要替换为下面的student等
//        if(UserUtils.getCurrentUser().getId().equals(UserTypeEnum.STUDENT.getUserType())){
//
//        }

        String userRole = new String();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof Managers){
            userRole = "admin";

        }else if(principal instanceof TblTeacherBase){
            System.out.println("该用户的权限为:"+((TblTeacherBase) principal).getAuthorities());

            if(((TblTeacherBase) principal).getAuthorities().equals("ROLE_dean")){
                userRole ="dean";
            }else{
                //
                userRole = "teacher";
            }

        }else if(principal instanceof TblStudentBase){
            userRole = "student";

        }

        // TODO 临时测试使用
        variables.put("论文题目","springboot整合工作流测试");
        variables.put("提交日期","我周末提交的啦");

        log.info("当前用户所拥有的角色为:",userRole.toString());

        for (Task o : tasks) {
            System.out.println("all task is:" + o.toString());
            // 得到认证关系
            List<IdentityLink> identitiesList = taskService.getIdentityLinksForTask(o.getId());

            for (IdentityLink p : identitiesList) {

                if (p.getType().equals("candidate") && p.getUserId().equals(userRole)) {
                    log.info("我是{},我提交自己的论文成功啦!", userRole);
                    taskService.claim(o.getId(), userRole);
                    taskService.complete(o.getId(), variables);
                    log.info("我是{}完成自己的任务啦!", userRole);


                    log.info("任务是否完成状态:", isFinishProcess(o.getProcessInstanceId())==true?'是':'否');

                }

            }

        }
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


        Map<String,Object> variables = new HashMap<>();
        variables.put("tile","from springbooy");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("thesis",variables);
        log.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());
        //启动流程second_approve
        return processInstance;
    }
    public List queryTask(){

        // 获取任务服务对象
        // 根据指派人获取用户任务
        TaskQuery taskQuery = taskService.createTaskQuery();

        List<Task> tasks = taskQuery.list();
        List<TaskVo> list1 = null;
        if(CollectionUtils.isNotEmpty(tasks)){
            list1 = listToBeanVo(tasks, TaskVo.class, "variables");

            for (TaskVo task : list1) {

                Map<String, Object> variables = taskService.getVariables(task.getId());
                task.setVariables(variables);
                taskService.complete(task.getId());

                TaskFormData formData = formService.getTaskFormData(task.getId());
                List<FormProperty> properties = formData.getFormProperties();


                log.info("ID:" + task.getId() + ",任务名称:" + task.getName() + ",接收人:" + task.getAssignee() + ",开始时间:" + task.getCreateTime());
            }
        }

        return null;
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
