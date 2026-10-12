package com.guhecloud.platform.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 作为配置的实体类，和对应的Param 结构基本一致。在这两者的转换中，可以容纳数据配置语法的调节。
 * <p>
 * Kwargs 被强制限定为str-str map, 避免过于灵活的参数设定。
 * vargs 为定义为Object List, 设计为进行数据传递，允许进行弹性处理。
 * 具体的配置支持模块通常需要提供由这个配置实体到实际的内部配置模型间的转换器，以支持配置模型到实体模型间的转换。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RequestedConfigItem implements Cloneable {
    @NotEmpty private String configType;

    private String version;
    private Boolean isMandatory;
    private Boolean isPartial;  // 注意这个值， 为null 则从上层的Requirement被继承，否则以本地指定为准。
    private Boolean isSystem;   // 缺省值为True或False由业务裁量，即如果可以由config type推导，则以业务推导为准。

    private Boolean isRemoval = Boolean.FALSE;

    private List<String> tenants = new ArrayList<>();

    private Map<String, String> kwargs = new LinkedHashMap<>();

    private List<Object> vargs = new ArrayList<>();

    private List<Object> errors = new ArrayList<>();

    @Override
    public String toString() {
        return "RequestedConfigItem{" +
               "configType='" + configType + '\'' +
               ", version='" + version + '\'' +
               ", isMandatory=" + isMandatory +
               ", isPartial=" + isPartial +
               ", isSystem=" + isSystem +
               ", isRemoval=" + isRemoval +
               ", tenants=" + tenants +
               ", kwargs=" + kwargs +
               ", vargs=" + vargs +
               '}';
    }

    public String briefInfo() {
        return "RequestedConfigItem{" +
               "configType='" + configType + '\'' +
               ", version='" + version + '\'' +
               ", isMandatory=" + isMandatory +
               ", isPartial=" + isPartial +
               ", isSystem=" + isSystem +
               ", isRemoval=" + isRemoval +
               ", tenants=" + tenants +
               ", kwargs=" + kwargs +
               '}';
    }

    public String getKwargs(String key, String defaultValue) {
        return kwargs == null ? defaultValue : kwargs.getOrDefault(key, defaultValue);
    }

    public RequestedConfigItem setKwargs(String key, String value) {
        if (kwargs == null) {
            kwargs = new LinkedHashMap<>();
        }
        kwargs.put(key, value);
        return this;
    }

    private boolean appliedForAllTenant() {
        return isSystem || tenants == null || tenants.isEmpty() || "ALL".equalsIgnoreCase(tenants.get(0));
    }

    public boolean matchedWithTenant(String tenant) {
        if (tenant != null) {
            tenant = tenant.trim().toUpperCase();
        }
        return isSystem && (tenant == null || tenant.isEmpty())
               || !isSystem && (appliedForAllTenant() || tenants.contains(tenant));
    }

    public Object clone() throws CloneNotSupportedException {
        RequestedConfigItem requestedConfigItem = (RequestedConfigItem) super.clone();
        requestedConfigItem.setKwargs(kwargs == null ? null : new LinkedHashMap<>(kwargs));
        requestedConfigItem.setVargs(vargs == null ? null : new ArrayList<>(vargs));
        requestedConfigItem.setTenants(tenants == null ? null : new ArrayList<>(tenants));
        return requestedConfigItem;
    }

}
