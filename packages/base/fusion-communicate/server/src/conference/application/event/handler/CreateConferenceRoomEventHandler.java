package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.event.handler;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.event.CreateConferenceRoomEvent;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.strategy.FusionCommunicateStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateConferenceRoomEventHandler {

    private final FusionCommunicateStrategy fusionCommunicateStrategy;

    public CreateConferenceRoomEventHandler(FusionCommunicateStrategy fusionCommunicateStrategy
                                           ) {this.fusionCommunicateStrategy = fusionCommunicateStrategy;}

    @EventListener
    public void createConferenceRoomEvent(final CreateConferenceRoomEvent event) {
        String roomNumber = this.fusionCommunicateStrategy.getFusionCommunicateRoom().createRoom();
        event.accept(roomNumber);
    }
}
