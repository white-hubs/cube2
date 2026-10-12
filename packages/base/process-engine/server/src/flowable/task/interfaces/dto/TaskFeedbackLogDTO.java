package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/11 13:35
 * @Description:
 */

@Data
public class TaskFeedbackLogDTO {

    /**
     * 流程id
     */
    private String processId;

    /**
     * 任务节点编码
     */
    private String taskNodeKey;

    /**
     * 反馈信息
     */
    private String feedbackInfo;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 反馈人id
     */
    private Long feedbackUserId;

    /**
     * 反馈人
     */
    private String feedbackUser;

    /**
     * 反馈时间
     */
    private LocalDateTime feedbackTime;

    /**
     * 照片集合
     */

    private List<FileVO> fileVOList;
}
