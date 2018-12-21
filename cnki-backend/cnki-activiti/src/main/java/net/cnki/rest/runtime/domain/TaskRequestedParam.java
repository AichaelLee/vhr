package net.cnki.rest.runtime.domain;

import lombok.Data;

/**
 * 用于接受前台传递的 查询Task的参数
 * @author: lizhizhong
 * CreatedDate: 2018/12/20.
 */
@Data
public class TaskRequestedParam {


    /**
     * 任务状态
     */
    private String state;
    /**
     * 部署key
     */
    private String deploymentKey;
    /**
     * 流程实例ID
     */
    private String processInstanceId;

    private String text;

    private String assignment;
    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    private String dueBefore;
    private String dueAfter;

    private String sort;

    private Integer page;

    private Integer size;



}
