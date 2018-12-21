/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.cnki.rest.runtime;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import net.cnki.logic.PermissionService;
import net.cnki.rest.exception.BadRequestException;
import net.cnki.rest.runtime.domain.TaskRequestedParam;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.TaskInfo;
import org.activiti.engine.task.TaskInfoQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractTaskQueryResource {

    private static final String SORT_CREATED_ASC = "created-asc";
    private static final String SORT_CREATED_DESC = "created-desc";
    private static final String SORT_DUE_ASC = "due-asc";
    private static final String SORT_DUE_DESC = "due-desc";

    private static final int DEFAULT_PAGE_SIZE = 25;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected HistoryService historyService;


    @Autowired
    protected PermissionService permissionService;

    public List listTasks(TaskRequestedParam taskParam) {

        if (taskParam == null) {
            throw new BadRequestException("没有发现任务查询参数!");
        }

       // User currentUser = SecurityUtils.getCurrentUserObject();

        String state = taskParam.getState();
        TaskInfoQueryWrapper taskInfoQueryWrapper = null;
        if (StringUtils.isNotEmpty(state) && "completed".equals(state)) {
            HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
            historicTaskInstanceQuery.finished();
            // 查询已经完成的任务信息
            taskInfoQueryWrapper = new TaskInfoQueryWrapper(historicTaskInstanceQuery);
        } else {
            taskInfoQueryWrapper = new TaskInfoQueryWrapper(taskService.createTaskQuery());
        }

        // 一会我测试的时候就经常把他写成空吧!哈哈 TODO
        String deploymentKey = taskParam.getDeploymentKey();
        if (StringUtils.isNotEmpty(deploymentKey)) {
            List<Deployment> deployments = repositoryService.createDeploymentQuery().deploymentKey(deploymentKey).list();
            List<String> deploymentIds = new ArrayList<String>(deployments.size());
            for (Deployment deployment : deployments) {
                deploymentIds.add(deployment.getId());
            }

            taskInfoQueryWrapper.getTaskInfoQuery().or()
                    .deploymentIdIn(deploymentIds)
                    .taskCategory(deploymentKey)
                    .endOr();
        }

        // 流程实例ID
        String processInstanceId = taskParam.getProcessInstanceId();
        if (StringUtils.isNotEmpty(processInstanceId)) {
            handleProcessInstanceFiltering(taskInfoQueryWrapper, processInstanceId);
        }

        // text
        if (StringUtils.isNotEmpty(taskParam.getText())) {
            handleTextFiltering(taskInfoQueryWrapper, taskParam.getText());
        }

//        if (StringUtils.isNotEmpty(taskParam.getAssignment())) {
//            handleAssignment(taskInfoQueryWrapper, taskParam.getAssignment(), currentUser);
//        }

        // processDefinitionId
        if (StringUtils.isNotEmpty(taskParam.getProcessDefinitionId())) {
            handleProcessDefinition(taskInfoQueryWrapper, taskParam.getProcessDefinitionId());
        }

        String dueBefore = taskParam.getDueBefore();
        if (StringUtils.isNotEmpty(dueBefore)) {
            handleDueBefore(taskInfoQueryWrapper, dueBefore);
        }

        String dueAfter = taskParam.getDueAfter();
        if (StringUtils.isNotEmpty(dueAfter)) {
            handleDueAfter(taskInfoQueryWrapper, dueAfter);
        }

        String sort = taskParam.getSort();
        if (StringUtils.isNotEmpty(sort)) {
            handleSorting(taskInfoQueryWrapper, sort);
        }

        int page = taskParam.getPage() == null ? 0 : taskParam.getPage();

        int size = taskParam.getSize() == null ? DEFAULT_PAGE_SIZE:taskParam.getSize();


        List<? extends TaskInfo> tasks = taskInfoQueryWrapper.getTaskInfoQuery().listPage(page * size, size);

       // hahahah  tasks就是根据条件我们最终查询出来的任务啦

        return tasks;
    }

    private void handleProcessInstanceFiltering(TaskInfoQueryWrapper taskInfoQueryWrapper,
                                                String processInstanceId) {
        taskInfoQueryWrapper.getTaskInfoQuery().processInstanceId(processInstanceId);
    }

    private void handleTextFiltering(TaskInfoQueryWrapper taskInfoQueryWrapper,
                                     String text) {

        // [4/9/2014] Used to be an or on description too, but doesnt work combined with the or query for an app.
        // (Would need a change in Activiti)
        taskInfoQueryWrapper.getTaskInfoQuery().taskNameLikeIgnoreCase("%" + text + "%");
    }

    private void handleAssignment(TaskInfoQueryWrapper taskInfoQueryWrapper,
                                  String assignment,
                                  User currentUser) {
        if (assignment.length() > 0) {
            String currentUserId = String.valueOf(currentUser.getId());
            if ("assignee".equals(assignment)) {
                taskInfoQueryWrapper.getTaskInfoQuery().taskAssignee(currentUserId);
            } else if ("candidate".equals(assignment)) {
                taskInfoQueryWrapper.getTaskInfoQuery().taskCandidateUser(currentUserId);
            } else if (assignment.startsWith("group_")) {
                String groupIdString = assignment.replace("group_", "");
                try {
                    Long.valueOf(groupIdString);
                } catch (NumberFormatException e) {
                    throw new BadRequestException("Invalid group id");
                }
                taskInfoQueryWrapper.getTaskInfoQuery().taskCandidateGroup(groupIdString);
            } else { // Default = involved
                taskInfoQueryWrapper.getTaskInfoQuery().taskInvolvedUser(currentUserId);
            }
        }
    }

    private void handleProcessDefinition(TaskInfoQueryWrapper taskInfoQueryWrapper,
                                         String processDefinitionId) {
        taskInfoQueryWrapper.getTaskInfoQuery().processDefinitionId(processDefinitionId);
    }

    private void handleDueBefore(TaskInfoQueryWrapper taskInfoQueryWrapper,
                                 String date) {
        try {
            Date d = ISO8601Utils.parse(date, new ParsePosition(0));
            taskInfoQueryWrapper.getTaskInfoQuery().taskDueBefore(d);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void handleDueAfter(TaskInfoQueryWrapper taskInfoQueryWrapper,
                                String date) {
        try {
            Date d = ISO8601Utils.parse(date, new ParsePosition(0));
            taskInfoQueryWrapper.getTaskInfoQuery().taskDueAfter(d);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void handleSorting(TaskInfoQueryWrapper taskInfoQueryWrapper,
                               String sort) {

        if (SORT_CREATED_ASC.equals(sort)) {
            taskInfoQueryWrapper.getTaskInfoQuery().orderByTaskCreateTime().asc();
        } else if (SORT_CREATED_DESC.equals(sort)) {
            taskInfoQueryWrapper.getTaskInfoQuery().orderByTaskCreateTime().desc();
        } else if (SORT_DUE_ASC.equals(sort)) {
            taskInfoQueryWrapper.getTaskInfoQuery().orderByDueDateNullsLast().asc();
        } else if (SORT_DUE_DESC.equals(sort)) {
            taskInfoQueryWrapper.getTaskInfoQuery().orderByDueDateNullsLast().desc();
        } else {
            // Default
            taskInfoQueryWrapper.getTaskInfoQuery().orderByTaskCreateTime().desc();
        }
    }

//
//    protected List<TaskVo> convertTaskInfoList(List<? extends TaskInfo> tasks,
//                                               Map<String, String> processInstanceNames) {
//        List<TaskVo> result = new ArrayList<TaskVo>();
//        if (CollectionUtils.isNotEmpty(tasks)) {
//            for (TaskInfo task : tasks) {
//                ProcessDefinitionEntity processDefinition = null;
//                if (task.getProcessDefinitionId() != null) {
//                    processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
//                }
//                TaskVo representation = new TaskVo(task, processDefinition, processInstanceNames.get(task.getProcessInstanceId()));
//
//                if (StringUtils.isNotEmpty(task.getAssignee())) {
//                   // todo
//                    representation.setAssignee("testestset");
//                }
//
//                result.add(representation);
//            }
//        }
//        return result;
//    }
}
