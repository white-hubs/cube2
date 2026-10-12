package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity;

import com.alibaba.fastjson2.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.LeaveTimeConvert;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.NewEmployeeLeaveRule;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/11/21 10:43
 * @description 请假规则
 * <p>
 * 本类的setter方法主要是为了方便框架做转换，不提供给用户调用
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LeaveRule {
    private Long id;

    /**
     * 最小请假时长
     */
    private MinimumLeaveTimeUnitConvert minimumLeaveTimeUnitConvert;

    /**
     * 请假时长核算-默认1:1,依赖于最小请假单位
     */
    private LeaveTimeConvert leaveTimeConvert;

    /**
     * 新员工请假限制
     */
    private NewEmployeeLeaveRule newEmployeeLeaveRule;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    public static class MinimumLeaveTimeUnitConvert {
        /**
         * 最小请假时长
         */
        @NotNull
        private MinimumLeaveTimeUnit minimumLeaveTimeUnit;

        private List<LeaveTimeUnitConvert> leaveTimeUnitConverts;
    }

    /**
     * 请假时间单位换算定义
     */
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @JSONType(seeAlso = { HalfDayConvert.class, DayConvert.class })
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type",
            visible = true
    )
    @JsonSubTypes(
            {
                    @JsonSubTypes.Type(value = HalfDayConvert.class, name = "HALF_DAY"),
                    @JsonSubTypes.Type(value = DayConvert.class, name = "DAY"),
            }
    )
    public static class LeaveTimeUnitConvert {
        protected String type;
    }

    @Getter
    @Setter
    @JSONType(typeName = "HALF_DAY")
    @NoArgsConstructor
    public static class HalfDayConvert extends LeaveTimeUnitConvert {
        private Float amHour;
        private Float pmHour;

        public HalfDayConvert(String type, Float amHour, Float pmHour) {
            super(type);
            this.amHour = amHour;
            this.pmHour = pmHour;
        }
    }

    @Getter
    @NoArgsConstructor
    @Setter
    @JSONType(typeName = "DAY")
    public static class DayConvert extends LeaveTimeUnitConvert {
        private Float dayHour;

        public DayConvert(String type, Float dayHour) {
            super(type);
            this.dayHour = dayHour;
        }
    }

    public enum MinimumLeaveTimeUnit {
        /**
         * 一整天
         */
        DAY("天"),
        /**
         * 半天
         */
        HALF_DAY("半天"),
        /**
         * 按小时
         */
        HOUR("小时");
        @Getter
        private final String value;

        MinimumLeaveTimeUnit(String value) {this.value = value;}
    }
}
