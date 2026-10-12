package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.event;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.aggregates.HeadquartersExamTask;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 考试任务发布事件
 *
 * @author lanchun
 * @date 2023/4/19 20:05
 */
@Getter
public class HeadquartersExamTaskPublishedEvent extends ApplicationEvent {

    private final HeadquartersExamTask examTask;

    public HeadquartersExamTaskPublishedEvent(Object source, HeadquartersExamTask examTask) {
        super(source);
        this.examTask = examTask;
    }

}
