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
public class DcmQrCodeEntityStyle implements Identifiable {

    /**
     * 实体类型
     */
    private String entityType;

    /**
     * 样式名称
     */
    private String styleName;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s", entityType);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s", styleName == null ? "" : styleName);
    }
}
