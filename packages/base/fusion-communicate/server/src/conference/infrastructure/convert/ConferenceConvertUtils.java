package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.Conference;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.CallingTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.ConferenceUserTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.DepartureTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.enumeration.MediaTypeEnum;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po.ConferenceInviteRecordPO;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po.ConferenceRoomPO;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po.ConferenceRoomUserPO;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.valueobject.Room;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 会议室转换工具类
 *
 * @author lanchun
 * @date 2025/2/12 14:48
 */
public class ConferenceConvertUtils {

    private ConferenceConvertUtils() {

    }

    /**
     * 会议室聚合对象转换为数据实体
     *
     * @param conferenceRoom
     *
     * @return
     */
    public static ConferenceRoomPO convertConferenceRoomTOPO(final ConferenceRoom conferenceRoom) {
        ConferenceRoomPO saveConferenceRoom = new ConferenceRoomPO();
        saveConferenceRoom.setId(conferenceRoom.getId());
        saveConferenceRoom.setCode(conferenceRoom.getConference().getCode());
        saveConferenceRoom.setNumber(conferenceRoom.getRoom().getNumber());
        saveConferenceRoom.setName(conferenceRoom.getConference().getName());
        saveConferenceRoom.setProposeUserId(conferenceRoom.getProposeUserId());
        saveConferenceRoom.setProposeTime(conferenceRoom.getProposeTime());
        saveConferenceRoom.setConferenceType(conferenceRoom.getConference().getConferenceType());
        saveConferenceRoom.setCallingType(conferenceRoom.getConference().getCallingType().name());
        saveConferenceRoom.setMediaType(conferenceRoom.getConference().getMediaType().name());
        saveConferenceRoom.setClosingTime(conferenceRoom.getConference().getClosingTime());
        saveConferenceRoom.setTenant(SessionContextHolder.getRequester().getTenant());
        saveConferenceRoom.setDeleted(false);
        EntityAuditor.populateAudit(saveConferenceRoom);
        return saveConferenceRoom;
    }

    /**
     * 会议房间数据实体转换为业务实体
     *
     * @param conferenceRoomPO 会议房间数据实体
     *
     * @return ConferenceRoom
     */
    public static ConferenceRoom convertPOTOConferenceRoom(final ConferenceRoomPO conferenceRoomPO) {
        Conference conference = new Conference(conferenceRoomPO.getCode(),
                                               conferenceRoomPO.getName(),
                                               conferenceRoomPO.getConferenceType(),
                                               MediaTypeEnum.valueOf(conferenceRoomPO.getMediaType()),
                                               CallingTypeEnum.valueOf(conferenceRoomPO.getCallingType()),
                                               conferenceRoomPO.getClosingTime()
        );
        return new ConferenceRoom(conferenceRoomPO.getId(),
                                  conference,
                                  new Room(conferenceRoomPO.getNumber()),
                                  conferenceRoomPO.getProposeUserId(),
                                  conferenceRoomPO.getProposeTime(),
                                  Collections.emptySet(),
                                  Collections.emptySet());
    }

    /**
     * 会议室邀请用户信息对象转换为数据实体
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     * @param inviteUsers          邀请用户集合
     *
     * @return List<ConferenceInviteRecordPO>
     */
    public static List<ConferenceInviteRecordPO> convertConferenceInviteUserToPO(final Long conferenceRoomId,
                                                                                 final String conferenceRoomNumber,
                                                                                 final Collection<ConferenceInviteUser> inviteUsers
                                                                                ) {
        return inviteUsers.stream()
                .map(inviteUser -> convertConferenceInviteUserToPO(conferenceRoomId, conferenceRoomNumber, inviteUser))
                .collect(Collectors.toList());
    }

    /**
     * 会议室邀请实体转换为数据实体
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     * @param inviteUser           邀请用户
     *
     * @return ConferenceInviteRecordPO
     */
    public static ConferenceInviteRecordPO convertConferenceInviteUserToPO(final Long conferenceRoomId,
                                                                           final String conferenceRoomNumber,
                                                                           final ConferenceInviteUser inviteUser
                                                                          ) {
        return ConferenceInviteRecordPO.builder()
                .id(inviteUser.getId())
                .conferenceRoomId(conferenceRoomId)
                .conferenceRoomNumber(conferenceRoomNumber)
                .userId(inviteUser.getUserId())
                .inviteTime(inviteUser.getInviteTime())
                .tenant(SessionContextHolder.getRequester().getTenant())
                .deleted(false)
                .createBy(SecurityContextHolder.getPrincipal().getAccount())
                .createTime(DateUtils.now())
                .updateBy(SecurityContextHolder.getPrincipal().getAccount())
                .updateTime(DateUtils.now())
                .build();
    }

