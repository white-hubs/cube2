package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.entity.ConferenceUser;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po.ConferenceRoomUserPO;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.convert.ConferenceConvertUtils;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository.mapper.ConferenceRoomUserMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
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
public class ConferenceRoomUserRepository {

    private final ConferenceRoomUserMapper conferenceRoomUserMapper;

    /**
     * 会议室用户信息保存
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     * @param conferenceUser       加入用户
     */
    @Transactional(rollbackFor = Exception.class)
    public Long saveConferenceUser(final Long conferenceRoomId,
                                   final String conferenceRoomNumber,
                                   final ConferenceUser conferenceUser
                                  ) {
        List<ConferenceRoomUserPO> conferenceRoomUserPOS = this.conferenceRoomUserMapper
                .selectList(Wrappers
                                    .<ConferenceRoomUserPO>lambdaQuery()
                                    .eq(ConferenceRoomUserPO::getConferenceRoomId, conferenceRoomId)
                                    .eq(ConferenceRoomUserPO::getConferenceRoomNumber, conferenceRoomNumber)
                                    .eq(ConferenceRoomUserPO::getUserId, conferenceUser.getId())
                           );
        ConferenceRoomUserPO saveConferenceRoomUser = ConferenceConvertUtils
                .convertConferenceUserTOPO(conferenceRoomId, conferenceRoomNumber, conferenceUser);
        boolean updated = !CollectionUtils.isEmpty(conferenceRoomUserPOS);
        if (updated) {
            ConferenceRoomUserPO conferenceRoomUserPO = conferenceRoomUserPOS.get(0);
            saveConferenceRoomUser.setId(conferenceRoomUserPO.getId());
            EntityAuditor.populateCreateAudit(saveConferenceRoomUser, conferenceRoomUserPO.getCreateBy(), conferenceRoomUserPO.getCreatorId(),
                                              conferenceRoomUserPO.getCreatorType(), conferenceRoomUserPO.getCreateTime());
            this.conferenceRoomUserMapper.updateById(saveConferenceRoomUser);
        } else {
            this.conferenceRoomUserMapper.insert(saveConferenceRoomUser);
        }
        return saveConferenceRoomUser.getId();
    }

    /**
     * 修改会议室用户信息
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     * @param conferenceUsers      修改的用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateConferenceUser(final Long conferenceRoomId,
                                     final String conferenceRoomNumber,
                                     final Set<ConferenceUser> conferenceUsers
                                    ) {
        for (ConferenceUser conferenceUser : conferenceUsers) {
            saveConferenceUser(conferenceRoomId, conferenceRoomNumber, conferenceUser);
        }
    }

    /**
     * 获取会议房间用户列表
     *
     * @param conferenceRoomId     会议房间id
     * @param conferenceRoomNumber 会议房间号
     *
     * @return List<ConferenceUser>
     */
    public List<ConferenceUser> getConferenceRoomUsers(final Long conferenceRoomId,
                                                       final String conferenceRoomNumber
                                                      ) {
        List<ConferenceRoomUserPO> conferenceRoomUserPOS = conferenceRoomUserMapper
                .selectList(Wrappers.<ConferenceRoomUserPO>lambdaQuery()
                                    .eq(ConferenceRoomUserPO::getConferenceRoomId, conferenceRoomId)
                                    .eq(ConferenceRoomUserPO::getConferenceRoomNumber, conferenceRoomNumber));
        if (CollectionUtils.isEmpty(conferenceRoomUserPOS)) {
            return Collections.emptyList();
        }
        return ConferenceConvertUtils.convertPOTOConferenceUser(conferenceRoomUserPOS);
    }
}
