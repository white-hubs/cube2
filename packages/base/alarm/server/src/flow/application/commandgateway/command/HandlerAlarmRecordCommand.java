package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.reason.HandlerAlarmReasonCommand;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.valueobject.AlarmHandlerUser;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 报警记录处理参数
 *
 * @author lanchun
 * @date 2022/6/7 14:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HandlerAlarmRecordCommand {

    /**
     * 确认结果
     */
    @NotNull(message = "确认结果不能为空")
    private AlarmHandlerResultEnum alarmHandlerResult;

    /**
     * 确认结果名称
     */
    private String alarmHandlerResultName;

    /**
     * 报警确认描述
     */
    @Size(max = 200, message = "描述信息字数不能大于200")
    private String alarmHandlerResultRemark;

    /**
     * 报警原因
     */
    private HandlerAlarmReasonCommand alarmReason;

    /**
     * 处理附件
     */
    private List<FileVO> fileVOS;

    /**
     * 报警处理人员
     */
    private AlarmHandlerUser handlerUser;
}
