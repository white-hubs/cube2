package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.event;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 考试任务结束事件
 *
 * @author lanchun
 * @date 2024/1/11 20:04
 */
@Getter
public class ExamTaskEndEvent extends ApplicationEvent {

    private final Long examTaskId;

    private final List<ExamTaskUserPO> examTaskUserPOS;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ExamTaskEndEvent(Object source, Long examTaskId, List<ExamTaskUserPO> examTaskUserPOS) {
        super(source);
        this.examTaskId = examTaskId;
        this.examTaskUserPOS = examTaskUserPOS;
    }
}
