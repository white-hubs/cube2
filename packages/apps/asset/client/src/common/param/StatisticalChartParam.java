package com.guhecloud.cube.property.management.client.common.param;

import com.guhecloud.cube.property.management.client.common.vo.DateTimeSlotStrVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.List;

import static com.guhecloud.cube.property.management.client.common.constant.TimeDimensionConstant.TODAY;

/**
 * 入参
 */
@Data
public class StatisticalChartParam {
    /**
     * 时间维度
     */
    private String dimension;
    /**
     * 状态
     */
    private String eventResult;
    /**
     * 时间段
     */
    private String period;
    /**
     * 是否只查询风险源
     */
    private List<String> typeNames;

    public DateTimeSlotStrVO getPeriodTiem() {
        DateTimeSlotStrVO dateTimeSlot = new DateTimeSlotStrVO();
        if (StringUtils.isNotEmpty(period)) {
            String[] split = period.split(",");
            dateTimeSlot.setStartTime(split[0]);
            dateTimeSlot.setEndTime(split[1]);
        }
        return dateTimeSlot;
    }

    public String getDimensiondefault() {
        String value = this.dimension;
        if (ObjectUtils.isNotEmpty(value)) {
            return value;
        }
        return TODAY;
    }
}
