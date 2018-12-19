package net.cnki.bean.activiti.presetation;

import lombok.Data;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;

import java.util.List;

/**
 * @author: lizhizhong
 * CreatedDate: 2018/12/17.
 */
@Data
public class ProcessInstanceRepresentation {

    private String id;

    protected String endActivityId;
    protected String businessKey;
    protected String startUserId;
    protected String startActivityId;
    protected String superProcessInstanceId;
    protected String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    protected String name;
    protected String localizedName;
    protected String description;
    protected String localizedDescription;
    protected String processDefinitionKey;
    protected String processDefinitionName;
    protected Integer processDefinitionVersion;
    protected String deploymentId;
    protected List<HistoricVariableInstanceEntity> queryVariables;

    public ProcessInstanceRepresentation(HistoricProcessInstance instance){
        this.id = instance.getId();
        this.endActivityId = instance.getEndActivityId();
        this.startUserId = instance.getStartUserId();
        this.startActivityId = instance.getStartActivityId();
        this.superProcessInstanceId = instance.getSuperProcessInstanceId();
        this.tenantId = instance.getTenantId();
        this.processDefinitionName = instance.getProcessDefinitionName();
        this.name = instance.getName();
        this.description = instance.getDescription();


    }
}
