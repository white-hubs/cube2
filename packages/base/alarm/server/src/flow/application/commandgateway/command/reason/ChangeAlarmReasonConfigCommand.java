package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ChangeAlarmReasonConfigCommand {

    private Long id;

    /**
     * 原因
     */
    @NotBlank(message = "报警原因不能为空")
    @Size(max = 100, message = "报警原因不能大于100")
    private String reason;

    /**
     * 原因备注
     */
    @Size(max = 300, message = "原因备注不能大于300")
    private String remark;
}
