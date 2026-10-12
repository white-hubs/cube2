package com.guhecloud.cube.biz.emergency.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcmEmergencyEventType implements Identifiable {

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否初始化的
     */
    @Builder.Default
    private Boolean initialized = Boolean.TRUE;

    /**
     * 备注说明
     */
    private String remark;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s", code);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s", name, initialized, remark == null ? "" : remark);
    }
}
