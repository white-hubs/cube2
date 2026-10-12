package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.mq.message;

import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkInQo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 捷顺云-入场记录延时处理消息（20s 后再处理取图/上报）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsCloudParkInDelayMessage {

    public static final String TOPIC = "JS_CLOUD_PARK_IN_DELAY";

    private Integer tenant;

    private List<JsCloudParkInQo> dataItems;
}

