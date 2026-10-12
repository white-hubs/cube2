package com.guhecloud.cube.srv.gen.maintain.dcm.fault.entity;

import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcmFaultType implements Identifiable {

    /**
     * 名称
     */
    private String name;

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String getFingerPrint() {
        return "";
    }
}
