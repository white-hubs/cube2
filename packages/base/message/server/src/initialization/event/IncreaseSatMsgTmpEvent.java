package com.guhecloud.cube.base.message2.server.initialization.event;

import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class IncreaseSatMsgTmpEvent extends ApplicationEvent {

    private List<SatMsgTmp> satMsgTmpList;

    public IncreaseSatMsgTmpEvent(Object source, List<SatMsgTmp> satMsgTmpList) {
        super(source);
        this.satMsgTmpList = satMsgTmpList;
    }
}
