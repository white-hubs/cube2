package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.model.CultivateTaskMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 培训任务发布事件
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class CultivateTaskPublishedEvent extends ApplicationEvent {

    private final CultivateTaskMessage message;

    public CultivateTaskPublishedEvent(Object source, CultivateTaskMessage message) {
        super(source);
        this.message = message;
    }

}
