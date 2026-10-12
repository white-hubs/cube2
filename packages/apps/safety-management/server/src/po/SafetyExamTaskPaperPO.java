package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName(value = "safety_exam_task_paper")
public class SafetyExamTaskPaperPO {

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
