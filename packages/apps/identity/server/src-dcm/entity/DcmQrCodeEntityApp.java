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
public class DcmQrCodeEntityApp implements Identifiable {

    /**
     * 实体类型
     */
    @JsonIgnore
    private String entityType;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 是否选中
     */
    @Builder.Default
    private Boolean selected = Boolean.FALSE;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s", entityType, applicationCode);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s", selected);
    }
}
