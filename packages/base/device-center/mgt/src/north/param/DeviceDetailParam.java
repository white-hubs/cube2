package com.guhecloud.cube.iot.mgt.north.param;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class DeviceDetailParam {
    @JSONField(serializeFeatures = JSONWriter.Feature.WriteLongAsString)
    private Long deviceId;
    private boolean withSnapShot;
    private boolean withEventSpec;
    private boolean withCommandSpec;
    private boolean withPropertySpec;
    private boolean withExtConfig;
}
