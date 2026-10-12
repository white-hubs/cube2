package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants.DaHuaApiConstant.VIDEO_RTSP;

/**
 * rtsp实时预览地址获取
 *
 * @author lanchun
 * @date 2025/5/16 15:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaVideoRtspRequest extends AbstractDaHuaRequest {

    /**
     * 视频通道编码，第一个$后数字代表通道类型，必须是1;
     */
    private String channelId;

    /**
     * 视频类型：1=视频
     */
    private String dataType;

    /**
     * 码流类型：1=主码流，2=辅码流，3=辅码流2
     */
    private String streamType;

    @Override
    public String getRequestUrl() {
        return getRequestUrl(VIDEO_RTSP).build();
    }
}
