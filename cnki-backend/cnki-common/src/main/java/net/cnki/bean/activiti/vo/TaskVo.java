package net.cnki.bean.activiti.vo;



import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @author Allen
 * @Date: 2018/6/27 20:18
 * @Description:
 */
@Data
public class TaskVo extends BaseVo{

    protected String id;
    protected String owner;
    protected int assigneeUpdatedCount;
    protected String originalAssignee;
    protected String assignee;
    protected String parentTaskId;
    protected String name;
    protected String localizedName;
    protected String description;
    protected String localizedDescription;
    protected int priority = 50;
    protected Date createTime;
    protected Date dueDate;
    protected int suspensionState;
    protected String category;
    protected boolean isIdentityLinksInitialized;
    protected String executionId;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String taskDefinitionKey;
    protected String formKey;
    protected boolean isDeleted;
    protected boolean isCanceled;
    protected String eventName;
    protected String tenantId;
    protected boolean forcedUpdate;
    protected Date claimTime;
    protected String processDefinationName;
    protected Map<String, Object> variables;


}
