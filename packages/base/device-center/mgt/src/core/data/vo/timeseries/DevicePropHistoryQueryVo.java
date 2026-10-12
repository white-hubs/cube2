package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import com.guhecloud.cube.iot.mgt.core.def.TimeRangeAware;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class DevicePropHistoryQueryVo implements TimeRangeAware {
    private String tenantId;
    private String prodCategoryId;
    private String productId;
    private Long deviceId;
    private List<String> propCodes;
    private String startTime;
    private String endTime;
    private String order = "desc";
    /**
     * 默认最大1000条,避免一次性查询过多数据
     */
    private Integer maxRecords = 1000;

    /**
     * 时间分片粒度,单位分钟
     */
    private Integer step = 1;

    /**
     * 聚合类型<br/>
     * min,max,count,sum,avg,any
     */
    private String aggType = "raw";

    public String getOrder() {
        if (StringUtils.isBlank(order)) {
            return "desc";
        }

        return order;
    }

    public Integer getMaxRecords() {
        if (Objects.isNull(maxRecords) || maxRecords <= 0) {
            return 1000;
        }

        return maxRecords;
    }
}
