package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lanchun
 * @date 2024/11/25 15:57
 */
@Data
public class AlarmRecordFollowUpVO {

    private Long id;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

    /**
     * 内容
     */
    private String content;

    /**
     * 跟进人主键
     */
    private Long followUpUserId;

    /**
     * 跟进人名称
     */
    private String followUpUserName;

    /**
     * 跟进时间
     */
    private LocalDateTime followUpTime;

    /**
     * 文件信息列表
     */
    private List<FileVO> files;
}
