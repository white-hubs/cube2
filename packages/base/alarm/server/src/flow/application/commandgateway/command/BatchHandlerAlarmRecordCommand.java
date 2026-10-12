package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 报警批量处理参数
 *
 * @author lanchun
 * @date 2022/6/7 15:56
 */
@Data
public class BatchHandlerAlarmRecordCommand {

    /**
     * 记录主键
     */
    @NotNull(message = "记录主键不能为空")
    @Size(min = 1, message = "记录主键不能小于1")
    private List<Long> ids;

    /**
     * 确认结果
     */
    @NotNull(message = "确认结果不能为空")
    private AlarmHandlerResultEnum alarmConfirmedResult;

    /**
     * 报警确认描述
     */
    @Size(max = 200, message = "描述信息字数不能大于200")
    private String alarmConfirmedResultRemark;

    /**
     * 报警处理人员
     */
    private AlarmHandlerUser handlerUser;
}
