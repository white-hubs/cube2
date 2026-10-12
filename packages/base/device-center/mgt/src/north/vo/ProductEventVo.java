package com.guhecloud.cube.iot.mgt.north.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.guhecloud.cube.iot.mgt.things.vo.EventConfig;
import lombok.Data;

@Data
public class ProductEventVo {
    private String eventName;
    private String eventCode;
    private EventConfig eventConfig;
    private String categoryCode;
    private String categoryName;
    private String remark;
}
