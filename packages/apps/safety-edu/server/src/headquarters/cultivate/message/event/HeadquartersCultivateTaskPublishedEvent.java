package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.event;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.message.model.HeadquartersCultivateTaskMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 培训任务发布事件
 *
 * @author lanchun
 * @date 2024/4/10 15:22
 */
@Getter
public class HeadquartersCultivateTaskPublishedEvent extends ApplicationEvent {

    private final HeadquartersCultivateTaskMessage message;

    public HeadquartersCultivateTaskPublishedEvent(Object source, HeadquartersCultivateTaskMessage message) {
        super(source);
        this.message = message;
    }

}
