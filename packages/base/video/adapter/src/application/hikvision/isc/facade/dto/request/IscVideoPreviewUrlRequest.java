package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.config.IscClientHttpConfig;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IscVideoPreviewUrlRequest extends BasicRequest {

    /**
     * 监控点唯一标识
     */
    private String cameraIndexCode;

    /**
     * 码流类型，0:主码流
     * 1:子码流
     * 2:第三码流
     * 参数不填，默认为主码流
     */
    private Integer streamType;

    /**
     * 取流协议（应用层协议），
     * “hik”:HIK私有协议，使用视频SDK进行播放时，传入此类型；
     * “rtsp”:RTSP协议；
     * “rtmp”:RTMP协议（RTMP协议只支持海康SDK协议、EHOME协议、ONVIF协议接入的设备；只支持H264视频编码和AAC音频编码）；
     * “hls”:HLS协议（HLS协议只支持海康SDK协议、EHOME协议、ONVIF协议接入的设备；只支持H264视频编码和AAC音频编码）；
     * “ws”:Websocket协议（一般用于H5视频播放器取流播放）。
     * 参数不填，默认为HIK协议
     */
    private String protocol;

    /**
     * 传输协议（传输层协议），0:UDP
     * 1:TCP
     * 默认是TCP
     * 注：GB28181 2011及以前版本只支持UDP传输
     */
    private Integer transmode;

    /**
     * 标识扩展内容，格式：key=value，
     * 调用方根据其播放控件支持的解码格式选择相应的封装类型；
     * 多个扩展时，以“&”隔开；
     */
    private String expand;

    /**
     * 输出码流转封装格式，“ps”:PS封装格式、“rtp”:RTP封装协议。当protocol=rtsp时生效，且不传值时默认为RTP封装协议。
     */
    private String streamform;

    @Override
    public Map<String, String> getUrl() {
        IscClientHttpConfig iscClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(getRequestConfig().getManufacturerJson()), IscClientHttpConfig.class);
        if (StringUtils.isNotBlank(iscClientHttpConfig.getVersion()) && "v1".equals(iscClientHttpConfig.getVersion())) {
            return getUrl("/api/video/v1/cameras/previewURLs").build();
        }
        return getUrl("/api/video/v2/cameras/previewURLs").build();
    }
}
