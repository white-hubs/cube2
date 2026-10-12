package com.guhecloud.cube.srv.gen.official.management.server.message.event;

import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkReplyPO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 督办事项反馈事件
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class FocusWorkReplyEvent extends ApplicationEvent {

    private FocusWorkReplyPO focusWorkReply;
    private FocusWorkPO focusWork;

    public FocusWorkReplyEvent(Object source, FocusWorkPO focusWork, FocusWorkReplyPO focusWorkReply) {
        super(source);
        this.focusWorkReply = focusWorkReply;
        this.focusWork = focusWork;
    }

}
