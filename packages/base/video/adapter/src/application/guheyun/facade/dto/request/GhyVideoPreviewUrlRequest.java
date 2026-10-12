package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GhyVideoPreviewUrlRequest extends AbstractGhyRequest {

    /**
     * 监控点唯一标识
     */
    private String deviceId;

    /**
     * 码流类型，0:主码流
     * 1:子码流
     * 2:第三码流
     * 参数不填，默认为主码流
     */
    private Integer streamType;

    /**
     * 取流协议（应用层协议），
     * NATIVE/RTSP/WS/WSS/RTC/HLS/RTMP
     * (NATIVE在明确知道底层设备使用何种设备原生协议时使用,返回底层设备原生码流)
     * 默认WS(在实现自身的流媒体平台前,仅支持WS协议)
     */
    private String protocol;

    @Override
    public String getUrl() {
        return getUrl("/api/stream/aggregator/startPlay").build();
    }
}
