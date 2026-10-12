package com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates;

import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.Conference;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.DepartureTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.valueobject.Room;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 会议房间
 *
 * @author lanchun
 * @date 2025/2/11 17:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConferenceRoom {

    private Long id;

    /**
     * 会议信息
     */
    private Conference conference;

    /**
     * 房间信息
     */
    private Room room;

    /**
     * 会议发起人
     */
    private Long proposeUserId;

    /**
     * 发起时间
     */
    private LocalDateTime proposeTime;

    /**
     * 会议室用户
     */
    private Set<ConferenceUser> conferenceUsers;

    /**
     * 会议室邀请用户信息
     */
    private Set<ConferenceInviteUser> conferenceInviteUsers;

    public ConferenceUser getConferenceUserById(Long userId) {
        Optional<ConferenceUser> conferenceUser = this.conferenceUsers.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
        return conferenceUser.orElse(null);
    }

    public void settingUserDepartureType(DepartureTypeEnum departureType) {
        conferenceUsers.stream().filter(user -> Objects.isNull(user.getDepartureTime()))
                .forEach(user -> user.setDepartureType(departureType));
    }

    public void settingUserDepartureType(DepartureTypeEnum departureType, LocalDateTime date) {
        conferenceUsers.stream().filter(user -> Objects.isNull(user.getDepartureTime()))
                .forEach(user -> {
                    user.setDepartureType(departureType);
                    user.setDepartureTime(date);
                });
    }

    public void settingUserDepartureType(Collection<Long> userIds, DepartureTypeEnum departureType) {
        conferenceUsers.stream().filter(user -> userIds.contains(user.getId()))
                .forEach(user -> user.setDepartureType(departureType));
    }

    public void settingUserDepartureTypeAndTime(Collection<Long> userIds, DepartureTypeEnum departureType, LocalDateTime date) {
        conferenceUsers.stream().filter(user -> userIds.contains(user.getId()))
                .forEach(user -> {
                    user.setDepartureType(departureType);
                    user.setDepartureTime(date);
                });
    }

    public Set<ConferenceUser> getUsersByDepartureType(DepartureTypeEnum departureType) {
        return conferenceUsers.stream().filter(user -> departureType == user.getDepartureType()).collect(Collectors.toSet());
    }

    public Set<ConferenceUser> getUsersByDepartureType(Collection<Long> userIds, DepartureTypeEnum departureType) {
        return conferenceUsers.stream().filter(user -> userIds.contains(user.getId()) && departureType == user.getDepartureType())
                .collect(Collectors.toSet());
    }

    public void addConferenceUser(ConferenceUser user) {
        if (CollectionUtils.isEmpty(conferenceUsers)) {
            conferenceUsers = new HashSet<>();
        }
        conferenceUsers.add(user);
    }
}
