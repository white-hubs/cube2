//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.activiti.engine.impl.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.activiti.engine.impl.bpmn.data.IOSpecification;
import org.activiti.engine.impl.context.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessDefinitionEntityImpl extends AbstractEntity implements ProcessDefinitionEntity, Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected String description;
    protected String key;
    protected int version;
    protected String category;
    protected String deploymentId;
    protected String resourceName;
    protected String tenantId = "";
    protected Integer historyLevel;
    protected String diagramResourceName;
    protected boolean isGraphicalNotationDefined;
    protected Map<String, Object> variables;
    protected boolean hasStartFormKey;
    protected int suspensionState;
    protected boolean isIdentityLinksInitialized;
    @JsonIgnore
    protected List<IdentityLinkEntity> definitionIdentityLinkEntities;
    protected IOSpecification ioSpecification;
    protected Integer appVersion;
    protected String engineVersion;

    public ProcessDefinitionEntityImpl() {
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
        this.definitionIdentityLinkEntities = new ArrayList();
    }

    public Object getPersistentState() {
        Map<String, Object> persistentState = new HashMap();
        persistentState.put("suspensionState", this.suspensionState);
        persistentState.put("category", this.category);
        return persistentState;
    }

    @JsonIgnore
    public List<IdentityLinkEntity> getIdentityLinks() {
        if (!this.isIdentityLinksInitialized) {
            this.definitionIdentityLinkEntities = Context.getCommandContext().getIdentityLinkEntityManager().findIdentityLinksByProcessDefinitionId(this.id);
            this.isIdentityLinksInitialized = true;
        }

        return this.definitionIdentityLinkEntities;
    }

    @Override
    public void setIdentityLinks(List<IdentityLinkEntity> identityLinks) {
        this.definitionIdentityLinkEntities = identityLinks;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDeploymentId() {
        return this.deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getHistoryLevel() {
        return this.historyLevel;
    }

    public void setHistoryLevel(Integer historyLevel) {
        this.historyLevel = historyLevel;
    }

    public Map<String, Object> getVariables() {
        return this.variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDiagramResourceName() {
        return this.diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public boolean hasStartFormKey() {
        return this.hasStartFormKey;
    }

    public boolean getHasStartFormKey() {
        return this.hasStartFormKey;
    }

    public void setStartFormKey(boolean hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }

    public void setHasStartFormKey(boolean hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }

    public boolean isGraphicalNotationDefined() {
        return this.isGraphicalNotationDefined;
    }

    public boolean hasGraphicalNotation() {
        return this.isGraphicalNotationDefined;
    }

    public void setGraphicalNotationDefined(boolean isGraphicalNotationDefined) {
        this.isGraphicalNotationDefined = isGraphicalNotationDefined;
    }

    public int getSuspensionState() {
        return this.suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }

    public boolean isSuspended() {
        return this.suspensionState == SuspensionState.SUSPENDED.getStateCode();
    }

    public String getEngineVersion() {
        return this.engineVersion;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public IOSpecification getIoSpecification() {
        return this.ioSpecification;
    }

    public void setIoSpecification(IOSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }

    public String toString() {
        return "ProcessDefinitionEntity[" + this.id + "]";
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getAppVersion() {
        return this.appVersion;
    }

}
