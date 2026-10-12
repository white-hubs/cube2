package com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项反馈
 */
@Data
public class FocusWorkReplyVO {

    private String applicantName;

    private String applicantDeptName;

    private LocalDateTime applyTime;

    private LocalDateTime applyDoneTime;

    private Long focusWorkId;//督办事项ID

    private float percentage;//进度百分比

    private String nextWorkDesc;//下一步工作描述

    private String undoneReason;//未完成原因

    private String assistProblem;//需要协同的问题
    /**
     * 落实情况
     */
    private String practicableDetails;

}