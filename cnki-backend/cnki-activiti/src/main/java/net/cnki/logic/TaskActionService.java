package net.cnki.logic;


import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskActionService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    public ProcessInstance startProcess(){

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("groupTest");
        // 模块名称
        String name = processInstance.getName();
        log.info("开始了一个 {} 流程",name);
        return processInstance;

    }
}
