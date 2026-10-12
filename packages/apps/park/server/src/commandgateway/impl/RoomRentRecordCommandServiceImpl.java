package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.EnterpriseCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.FileCommonApi;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.RoomCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.RoomRentRecordCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RentRecordRoomParam;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomRentRecordParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomRentRecordMapper;
import com.guhecloud.cube.srv.gen.park.management.server.enumeration.RoomRentRecordStatusEnum;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.EnterpriseVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.EnterpriseQueryService;
import com.guhecloud.cube.srv.gen.park.management.server.service.RoomRentRecordRoomService;
import com.guhecloud.cube.srv.gen.park.management.server.validator.RoomRentRecordValidator;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomRentRecordCommandServiceImpl extends ServiceImpl<RoomRentRecordMapper, RoomRentRecordPO> implements RoomRentRecordCommandService {

    private final RoomRentRecordValidator validator;
    private final RoomCommandService roomCommandService;
    private final EnterpriseCommandService enterpriseCommandService;
    private final RoomRentRecordRoomService rentRecordRoomService;
    private final EnterpriseQueryService enterpriseQueryService;
    private final FileCommonApi fileCommonApi;
    private final RoomRentRecordMapper roomRentRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoomRentRecordParam param) {

        List<Long> roomIds = param.getRentRooms().stream().map(RentRecordRoomParam::getRoomId).collect(Collectors.toList());

        validator.verifyIsExistByRoomIds(roomIds, param.getBeginTime(), param.getEndTime());

        RoomRentRecordPO po = ObjectUtils.copying(param, RoomRentRecordPO.class);

        po.setRoomIds(roomIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        po.setRoomNames(param.getRentRooms().stream().map(RentRecordRoomParam::getRoomName).collect(Collectors.joining(",")));
        po.setStatus(RoomRentRecordStatusEnum.EXECUTING);
        EnterpriseVO voById = enterpriseQueryService.getVoById(po.getEnterpriseId());
        po.setEnterpriseName(voById.getName());
        // 保存
        this.save(po);

        // 保存租赁记录与房间的关系
        rentRecordRoomService.saveOrUpdate(po.getId(), param.getRentRooms());

        // 更新房间租赁信息
        this.updateRoomRentInfo(po);

        // 更新企业有效租赁次数
        enterpriseCommandService.addRoomRentTime(po.getEnterpriseId());

        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, RoomRentRecordParam param) {

        RoomRentRecordPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的租赁信息!");

        List<Long> roomIds = param.getRentRooms().stream().map(RentRecordRoomParam::getRoomId).collect(Collectors.toList());

        validator.verifyIsExistByRoomIds(roomIds, param.getBeginTime(), param.getEndTime());

        // 更新
        ObjectUtils.copying(param, po);
        po.setRoomIds(roomIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        po.setRoomNames(param.getRentRooms().stream().map(RentRecordRoomParam::getRoomName).collect(Collectors.joining(",")));
        this.updateById(po);

        // 保存租赁记录与房间的关系
        rentRecordRoomService.saveOrUpdate(po.getId(), param.getRentRooms());

        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());

    }

    @Override
    public void remove(Long id) {
        RoomRentRecordPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的租赁信息!");

        if (po.getStatus() == RoomRentRecordStatusEnum.NORMAL) {
            // 清空关联的房间
            List<Long> roomIds = Arrays.stream(po.getRoomIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            roomCommandService.saveOrUpdateEnterprise(roomIds, 0L);
            // 更新企业有效租赁次数
            enterpriseCommandService.reduceRoomRentTime(po.getEnterpriseId());
        }

        // 删除租赁记录与房间的关系
        rentRecordRoomService.removeByRecordId(po.getId());

        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void removeBatch(String ids) {
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                this.remove(Long.valueOf(id));
            }
        } else {
            this.remove(Long.valueOf(ids));
        }
    }

    @Override
    public void updateRoomRentInfo(String rentRecordIds) {
        List<Long> ids = StringUtils.isNotEmpty(rentRecordIds) ? Arrays.stream(rentRecordIds.split(",")).map(Long::parseLong)
                .collect(Collectors.toList()) : null;

        List<RoomRentRecordPO> pos = this.lambdaQuery()
                .in(CollectionUtils.isNotEmpty(ids), RoomRentRecordPO::getId, ids)
                .list();

        Optional.ofNullable(pos).orElse(Collections.emptyList())
                .forEach(this::updateRoomRentInfo);
    }

    // 更新房间租赁信息
    @Override
    public void updateRoomRentInfo(RoomRentRecordPO po) {

        if (po == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        if (po.getStatus() != RoomRentRecordStatusEnum.EXPIRE) {

            Long enterpriseId = null;
            RoomRentRecordStatusEnum statusEnum = null;
            if (po.getEndTime().isBefore(now)) {
                // 过期
                statusEnum = RoomRentRecordStatusEnum.EXPIRE;

                // 更新企业有效租赁次数
                enterpriseCommandService.reduceRoomRentTime(po.getEnterpriseId());

                // 清空房间的企业ID
                enterpriseId = 0L;

            } else if (po.getBeginTime().isBefore(now) && po.getStatus() == RoomRentRecordStatusEnum.EXECUTING) {
                // 生效
                statusEnum = RoomRentRecordStatusEnum.NORMAL;

                // 关联房间的企业ID
                enterpriseId = po.getEnterpriseId();
            }

            if (statusEnum != null) {

                // 更新房间的企业ID
                List<Long> roomIds = Arrays.stream(po.getRoomIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
                roomCommandService.saveOrUpdateEnterprise(roomIds, enterpriseId);

                // 更新租赁记录状态
                SessionContextHolder.requestTenant(po.getTenant());
                po.setStatus(statusEnum);
                roomRentRecordMapper.updateById(po);
            }
        }

    }

}
