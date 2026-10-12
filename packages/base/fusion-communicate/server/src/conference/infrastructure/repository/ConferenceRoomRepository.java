package com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.aggregates.ConferenceRoom;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.domain.po.ConferenceRoomPO;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.convert.ConferenceConvertUtils;
import com.guhecloud.cube.biz.fusion.communicate.server.conference.infrastructure.repository.mapper.ConferenceRoomMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceRoomRepository {

    private final ConferenceRoomMapper conferenceRoomMapper;

    private final ConferenceRoomUserRepository conferenceRoomUserRepository;

    private final ConferenceInviteRecordRepository conferenceInviteRecordRepository;

    /**
     * 会议室信息保存
     *
     * @param conferenceRoom 会议室对象
     *
     * @return ConferenceRoom
     */
    @Transactional(rollbackFor = Exception.class)
    public ConferenceRoom save(final ConferenceRoom conferenceRoom) {
        ConferenceRoomPO conferenceRoomPO = this.conferenceRoomMapper.selectById(conferenceRoom.getId());
        boolean updated = Objects.nonNull(conferenceRoomPO);
        ConferenceRoomPO saveConferenceRoom = ConferenceConvertUtils.convertConferenceRoomTOPO(conferenceRoom);
        //新增邀请记录
        conferenceInviteRecordRepository.saveConferenceInviteRecord(saveConferenceRoom.getId(), saveConferenceRoom.getNumber(), conferenceRoom.getConferenceInviteUsers());
        //新增会议用户信息
        conferenceRoomUserRepository.saveConferenceUser(saveConferenceRoom.getId(), saveConferenceRoom.getNumber(), conferenceRoom
                .getConferenceUsers().stream()
                .findFirst().get());
        if (updated) {
            EntityAuditor.populateCreateAudit(saveConferenceRoom, conferenceRoomPO.getCreateBy(), conferenceRoomPO.getCreatorId(),
                                              conferenceRoomPO.getCreatorType(), conferenceRoomPO.getCreateTime());
            this.conferenceRoomMapper.updateById(saveConferenceRoom);
        } else {
            this.conferenceRoomMapper.insert(saveConferenceRoom);
        }
        return conferenceRoom;
    }

    /**
     * 修改会议房间基本信息
     *
     * @param conferenceRoom
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ConferenceRoom updateBaseInfo(final ConferenceRoom conferenceRoom) {
        ConferenceRoomPO conferenceRoomPO = this.conferenceRoomMapper.selectById(conferenceRoom.getId());
        boolean updated = Objects.nonNull(conferenceRoomPO);
        ConferenceRoomPO saveConferenceRoom = ConferenceConvertUtils.convertConferenceRoomTOPO(conferenceRoom);
        if (updated) {
            EntityAuditor.populateCreateAudit(saveConferenceRoom, conferenceRoomPO.getCreateBy(), conferenceRoomPO.getCreatorId(),
                                              conferenceRoomPO.getCreatorType(), conferenceRoomPO.getCreateTime());
            this.conferenceRoomMapper.updateById(saveConferenceRoom);
        } else {
            this.conferenceRoomMapper.insert(saveConferenceRoom);
        }
        return conferenceRoom;
    }

    /**
     * 获取会议房间信息
     *
     * @param roomNumber 房间号
     *
     * @return ConferenceRoom
     */
    public Optional<ConferenceRoom> getConferenceRoom(final String roomNumber) {
        List<ConferenceRoomPO> roomPOS = conferenceRoomMapper.selectList(Wrappers.<ConferenceRoomPO>lambdaQuery()
                                                                                 .eq(ConferenceRoomPO::getNumber, roomNumber)
                                                                                 .isNull(ConferenceRoomPO::getClosingTime)
                                                                        );
        if (CollectionUtils.isEmpty(roomPOS)) {
            return Optional.empty();
        }
        ConferenceRoomPO conferenceRoomPO = roomPOS.get(0);
        ConferenceRoom conferenceRoom = ConferenceConvertUtils.convertPOTOConferenceRoom(conferenceRoomPO);
        conferenceRoom.setConferenceUsers(new HashSet<>(conferenceRoomUserRepository.
                                                                getConferenceRoomUsers(conferenceRoomPO.getId(), conferenceRoomPO.getNumber())));
        conferenceRoom.setConferenceInviteUsers(new HashSet<>(conferenceInviteRecordRepository.
                                                                      getConferenceInviteUser(conferenceRoomPO.getId(), conferenceRoomPO.getNumber())));
        return Optional.of(conferenceRoom);
    }
}
