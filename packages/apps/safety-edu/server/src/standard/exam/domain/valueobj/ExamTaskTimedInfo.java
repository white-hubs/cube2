package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试任务限时信息
 *
 * @author lanchun
 * @date 2023/4/19 15:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskTimedInfo {

    /**
     * 是否限时
     */
    private Boolean timed;

    /**
     * 时长
     */
    private Integer examDuration;

    /**
     * 时长单位
     */
    private String examDurationUnit;
}
