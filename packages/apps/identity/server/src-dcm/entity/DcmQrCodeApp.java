package com.guhecloud.cube.srv.gen.identity.dcm.entity;

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
public class DcmQrCodeApp implements Identifiable {

    /**
     * 应用编号
     */
    private String code;

    /**
     * 应用名称
     */
    private String name;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s", code);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s", name == null ? "" : name);
    }
}
