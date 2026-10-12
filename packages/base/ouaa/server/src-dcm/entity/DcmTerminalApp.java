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
public class DcmTerminalApp implements Identifiable {
    @JsonIgnore
    private String prodCode;
    @JsonIgnore
    private String terminalCode;

    private String appCode;

    private String path;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s:%s:%s", prodCode, terminalCode, appCode, path == null ? "" : path);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        // id 相同即相同
        return path == null ? "" : path;
    }
}
