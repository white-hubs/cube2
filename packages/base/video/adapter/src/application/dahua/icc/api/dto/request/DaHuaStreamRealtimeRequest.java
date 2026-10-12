package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants.DaHuaApiConstant.VIDEO_STREAM_REAL_TIME;

/**
 * HLS、FLV、RTMP实时预览接口方式请求
 *
 * @author lanchun
 * @date 2025/5/16 16:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaStreamRealtimeRequest extends AbstractDaHuaRequest {

    /**
     * 视频通道编码，第一个$后数字代表通道类型，必须是1;
     */
    private String channelId;

    /**
     * 协议类型：hls,hlss,flv,flvs,ws_flv,wss_flv,rtmp
     * hls:http协议，m3u8格式，端口7086;
     * hlss:https协议，m3u8格式，端口是7096;
     * flv:http协议，flv格式,端口7886;
     * flvs:https协议，flv格式,端口是7896;
     * ws_flv:ws协议，flv格式,端口是7886;
     * wss_flv:wss协议，flv格式,端口是7896;
     * rtmp:rtmp协议,端口是1975;
     */
    private String type;

    /**
     * 码流类型：1=主码流，2=辅码流，3=辅码流2
     */
    private String streamType;

    @Override
    public String getRequestUrl() {
        return getRequestUrl(VIDEO_STREAM_REAL_TIME).build();
    }

}
