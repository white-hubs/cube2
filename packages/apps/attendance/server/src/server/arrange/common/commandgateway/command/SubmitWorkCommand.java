package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupStaffCommand;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 * @description 提交考勤组数据-用于排班或值班
 */
@Data
public class SubmitWorkCommand {

    /**
     * 考勤类型（排班制、值班制）
     */
    private String type;

    /**
     * 适用范围(所有人、部分人)
     */
    private String ranges;

    /**
     * 班次类型（固定班制、排班制）
     */
    private String classesType;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 节假日自动排休
     */
    private Boolean autoRest;

    /**
     * 开始排班时间，不指定时为当前时间
     */
    private LocalDateTime beginDate;

    private List<SubmitAttendanceGroupStaffCommand> staffPOS;

    private List<SubmitAttendanceGroupClassesCommand> classesPOS;

}
