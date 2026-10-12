package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.WeekEnum;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组班次
 */
@Data
public class AttendanceGroupClassesVO implements Comparable<AttendanceGroupClassesVO> {

    private Long id;

    /**
     * 周几
     */
    private String name;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 时间（周几）
     */
    private Integer week;

    /**
     * 班次信息
     */
    private List<Classes> classes;

    @Override
    public int compareTo(AttendanceGroupClassesVO o) {  // 重写排序
        if (o.getWeek() == 1) {
            return -1;
        }
        if (this.week - o.getWeek() > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public String getName() {
        return WeekEnum.getWeekName(this.week).substring(1);
    }

}