package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.factory;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.commandgateway.command.ProposeConferenceCommand;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.Conference;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.ConferenceUserTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.valueobject.Room;
import com.guhecloud.platform.base.util.DateUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 会议房间工厂
 *
 * @author lanchun
 * @date 2025/2/12 10:19
 */
public class ConferenceRoomFactory {

    public static ConferenceRoom createConferenceRoom(ProposeConferenceCommand command) {
        Conference conference = new Conference(
                command.getCode(),
                command.getName(),
                command.getConferenceType(),
                command.getMediaType(),
                command.getCallingType(),
                null
        );
        Room room = new Room(command.getRoomNumber());
        Set<ConferenceInviteUser> conferenceInviteUsers = new HashSet<>(command.getConferenceUsers().size());
        Set<ConferenceUser> conferenceUsers = new HashSet<>(command.getConferenceUsers().size() + 1);
        //新增房主信息
        conferenceUsers.add(new ConferenceUser(command.getProposeUserId(), ConferenceUserTypeEnum.HOMEOWNER));
        command.getConferenceUsers().forEach(conferenceUser -> {
            conferenceInviteUsers.add(new ConferenceInviteUser(IdWorker.getId(), conferenceUser, DateUtils.now()));
        });
        return new ConferenceRoom(IdWorker.getId(),
                                  conference,
                                  room,
                                  command.getProposeUserId(),
                                  DateUtils.now(),
                                  conferenceUsers,
                                  conferenceInviteUsers);
    }
}
