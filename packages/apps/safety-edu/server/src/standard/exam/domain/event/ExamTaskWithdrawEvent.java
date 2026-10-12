package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates.ExamTask;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 考试任务撤回事件
 *
 * @author lanchun
 * @date 2023/4/19 20:22
 */
@Getter
public class ExamTaskWithdrawEvent extends ApplicationEvent {

    private final ExamTask examTask;

    public ExamTaskWithdrawEvent(Object source, ExamTask examTask) {
        super(source);
        this.examTask = examTask;
    }
}
