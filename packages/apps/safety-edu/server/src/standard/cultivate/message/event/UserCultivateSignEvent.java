package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.model.CultivateSignMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户培训完成事件
 *
 * @author lanchun
 * @date 2023/4/20 16:33
 */
@Getter
public class UserCultivateSignEvent extends ApplicationEvent {

    private final CultivateSignMessage message;

    public UserCultivateSignEvent(Object source, CultivateSignMessage message) {
        super(source);
        this.message = message;
    }
}
