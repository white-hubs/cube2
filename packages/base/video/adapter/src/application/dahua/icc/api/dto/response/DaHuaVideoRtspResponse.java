package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response;

import lombok.Data;

@Data
public class DaHuaVideoRtspResponse {

    /**
     * (已废弃)最低码流，要求客户具备的最低码流
     */
    private String minRate;

    /**
     * (已废弃)协议：1=CNM3, 2=RTSP
     */
    private String protocol;

    /**
     * (已废弃)IP地址，根据类型可能是转发服务器IP，也可能是设备IP
     */
    private String ip;

    /**
     * (已废弃)端口
     */
    private Integer port;

    /**
     * (已废弃)是否支持STUN协议：1=支持, 0=不支持
     */
    private Boolean stunEnable;

    /**
     * (已废弃)STUN协议端口
     */
    private Integer stunPort;

    /**
     * RTSP地址，完整的播放地址需拼接RTSP令牌访问，RTSP令牌为此接口返回的token字段;
     * 以返回参数示例（无内外网映射：rtsp://10.35.239.81:9100/dss/monitor/param/cameraid=1000018%240%26substream=1）为例,
     * 完整的播放地址：rtsp://10.35.239.81:9100/dss/monitor/param/cameraid=1000018%240%26substream=1?token=170
     * 以返回参数示例（内外网映射：rtsp://10.55.36.202:9100/dss/monitor/param/cameraid=1001337%240%26substream=1|rtsp://115.236.17.59:9022/dss/monitor/param/cameraid=1001337%240%26substream=1）为例，根据竖线截取，选择响应网络地址，拼接token;
     * 内网播放的完整地址：rtsp://10.55.36.202:9100/dss/monitor/param/cameraid=1001337%240%26substream=1?token=20004;
     * 外网播放的完整地址：rtsp://115.236.17.59:9022/dss/monitor/param/cameraid=1001337%240%26substream=1?token=20004;
     */
    private String url;

    /**
     * (已废弃)类型：1=转发模式，2=直连模式
     */
    private String connectType;

    /**
     * (已废弃)转发会话ID
     */
    private String session;

    /**
     * RTSP令牌（有效时间30秒，请及时建立连接），仅能播放一次，失效后重新请求获取
     */
    private String token;

    /**
     * 轨道ID
     */
    private String trackId;
}
