package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.application.ConferenceInviteRecordService;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceInviteUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po.ConferenceInviteRecordPO;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.convert.ConferenceConvertUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceInviteRecordRepository {

    private final ConferenceInviteRecordService conferenceInviteRecordService;

    /**
     * 会议邀请用户记录保存
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     * @param inviteUsers          邀请用户集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveConferenceInviteRecord(final Long conferenceRoomId,
                                           final String conferenceRoomNumber,
                                           final Set<ConferenceInviteUser> inviteUsers
                                          ) {
        List<ConferenceInviteRecordPO> inviteRecordPOS = ConferenceConvertUtils.convertConferenceInviteUserToPO(conferenceRoomId, conferenceRoomNumber, inviteUsers);
        if (!CollectionUtils.isEmpty(inviteRecordPOS)) {
            conferenceInviteRecordService.saveBatch(inviteRecordPOS);
        }
    }

    /**
     * 获取会议邀请用户记录信息
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     *
     * @return ConferenceInviteUser
     */
    public List<ConferenceInviteUser> getConferenceInviteUser(final Long conferenceRoomId,
                                                              final String conferenceRoomNumber
                                                             ) {
        List<ConferenceInviteRecordPO> inviteRecordPOS = conferenceInviteRecordService
                .list(Wrappers.<ConferenceInviteRecordPO>lambdaQuery()
                              .eq(ConferenceInviteRecordPO::getConferenceRoomId, conferenceRoomId)
                              .eq(ConferenceInviteRecordPO::getConferenceRoomNumber, conferenceRoomNumber));
        if (CollectionUtils.isEmpty(inviteRecordPOS)) {
            return Collections.emptyList();
        }
        return ConferenceConvertUtils.convertPOTOConferenceInviteUser(inviteRecordPOS);
    }
}
