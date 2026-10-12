package com.guhecloud.cube.biz.ouaa.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class App implements Identifiable {
    private String code;

    @JsonIgnore      // 加入JsonIgnore, 避免放在RequestConfigItem的vargs中时被暴露出来。
    /** 组名称 */
    private String group;

    private String name;

    /**
     * 应用图标
     */
    private String icon;

    /**
     * 应用备注
     */
    @Builder.Default
    private String remark = "";

    /**
     * 是否启用
     */
    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    public static App from(Map<String, Object> object) {
        return ConvertUtils.convert(App.class, object);
    }

    @Override
    @JsonIgnore
    public String getId() {
        return "" + getCode();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s:%s", group, name, icon, enabled, remark == null ? "" : remark);
    }
}
