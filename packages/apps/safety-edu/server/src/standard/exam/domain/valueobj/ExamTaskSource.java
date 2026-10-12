package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务来源信息
 *
 * @author lanchun
 * @date 2024/4/9 20:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskSource {

    /**
     * 任务来源编号
     */
    private String sourceCode;

    /**
     * 任务来源名称
     */
    private String sourceName;

    /**
     * 任务来源键
     */
    private String sourceKey;
}
