package com.guhecloud.cube.base.message2.server.initialization.event;

import com.guhecloud.cube.base.message2.server.base.vo.MsgTmpPageVO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class ChangeSatMsgTmpEvent extends ApplicationEvent {

    private final List<MsgTmpPageVO> msgTmpList;

    public ChangeSatMsgTmpEvent(Object source, List<MsgTmpPageVO> msgTmpList) {
        super(source);
        this.msgTmpList = msgTmpList;
    }
}
