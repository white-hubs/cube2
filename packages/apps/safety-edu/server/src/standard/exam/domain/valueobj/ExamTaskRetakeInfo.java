package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试任务复考信息
 *
 * @author lanchun
 * @date 2023/4/19 15:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskRetakeInfo {

    /**
     * 是否支持复考
     */
    private Boolean retake;

    /**
     * 复考次数
     */
    private Integer retakeNumber;
}
