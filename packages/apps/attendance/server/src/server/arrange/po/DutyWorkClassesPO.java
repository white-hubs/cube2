package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/16
 * @description 值班班次
 */
@Data
@TableName("duty_work_classes")
public class DutyWorkClassesPO extends Classes {

    /**
     * 上班时间前
     */
    private String beforeStartWorkTime;

    /**
     * 上班时间后
     */
    private String afterStartWorkTime;

    /**
     * 下班时间前
     */
    private String beforeEndWorkTime;

    /**
     * 下班时间后
     */
    private String afterEndWorkTime;

}