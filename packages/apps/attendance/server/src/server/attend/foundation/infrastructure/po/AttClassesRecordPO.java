package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 考勤班次数据库对象
 * 参考流引擎每次修改都是新增班次，在使用中的班次不受影响
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("att_classes_record")
@EqualsAndHashCode(callSuper = true)
public class AttClassesRecordPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 班次id
     */
    private Long classesId;
    /**
     * 名称
     */
    private String name;

    /**
     * 别称
     */
    private String nickName;

    /**
     * 上班时间
     */
    private String startWorkTime;

    /**
     * 上班打卡开始时间
     */
    private String startClockBeforeTime;

    /**
     * 上班打卡结束时间
     */
    private String startClockAfterTime;

    /**
     * 下班时间
     */
    private String endWorkTime;

    /**
     * 下班打卡开始时间
     */
    private String endClockBeforeTime;

    /**
     * 下班打卡结束时间
     */
    private String endClockAfterTime;

    /**
     * 休息开始时间
     */
    private String restStartTime;

    /**
     * 休息结束时间
     */
    private String restEndTime;

    /**
     * 班次类型
     * 枚举AttClassesTypeEnum，（如果有需求可拓展成班次类型模块）
     */
    private String classesType;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    /**
     * 建议应用范围(部门id)
     */
    private String deptIds;
    /**
     * 修改记录版本号
     */
    private Integer version = 1;
}
