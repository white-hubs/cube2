package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SuspendVO {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 预计处理时间
     */
    private Long estimatedProcessTime;
    /**
     * 挂起原因
     */
    private String remark;
    /**
     * 图片
     */
    private List<FileVO> fileVOS;
    /**
     * 挂起时间
     */
    private LocalDateTime createTime;
    /**
     * 挂起人
     */
    private String createUserName;
}
