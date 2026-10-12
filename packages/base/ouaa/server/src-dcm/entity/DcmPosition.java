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
public class DcmPosition implements Identifiable {

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 排序数字
     */
    private Float sort = Float.valueOf("0.0");

    /**
     * 岗位备注
     */
    private String remark = "";

    /**
     * 是否启用
     */
    private Boolean enabled = Boolean.TRUE;

    /**
     * 扩展字段（JSON）
     */
    private String extension;

    @Override
    @JsonIgnore
    public String getId() {
        return getName();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s",
                             enabled == null ? Boolean.TRUE : enabled,
                             sort == null ? Float.valueOf("1.0") : sort,
                             remark == null ? "" : remark,
                             extension == null ? "" : extension);
    }
}
