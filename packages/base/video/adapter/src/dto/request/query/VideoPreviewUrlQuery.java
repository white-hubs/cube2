package com.guhecloud.cube.srv.gen.adapter.video.server.dto.request.query;

import lombok.Data;

@Data
public class VideoPreviewUrlQuery {

    /**
     * 0 主码流/1 副码流/2 第三码流 默认主码流
     */
    private Integer streamType;

    /**
     * NATIVE/RTSP/WS/RTC/HLS/RTMP (NATIVE在明确知道底层设备使用何种设备原生协议时使用, 返回底层设备原生码流) 默认WS (在实现自身的流媒体平台前, 仅支持WS协议)
     */
    private String protocol;

    /**
     * AUTO/UDP/TCP/MUTLICAST 码流传输层协议, AUTO是底层支持的默认协议或自动协商的协议. 仅对于rtsp等涉及传输层协议选择的协议适用. 对于WS/RTC 等无效.
     */
    private String transportMode;

    /**
     * RTP/PS 输出码率封装格式,默认RTP
     */
    private String streamFormat;
}
