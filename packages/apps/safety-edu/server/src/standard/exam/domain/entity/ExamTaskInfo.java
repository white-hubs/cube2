package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 任务基本信息
 *
 * @author lanchun
 * @date 2023/4/19 15:40
 */
@Getter
@NoArgsConstructor
public class ExamTaskInfo {

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务描述
     */
    private String remark;

    public ExamTaskInfo(String name, String remark) {
        this.name = name;
        this.remark = remark;
    }
}
