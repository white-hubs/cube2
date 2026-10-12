package com.guhecloud.cube.biz.ouaa.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DcmProdApp implements Identifiable {
    @JsonIgnore
    private String prodCode;

    private String appCode;

    @Override
    @JsonIgnore
    public String getId() {
        return prodCode + ":" + appCode;
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        // id 相同即相同
        return "";
    }
}
