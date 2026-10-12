package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.validator;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.ProposeConferenceCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository.ConferenceRoomRepository;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ProposeConferenceRoomValidator {

    private final ConferenceRoomRepository conferenceRoomRepository;

    public ProposeConferenceRoomValidator(ConferenceRoomRepository conferenceRoomRepository
                                         ) {this.conferenceRoomRepository = conferenceRoomRepository;}

    public void validate(ProposeConferenceCommand command) {
        Optional<ConferenceRoom> conferenceRoom = conferenceRoomRepository.getConferenceRoom(command.getRoomNumber());
        Assert.isConflict(conferenceRoom.isPresent(), "房间号已存在");
    }
}
