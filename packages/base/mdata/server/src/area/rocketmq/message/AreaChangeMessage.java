package com.guhecloud.cube.biz.mdata.server.area.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaChangeMessage {

    public static final String TOPIC = "AREA_CHANGE";

    /**
     * 变更的区域id
     */
    private Long id;
}
