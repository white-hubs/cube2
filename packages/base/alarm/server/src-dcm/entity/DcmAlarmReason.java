package com.guhecloud.cube.base.alarm.dcm.entity;

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
public class DcmAlarmReason implements Identifiable {

    /**
     * 编号
     */
    private String code;

    /**
     * 是否为初始化的
     */
    private Boolean initialized;

    /**
     * 原因
     */
    private String reason;

    /**
     * 原因备注
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
        return String.format("%s:%s:%s", reason, initialized, remark == null ? "" : remark);
    }
}
