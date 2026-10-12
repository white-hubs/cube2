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
public class DcmTenantApp implements Identifiable {

    /**
     * 产品名称
     */
    @JsonIgnore
    private String prodCode;

    private String appCode;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s", prodCode, appCode);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return "";
    }
}
