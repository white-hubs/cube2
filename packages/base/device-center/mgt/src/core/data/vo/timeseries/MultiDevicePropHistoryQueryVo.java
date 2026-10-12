package com.guhecloud.cube.iot.mgt.core.data.vo.timeseries;

import com.guhecloud.cube.iot.mgt.core.def.TimeRangeAware;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 多设备-属性时序历史查询（用于一次SQL批量查询）
 */
@Data
public class MultiDevicePropHistoryQueryVo implements TimeRangeAware {

    private String tenantId;

    private String startTime;

    private String endTime;

    private String order = "desc";

    /**
     * 每个设备最多返回记录数（与单设备查询pageSize语义保持一致）
     */
    private Integer maxRecords = 1000;

    /**
     * 时间分片粒度,单位分钟
     */
    private Integer step = 1;

    /**
     * 聚合类型<br/>
     * min,max,count,sum,avg,any,raw
     */
    private String aggType = "raw";

    /**
     * 查询项：deviceId + propCode（raw模式下可带productId）
     */
    private List<QueryItem> items;

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

    @Data
    public static class QueryItem {
        private String deviceId;
        private String propCode;
        /**
         * raw模式下用于兼容单设备SQL的product_id过滤（可选）
         */
        private String productId;
    }
}

