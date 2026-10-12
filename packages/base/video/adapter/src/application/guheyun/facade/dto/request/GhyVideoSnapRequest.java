package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GhyVideoSnapRequest extends AbstractGhyRequest {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 主码流:false,子码流:true
     */
    private Boolean isSubStream;

    @Override
    public String getUrl() {
        return getUrl("/api/stream/aggregator/snap")
                .append("?deviceId={deviceId}")
                .append("&isSubStream={isSubStream}")
                .toString();
    }
}
