package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnisInsightPtzRequest extends AbstractUnisInsightRequest {

    @JsonIgnore
    private String requestPath;

    /**
     * 通道编码
     */
    @JsonProperty("channel_code")
    private String channelCode;

    /**
     * 转动方向
     */
    private Integer direction;

    /**
     * 参数调节操作类型
     */
    private Integer operation;

    /**
     * 转速或调整速率
     */
    private Integer speed;

    @JsonIgnore
    public HttpEntity<Map<String, Object>> getBodyHttpEntity() {
        Map<String, Object> body = new HashMap<>(4);
        body.put("channel_code", channelCode);
        if (Objects.nonNull(direction)) {
            body.put("direction", direction);
        }
        if (Objects.nonNull(operation)) {
            body.put("operation", operation);
        }
        if (Objects.nonNull(speed)) {
            body.put("speed", speed);
        }
        return new HttpEntity<>(body, this.getHttpHeaders());
    }

    @Override
    public String getUrl() {
        return getUrl(requestPath).build();
    }
}
