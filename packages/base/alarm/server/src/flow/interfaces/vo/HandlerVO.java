package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HandlerVO {

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 报警类型
     */
    private String alarmHandlerResult;

    private String alarmHandlerResultName;
    /**
     * 处理说明
     */
    private String alarmHandlerResultRemark;
    /**
     * 处理结果附件列表
     */
    private List<FileVO> fileVOS;
    /**
     * 处理时间
     */
    private LocalDateTime createTime;
    /**
     * 处理人
     */
    private String createUserName;

    /**
     * 记录处理原因
     */
    private AlarmRecordHandlerReasonVO recordHandlerReason;
}
