package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.event;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 用户培训完成事件
 *
 * @author appet
 * @date 2023/4/20 16:33
 */
@Getter
public class UserCultivateOverEvent extends ApplicationEvent {

    private CultivateTask cultivateTask;

    private final List<CultivateTaskUser> taskUserList;

    public UserCultivateOverEvent(Object source, CultivateTask cultivateTask, List<CultivateTaskUser> taskUserList) {
        super(source);
        this.cultivateTask = cultivateTask;
        this.taskUserList = taskUserList;
    }
}