    /**
     * 会议用户实体转换为数据实体
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     * @param users                加入用户
     *
     * @return ConferenceRoomUserPO
     */
    public static List<ConferenceRoomUserPO> convertConferenceUserTOPO(final Long conferenceRoomId,
                                                                       final String conferenceRoomNumber,
                                                                       final Collection<ConferenceUser> users
                                                                      ) {
        return users.stream()
                .map(user -> convertConferenceUserTOPO(conferenceRoomId, conferenceRoomNumber, user))
                .collect(Collectors.toList());
    }

    /**
     * 会议用户实体转换为数据实体
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     * @param user                 加入用户
     *
     * @return ConferenceRoomUserPO
     */
    public static ConferenceRoomUserPO convertConferenceUserTOPO(final Long conferenceRoomId,
                                                                 final String conferenceRoomNumber,
                                                                 final ConferenceUser user
                                                                ) {
        return ConferenceRoomUserPO.builder()
                .id(IdWorker.getId())
                .conferenceRoomId(conferenceRoomId)
                .conferenceRoomNumber(conferenceRoomNumber)
                .userId(user.getId())
                .conferenceUserType(user.getConferenceUserType().name())
                .membershipTime(user.getMembershipTime())
                .departureTime(user.getDepartureTime())
                .departureType(Objects.nonNull(user.getDepartureType()) ? user.getDepartureType().name() : null)
                .tenant(SessionContextHolder.getRequester().getTenant())
                .deleted(false)
                .createBy(SecurityContextHolder.getPrincipal().getAccount())
                .createTime(DateUtils.now())
                .updateBy(SecurityContextHolder.getPrincipal().getAccount())
                .updateTime(DateUtils.now())
                .build();
    }

    /**
     * 会议房间用户数据实体转换业务实体
     *
     * @param conferenceRoomUserPOS 会议房间用户
     *
     * @return List<ConferenceUser>
     */
    public static List<ConferenceUser> convertPOTOConferenceUser(Collection<ConferenceRoomUserPO> conferenceRoomUserPOS) {
        return conferenceRoomUserPOS.stream().map(ConferenceConvertUtils::convertPOTOConferenceUser).collect(Collectors.toList());
    }

    /**
     * 会议房间用户数据实体转换业务实体
     *
     * @param conferenceRoomUserPO 会议房间用户
     *
     * @return ConferenceUser
     */
    public static ConferenceUser convertPOTOConferenceUser(ConferenceRoomUserPO conferenceRoomUserPO) {
        ConferenceUser conferenceUser = new ConferenceUser(conferenceRoomUserPO.getUserId(),
                                                           ConferenceUserTypeEnum.valueOf(conferenceRoomUserPO.getConferenceUserType()));
        conferenceUser.setMembershipTime(conferenceRoomUserPO.getMembershipTime());
        conferenceUser.setDepartureTime(conferenceRoomUserPO.getDepartureTime());
        conferenceUser.setDepartureType(StringUtils.isNotBlank(conferenceRoomUserPO.getDepartureType())
                                        ? DepartureTypeEnum.valueOf(conferenceRoomUserPO.getDepartureType()) : null);
        return conferenceUser;
    }

    /**
     * 会议邀请记录数据实体转为业务实体
     *
     * @param conferenceInviteRecordPOS 会议邀请记录信息
     *
     * @return ConferenceInviteUser
     */
    public static List<ConferenceInviteUser> convertPOTOConferenceInviteUser(Collection<ConferenceInviteRecordPO> conferenceInviteRecordPOS
                                                                            ) {
        return conferenceInviteRecordPOS.stream().map(ConferenceConvertUtils::convertPOTOConferenceInviteUser).collect(Collectors.toList());
    }

    /**
     * 会议邀请记录数据实体转为业务实体
     *
     * @param conferenceInviteRecordPO 会议邀请记录信息
     *
     * @return ConferenceInviteUser
     */
    public static ConferenceInviteUser convertPOTOConferenceInviteUser(ConferenceInviteRecordPO conferenceInviteRecordPO) {
        return new ConferenceInviteUser(conferenceInviteRecordPO.getId(),
                                        conferenceInviteRecordPO.getUserId(),
                                        conferenceInviteRecordPO.getInviteTime());
    }
}
