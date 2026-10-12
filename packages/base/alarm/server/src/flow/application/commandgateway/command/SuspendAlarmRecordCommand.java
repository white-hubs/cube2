package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报警挂起操作参数
 *
 * @author lanchun
 * @date 2022/10/25 14:41
 */
@Data
public class SuspendAlarmRecordCommand {

    /**
     * 预计处理时间
     */
    private LocalDateTime estimatedProcessTime;

    /**
     * 挂起原因
     */
    private String remark;

    /**
     * 附件集合
     */
    private List<FileVO> fileVOS;
}
