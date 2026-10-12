package com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query;

import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRecordStandardQueryParam {

    /**
     * 报警类型与级别参数
     */
    private List<AlarmRecordTypeLevelQuery> alarmRecordTypeLevels;

    /**
     * 时间不能为空
     */
    @NotNull(message = "时间不能为空")
    private Period period;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmRecordTypeLevelQuery {

        /**
         * 报警类型编号
         */
        private String alarmTypeCode;

        /**
         * 报警级别编号集合
         */
        private List<AlarmRecordTypeLevelDetailQuery> alarmLevelQuery;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmRecordTypeLevelDetailQuery {

        /**
         * 报警级别编号
         */
        private String alarmLevelCode;

        /**
         * 是否超时
         */
        private Boolean timeout;
    }
}
