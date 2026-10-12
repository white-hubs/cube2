package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 批量删除读数记录参数
 */
@Data
public class MeterReadLogDeleteParam {

    private List<DateRange> dateRangeList;

    @Data
    public static class DateRange {
        private LocalDateTime time;
        private Long deviceId;
    }
}
