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
public class DcmQrCodeStyle implements Identifiable {

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 配置的样式json
     */
    private String configJson;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s", code, name);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s", width, height, configJson == null ? "" : configJson);
    }
}
