package com.guhecloud.cube.biz.ouaa.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DcmRole implements Identifiable {

    @JsonIgnore
    private String prodCode;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色图标
     */
    private String icon;

    /**
     * 排序数字
     */
    @Builder.Default
    private Float sort = Float.valueOf("0.0");

    /**
     * 角色备注
     */
    @Builder.Default
    private String remark = "";

    /**
     * 是否内置
     */
    @Builder.Default
    private Boolean builtin = Boolean.FALSE;

    /**
     * 是否启用
     */
    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s", prodCode, name);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s:%s",
                             builtin == null ? Boolean.TRUE : builtin,
                             enabled == null ? Boolean.TRUE : enabled,
                             icon == null ? "" : icon,
                             remark == null ? "" : remark,
                             sort == null ? Float.valueOf("1.0") : sort);
    }
}
