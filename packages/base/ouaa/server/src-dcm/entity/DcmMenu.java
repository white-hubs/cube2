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
public class DcmMenu implements Identifiable {
    @JsonIgnore
    private String prodCode;

    @JsonIgnore
    private String terminalCode;

    @JsonIgnore
    private String appCode;

    /**
     * 用于识别菜单全路径的标识
     */
    @JsonIgnore
    private String parentCodePath;

    private String code;

    private String name;

    /**
     * 菜单图标
     */
    @Builder.Default
    private String icon = "";

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 排序数字
     */
    @Builder.Default
    private Float sort = Float.valueOf("1.0");

    /**
     * 菜单备注
     */
    @Builder.Default
    private String remark = "";

    /**
     * 是否启用
     */
    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s:%s:%s:%s", prodCode, terminalCode, appCode, parentCodePath == null ? "" : parentCodePath, code);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s", name, icon == null ? "" : icon, path == null ? "" : path, enabled);
    }
}
