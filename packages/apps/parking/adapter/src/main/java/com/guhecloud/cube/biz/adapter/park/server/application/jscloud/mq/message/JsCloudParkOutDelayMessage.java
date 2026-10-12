package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.mq.message;

import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkOutQo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 捷顺云-出场记录延时处理消息（20s 后再处理取图/上报）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsCloudParkOutDelayMessage {

    public static final String TOPIC = "JS_CLOUD_PARK_OUT_DELAY";

    private Integer tenant;

    private List<JsCloudParkOutQo> dataItems;
}

