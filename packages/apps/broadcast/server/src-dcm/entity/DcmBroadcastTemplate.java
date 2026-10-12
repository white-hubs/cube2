package com.guhecloud.cube.biz.subsystem.broadcast.dcm.entity;

import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcmBroadcastTemplate implements Identifiable {

    /**
     * 名称
     */
    private String name;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否是默认模板
     */
    private Boolean defaulted = Boolean.TRUE;

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String getFingerPrint() {
        return String.format("%s:%s",
                             content == null ? "" : content,
                             defaulted == null ? Boolean.TRUE : defaulted);
    }
}
