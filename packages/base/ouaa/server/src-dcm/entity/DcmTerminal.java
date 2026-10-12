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
public class DcmTerminal implements Identifiable {
    /**
     * 访问端编号
     */
    private String code;

    @JsonIgnore
    private String prodCode;

    /**
     * 访问端名称
     */
    private String name;

    /**
     * 访问端备注
     */
    private String remark = "";

    /**
     * 是否启用
     */
    private Boolean enabled = Boolean.TRUE;

    @Override
    @JsonIgnore
    public String getId() {
        return prodCode + ":" + code;
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s", name, remark == null ? "" : remark, enabled);
    }
}
