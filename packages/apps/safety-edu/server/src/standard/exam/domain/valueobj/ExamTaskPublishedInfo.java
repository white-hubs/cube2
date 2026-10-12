package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 任务发布信息
 *
 * @author lanchun
 * @date 2023/4/20 11:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskPublishedInfo {

    /**
     * 发布时间
     */
    private LocalDateTime publishedTime;
}
