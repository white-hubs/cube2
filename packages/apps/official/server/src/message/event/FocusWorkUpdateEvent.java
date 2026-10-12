package com.guhecloud.cube.srv.gen.official.management.server.message.event;

import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkUpdatePO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 督办事项更新审批通过事件
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class FocusWorkUpdateEvent extends ApplicationEvent {

    private FocusWorkUpdatePO focusWork;

    public FocusWorkUpdateEvent(Object source, FocusWorkUpdatePO focusWork) {
        super(source);
        this.focusWork = focusWork;
    }

}
