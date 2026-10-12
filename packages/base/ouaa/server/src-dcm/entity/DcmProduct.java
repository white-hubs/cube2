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
public class DcmProduct implements Identifiable {
    private String code;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品域名
     */
    private String domain;

    /**
     * 产品备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled = Boolean.TRUE;

    @Override
    @JsonIgnore
    public String getId() {
        return getCode();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s",
                             name, domain == null ? "" : domain,
                             enabled, remark == null ? "" : remark);
    }
}
