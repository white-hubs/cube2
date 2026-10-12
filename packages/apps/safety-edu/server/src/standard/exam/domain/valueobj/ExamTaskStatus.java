package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试任务状态
 *
 * @author lanchun
 * @date 2023/4/19 16:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskStatus {

    /**
     * 状态枚举
     */
    private ExamTaskStatusEnum examTaskStatus;

    /**
     * 状态名称
     */
    private String examTaskStatusName;
}
