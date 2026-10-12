package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试类型
 *
 * @author lanchun
 * @date 2023/4/19 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskType {

    /**
     * 考试类型枚举
     */
    private ExamTaskTypeEnum examTaskType;

    /**
     * 考试类型名称
     */
    private String examTaskTypeName;
}
