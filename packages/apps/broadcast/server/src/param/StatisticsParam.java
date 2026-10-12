package com.guhecloud.cube.biz.subsystem.broadcast.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class StatisticsParam {
    private String type;
    private Period period;
    private List<String> names;
    private Set<Long> ids;
    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    private String dateFormat;
}
