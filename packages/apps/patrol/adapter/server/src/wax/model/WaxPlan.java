package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaxPlan {
    private Long id;//计划ID
    private String name;//计划名称
    private int planType;//计划类型： 3 每天
    private Long startDate;//开始日期(带毫秒的时间戳)
    private Long endDate;//结束日期(带毫秒的时间戳)
    private Boolean mon;//周一是否巡检
    private Boolean tue;//周二是否巡检
    private Boolean wed;//周三是否巡检
    private Boolean thu;//周四是否巡检
    private Boolean fri;//周五是否巡检
    private Boolean sat;//周六是否巡检
    private Boolean sun;// 周天是否巡检
    private Boolean fixedOrder;// 【这里不使用】
    private Boolean fixedTime;//固定时间(需要在线路点位中设置间隔时间)
    private int errorTime;//误差时间
    private Long patrolmanId;// 巡检员 ID
    private Long lineId;//线路 Id
    private String lineName;//线路名称
    private Object patrolman;//巡检员对象
    private List<Period> planTimes;//巡检时间列表

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Period {
        private Long startTime;
        private Long endTime;
    }
}
