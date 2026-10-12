package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 报警处理原因命令
 *
 * @author lanchun
 * @date 2024/7/25 10:56
 */
@Data
public class HandlerAlarmReasonCommand {

    /**
     * 报警原因主键
     */
    private Long id;

    /**
     * 报警原因内容
     */
    @Size(max = 100, message = "报警原因不能大于100")
    private String reason;
}
