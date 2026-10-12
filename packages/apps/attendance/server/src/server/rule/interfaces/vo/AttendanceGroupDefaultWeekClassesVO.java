package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组一周的默认班次
 */
@Data
public class AttendanceGroupDefaultWeekClassesVO {

    private String name;

    /**
     * 时间（周几）
     */
    private Integer week;

    /**
     * 班次信息
     */
    private List<Classes> classes;

}