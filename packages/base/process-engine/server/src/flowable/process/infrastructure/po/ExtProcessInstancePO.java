package com.guhecloud.cube.base.flow.flowable.process.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.ApplicationId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.BusinessId;
import com.guhecloud.cube.base.flow.flowable.process.domain.valueobject.StartUserInfo;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/9/5 16:35
 * @description
 */
@Data
@TableName(value = "process_instance_running", autoResultMap = true)
public class ExtProcessInstancePO extends BasicEnhancedTenantEntity<String> {
    private String id;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private BusinessId businessId;
    private String businessType;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ApplicationId applicationId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private StartUserInfo startUserInfo;

    private String instanceName;
    private String processDefinitionKey;
    private String data;
    private String applicationCode;
    private String result;
    private Long startUserId;
    private String startUserName;
    private Long organizationId;
    private String organizationName;

    public void setBusinessId(BusinessId businessId) {
        this.businessId = businessId;
        if (Objects.nonNull(businessId)) {
            this.businessType = businessId.getBusinessType();
        }
    }
}
