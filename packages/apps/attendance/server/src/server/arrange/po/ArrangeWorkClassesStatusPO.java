package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/30
 * @description 排班班次状态
 */
@Data
@TableName("arrange_work_classes_status")
public class ArrangeWorkClassesStatusPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 排班ID
     */
    private Long workId;
    /**
     * 具体班次ID
     */
    private Long classesId;

    /**
     * 班次状态
     */
    private String classesStatus;
    /**
     * 触发动作：触发动作：CLOCK(打卡) OVERTIME（加班）
     * LEAVE（请假） REPAIR_CLOCK（补卡） CHANGE_CLASSES（换班）
     */
    @TableField("`trigger`")
    private String trigger;

}