package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/23
 * @description 提交手动排班/值班
 */
@Data
public class SubmitArrangeAndDutyWorkCommand {

    /**
     * 日期
     */
    @NotNull(message = "排班日期不能为空")
    private LocalDateTime date;

    /**
     * 员工ID
     */
    private Long staffId;

    /**
     * 班次信息
     */
    private List<Classes> classes;

}
