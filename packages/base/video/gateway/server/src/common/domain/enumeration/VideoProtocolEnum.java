package com.guhecloud.cube.base.videogw.server.common.domain.enumeration;

import lombok.Getter;

@Getter
public enum VideoProtocolEnum {

    NATIVE,//NATIVE在明确知道底层设备使用何种设备原生协议时使用, 返回底层设备原生码流)

    RTSP,//RTSP协议

    WS,//Websocket协议（一般用于H5视频播放器取流播放）

    WSS,//Websocket协议(ssl)

    RTC,//RTC协议

    HLS,//HLS协议（HLS协议只支持海康SDK协议、EHOME协议、ONVIF协议接入的设备；只支持H264视频编码和AAC音频编码

    HLS_S,//HLS协议(ssl)

    RTMP,//RTMP协议

}
