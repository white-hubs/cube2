package com.guhecloud.cube.biz.fusion.communicate.server.conference.application.querygateway;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository.ConferenceRoomRepository;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.interfaces.vo.ConferenceRoomVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConferenceRoomQueryService {

    private final ConferenceRoomRepository conferenceRoomRepository;

    public ConferenceRoomQueryService(ConferenceRoomRepository conferenceRoomRepository
                                     ) {this.conferenceRoomRepository = conferenceRoomRepository;}

    public ConferenceRoomVO getConferenceRoom(String roomNumber) {
        Optional<ConferenceRoom> conferenceRoomOptional = this.conferenceRoomRepository.getConferenceRoom(roomNumber);
        Assert.isNotFound(conferenceRoomOptional.isEmpty(), "房间信息不存在");
        ConferenceRoom room = conferenceRoomOptional.get();
        Set<Long> onlineUsers = room.getConferenceUsers().stream()
                .filter(user -> Objects.isNull(user.getDepartureTime()))
                .map(ConferenceUser::getId)
                .collect(Collectors.toSet());
        return ConferenceRoomVO.builder()
                .id(room.getId())
                .roomNumber(room.getRoom().getNumber())
                .proposeUserId(room.getProposeUserId())
                .proposeTime(room.getProposeTime())
                .mediaType(room.getConference().getMediaType().name())
                .callingType(room.getConference().getCallingType().name())
                .closingTime(room.getConference().getClosingTime())
                .members(onlineUsers)
                .build();
    }
}
