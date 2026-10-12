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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmTenantProduct implements Identifiable {
    /**
     * 产品名称
     */
    private String prodCode;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s", prodCode);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return "";
    }
}
