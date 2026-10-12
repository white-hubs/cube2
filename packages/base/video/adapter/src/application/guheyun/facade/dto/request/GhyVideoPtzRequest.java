package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GhyVideoPtzRequest extends AbstractGhyRequest {

    /**
     * 设备主键
     */
    private String deviceId;

    /**
     * 控制指令 允许值: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop
     */
    private String command;

    /**
     * 速度(0~255) 默认值: 129
     */
    private Integer speed;

    @Override
    public String getUrl() {
        return getUrl("/api/stream/aggregator/ptz")
                .append("?deviceId={deviceId}")
                .append("&command={command}")
                .append("&speed={speed}")
                .toString();
    }
}
