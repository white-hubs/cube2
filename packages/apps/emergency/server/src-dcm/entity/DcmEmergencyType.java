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
public class DcmEmergencyType implements Identifiable {

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s", applicationCode);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s", applicationName);
    }
}
