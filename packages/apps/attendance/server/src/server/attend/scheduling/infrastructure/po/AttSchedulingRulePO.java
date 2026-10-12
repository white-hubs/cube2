package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@TableName("att_scheduling_rule")
@EqualsAndHashCode(callSuper = true)
public class AttSchedulingRulePO extends BasicEnhancedTenantEntity<Long> {
    @DiffLogField(name = "规则id")
    private Long id;
    /**
     * 规则名称
     */
    @DiffLogField(name = "名称")
    private String name;
    /**
     * 排班方式(固定班，三班倒)
     */
    @DiffLogField(name = "排班方式")
    private String ways;
    /**
     * 法定节假日是否休息
     */
    @DiffLogField(name = "法定节假日是否休息", function = "BOOLEAN")
    private Boolean legalHolidaysRest;
    /**
     * 一个月允许最大连续排班天数
     */
    @DiffLogField(name = "最大连续排班天数")
    private int continuousDays;
    /**
     * 一天两个班次最小间隔时间
     */
    @DiffLogField(name = "最小间隔时间")
    private int intervalHour;
    /**
     * 一个月相同班次数据json
     */
    @DiffLogField(name = "一个月相同班次")
    private String sameShift;
    //    /**
    //     * 一个月相同班次数量
    //     */
    //    private int sameShiftNumber;
    //    /**
    //     * 一个月相同班次类型
    //     */
    //    private String sameShiftType;
}
