package com.guhecloud.platform.dcm.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.utils.DcmTools;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestedConfigItemParam {
    @NotNull
    private String configType;
    private String version;
    private Boolean isMandatory;
    private Boolean isPartial;
    private Boolean isRemoval = Boolean.FALSE;
    private Boolean isSystem; // 显式指定是否为系统配置项. 当显式指定的system 与tenants冲突时, 以isSystem为准; 如果isSystem显式为false, 而tenants未指定, 相当于指定为all.
    private Object tenants = new ArrayList<>(); // 可选类型有String, 数组/列表: all/tenantId/逗号分割的tenantId/tenantId数组等.
    private Map<String, String> kwargs = new HashMap<>();
    private List<Object> vargs = new ArrayList<>();

    public RequestedConfigItem toEntity() {
        RequestedConfigItem configItem = new RequestedConfigItem();
        configItem.setConfigType(configType);
        configItem.setVersion(version);
        configItem.setIsMandatory(isMandatory == null || isMandatory); // default: True
        configItem.setIsRemoval(Boolean.TRUE.equals(isRemoval));  // default: False
        configItem.setIsPartial(isPartial); // default: null

        List<String> parsedTenants = tenants == null ? Collections.emptyList() : DcmTools.parseStrColl(tenants, ",");
        configItem.setIsSystem(isSystem == null ? parsedTenants.isEmpty() : isSystem);
        if (configItem.getIsSystem()) {
            parsedTenants = Collections.emptyList();
        } else {
            if (parsedTenants.isEmpty()) {
                parsedTenants = Collections.singletonList("ALL");
            }
        }
        configItem.setTenants(parsedTenants);
        configItem.setKwargs(kwargs);
        configItem.setVargs(vargs);
        return configItem;
    }

    @JsonIgnore
    public boolean isSystem() {
        Collection<String> regulatedTenants = tenants == null ? Collections.emptyList() : DcmTools.parseStrColl(tenants, ",");
        return regulatedTenants.isEmpty();
    }

    public static RequestedConfigItemParam fromEntity(RequestedConfigItem requestedConfigItem) {
        RequestedConfigItemParam requestedConfigItemParam = new RequestedConfigItemParam();
        requestedConfigItemParam.setConfigType(requestedConfigItem.getConfigType());
        requestedConfigItemParam.setTenants(requestedConfigItem.getTenants() == null ?
                                            Collections.emptyList() : requestedConfigItem.getTenants());
        requestedConfigItemParam.setIsMandatory(requestedConfigItem.getIsMandatory());
        requestedConfigItemParam.setIsPartial(requestedConfigItem.getIsPartial());
        requestedConfigItemParam.setIsRemoval(requestedConfigItem.getIsRemoval());
        requestedConfigItemParam.setKwargs(requestedConfigItem.getKwargs());
        requestedConfigItemParam.setVargs(requestedConfigItem.getVargs());
        requestedConfigItemParam.setVersion(requestedConfigItem.getVersion());
        requestedConfigItemParam.setIsSystem(requestedConfigItem.getIsSystem());
        return requestedConfigItemParam;
    }
}
