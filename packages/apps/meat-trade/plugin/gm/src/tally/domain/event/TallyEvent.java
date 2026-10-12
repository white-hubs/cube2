package com.guhecloud.cube.gm.meattrade.plugin.tally.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TallyEvent extends ApplicationEvent {

    private final String tallyContent;

    public TallyEvent(Object source, String tallyContent) {
        super(source);
        this.tallyContent = tallyContent;
    }
}
