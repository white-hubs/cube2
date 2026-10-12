//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.activiti.engine.impl.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.BulkDeleteable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IdentityLinkEntityImpl extends AbstractEntityNoRevision implements IdentityLinkEntity, Serializable, BulkDeleteable {
    private static final long serialVersionUID = 1L;
    protected String type;
    protected String userId;
    protected String groupId;
    protected String taskId;
    protected String processInstanceId;
    protected String processDefId;
    protected TaskEntity task;
    protected ExecutionEntity processInstance;
    @JsonIgnore
    protected ProcessDefinitionEntity processDef;
    protected byte[] details;

    public Object getPersistentState() {
        Map<String, Object> persistentState = new HashMap();
        persistentState.put("id", this.id);
        persistentState.put("type", this.type);
        if (this.userId != null) {
            persistentState.put("userId", this.userId);
        }

        if (this.groupId != null) {
            persistentState.put("groupId", this.groupId);
        }

        if (this.taskId != null) {
            persistentState.put("taskId", this.taskId);
        }

        if (this.processInstanceId != null) {
            persistentState.put("processInstanceId", this.processInstanceId);
        }

        if (this.processDefId != null) {
            persistentState.put("processDefId", this.processDefId);
        }

        if (this.details != null) {
            persistentState.put("details", this.details);
        }

        return persistentState;
    }

    public boolean isUser() {
        return this.userId != null;
    }

    public boolean isGroup() {
        return this.groupId != null;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        if (this.groupId != null && userId != null) {
            throw new ActivitiException("Cannot assign a userId to a task assignment that already has a groupId");
        } else {
            this.userId = userId;
        }
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        if (this.userId != null && groupId != null) {
            throw new ActivitiException("Cannot assign a groupId to a task assignment that already has a userId");
        } else {
            this.groupId = groupId;
        }
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcessInstanceId() {
        return this.processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefId() {
        return this.processDefId;
    }

    public void setProcessDefId(String processDefId) {
        this.processDefId = processDefId;
    }

    public TaskEntity getTask() {
        if (this.task == null && this.taskId != null) {
            this.task = (TaskEntity)Context.getCommandContext().getTaskEntityManager().findById(this.taskId);
        }

        return this.task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
        this.taskId = task.getId();
    }

    public ExecutionEntity getProcessInstance() {
        if (this.processInstance == null && this.processInstanceId != null) {
            this.processInstance = (ExecutionEntity)Context.getCommandContext().getExecutionEntityManager().findById(this.processInstanceId);
        }

        return this.processInstance;
    }

    public void setProcessInstance(ExecutionEntity processInstance) {
        this.processInstance = processInstance;
        this.processInstanceId = processInstance.getId();
    }

    public ProcessDefinitionEntity getProcessDef() {
        if (this.processDef == null && this.processDefId != null) {
            this.processDef = (ProcessDefinitionEntity)Context.getCommandContext().getProcessDefinitionEntityManager().findById(this.processDefId);
        }

        return this.processDef;
    }

    public void setProcessDef(ProcessDefinitionEntity processDef) {
        this.processDef = processDef;
        this.processDefId = processDef.getId();
    }

    public String getProcessDefinitionId() {
        return this.processDefId;
    }

    public void setDetails(byte[] details) {
        this.details = details;
    }

    public byte[] getDetails() {
        return this.details;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IdentityLinkEntity[id=").append(this.id);
        sb.append(", type=").append(this.type);
        if (this.userId != null) {
            sb.append(", userId=").append(this.userId);
        }

        if (this.groupId != null) {
            sb.append(", groupId=").append(this.groupId);
        }

        if (this.taskId != null) {
            sb.append(", taskId=").append(this.taskId);
        }

        if (this.processInstanceId != null) {
            sb.append(", processInstanceId=").append(this.processInstanceId);
        }

        if (this.processDefId != null) {
            sb.append(", processDefId=").append(this.processDefId);
        }

        if (this.details != null) {
            sb.append(", details=").append(new String(this.details));
        }

        sb.append("]");
        return sb.toString();
    }
}
