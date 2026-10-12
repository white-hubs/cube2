package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.config.UnisInsightHttpConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnisInsightVideoPreviewUrlRequest extends AbstractUnisInsightRequest {

    /**
     * 通道编码
     */
    private String channelCode;

    /**
     * 流类型，0：主码流，1：辅码流
     */
    private Integer streamType;

    /**
     * 流协议，1：RTSP，2：RTMP，3：HLS，4：HLS_SSL
     */
    private Integer streamMode;

    /**
     * 访问IP
     */
    private String visitIp;

    private Integer portType;

    private Integer h265MuxerType;

    private Integer keepAlive;

    public Map<String, Object> getParamForGet() {
        Map<String, Object> param = new HashMap<>(8);
        param.put("channel_code", channelCode);
        param.put("stream_type", streamType);
        param.put("stream_mode", streamMode);
        param.put("visit_ip", visitIp);
        param.put("port_type", portType);
        param.put("h265_muxer_type", h265MuxerType);
        param.put("keep_alive", keepAlive);
        return param;
    }

    @Override
    public String getUrl() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUrl("/api/vms/v2/webuas/live/stream/url").build())
                .queryParam("channel_code", "{channel_code}")
                .queryParam("stream_type", "{stream_type}")
                .queryParam("stream_mode", "{stream_mode}")
                .queryParam("visit_ip", "{visit_ip}");
        if (Objects.nonNull(portType)) {
            builder.queryParam("port_type", "{port_type}");
        }
        if (Objects.nonNull(h265MuxerType)) {
            builder.queryParam("h265_muxer_type", "{h265_muxer_type}");
        }
        if (Objects.nonNull(keepAlive)) {
            builder.queryParam("keep_alive", "{keep_alive}");
        }
        return builder.build().toUriString();
    }

    public void fillDefaultConfig() {
        UnisInsightHttpConfig config = getRequestConfig();
        if (Objects.isNull(visitIp)) {
            visitIp = Objects.nonNull(config.getVisitIp()) ? config.getVisitIp() : config.getHost();
        }
        if (Objects.isNull(portType)) {
            portType = config.getPortType();
        }
        if (Objects.isNull(h265MuxerType)) {
            h265MuxerType = config.getH265MuxerType();
        }
        if (Objects.isNull(keepAlive)) {
            keepAlive = config.getKeepAlive();
        }
    }
}
