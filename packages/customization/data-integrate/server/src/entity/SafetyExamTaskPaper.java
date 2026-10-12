package com.guhecloud.cube.base.dataintegrate.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SafetyExamTaskPaper {

    private Long id;

    /**
     * 考试任务id
     */
    private Long examTaskId;

    /**
     * 试卷id
     */
    private Long examPaperId;

    /**
     * 试卷名称
     */
    private String examPaperName;
}
