package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.event;

import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.enumeration.AiReconfirmResultEnum;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AiReConfirmResultEvent extends ApplicationEvent {

    private final Long eventId;

    private final String eventSnapshot;

    private final AiReconfirmResultEnum reConfirmedResult;

    public AiReConfirmResultEvent(Object source, Long eventId, String eventSnapshot, AiReconfirmResultEnum reConfirmedResult) {
        super(source);
        this.eventId = eventId;
        this.eventSnapshot = eventSnapshot;
        this.reConfirmedResult = reConfirmedResult;
    }
}
