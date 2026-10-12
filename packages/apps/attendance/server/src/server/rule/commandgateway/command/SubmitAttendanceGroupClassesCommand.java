package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/15
 * @description 考勤组班次
 */
@Data
public class SubmitAttendanceGroupClassesCommand {

    private Long id;

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
}