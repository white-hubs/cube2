package org.activiti.engine.impl.persistence.entity;

import org.activiti.engine.api.internal.Internal;
import org.activiti.engine.impl.db.HasRevision;
import org.activiti.engine.repository.ProcessDefinition;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/10 16:38
 * @Description:
 */
@Internal
public interface ProcessDefinitionEntity extends ProcessDefinition, Entity, HasRevision {

    List<IdentityLinkEntity> getIdentityLinks();

    void setIdentityLinks(List<IdentityLinkEntity> identityLinks);

    void setKey(String key);

/**
 * 设置名称的方法
 * @param name 要设置的名称参数
 */
    void setName(String name);

    void setDescription(String description);

    void setDeploymentId(String deploymentId);

    void setVersion(int version);

    void setResourceName(String resourceName);

    void setTenantId(String tenantId);

    Integer getHistoryLevel();

    void setHistoryLevel(Integer historyLevel);

    void setCategory(String category);

    void setDiagramResourceName(String diagramResourceName);

    boolean getHasStartFormKey();

    void setStartFormKey(boolean hasStartFormKey);

    void setHasStartFormKey(boolean hasStartFormKey);

    boolean isGraphicalNotationDefined();

    void setGraphicalNotationDefined(boolean isGraphicalNotationDefined);

    int getSuspensionState();

    void setSuspensionState(int suspensionState);

    String getEngineVersion();

    void setEngineVersion(String engineVersion);
}
