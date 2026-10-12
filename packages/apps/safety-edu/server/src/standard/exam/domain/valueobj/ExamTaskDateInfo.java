package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 任务考试时间信息
 *
 * @author lanchun
 * @date 2023/4/19 15:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskDateInfo {

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
