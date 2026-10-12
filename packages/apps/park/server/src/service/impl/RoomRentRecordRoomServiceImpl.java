package com.guhecloud.cube.srv.gen.park.management.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RentRecordRoomParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomRentRecordRoomMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordRoomPO;
import com.guhecloud.cube.srv.gen.park.management.server.service.RoomRentRecordRoomService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomRentRecordRoomServiceImpl extends ServiceImpl<RoomRentRecordRoomMapper, RoomRentRecordRoomPO> implements RoomRentRecordRoomService {

    @Override
    public void saveOrUpdate(Long rentRecordId, List<RentRecordRoomParam> rentRooms) {
        this.lambdaUpdate()
                .eq(RoomRentRecordRoomPO::getRecordId, rentRecordId)
                .remove();

        List<RoomRentRecordRoomPO> pos = rentRooms.stream().map(rentRoomParam -> {
            RoomRentRecordRoomPO po = ObjectUtils.copying(rentRoomParam, RoomRentRecordRoomPO.class);
            po.setRecordId(rentRecordId);
            return po;
        }).collect(Collectors.toList());

        this.saveBatch(pos);
    }

    @Override
    public void removeByRecordId(Long rentRecordId) {
        List<RoomRentRecordRoomPO> rentRecordRoomPOS = this.lambdaQuery()
                .eq(RoomRentRecordRoomPO::getRecordId, rentRecordId)
                .list();

        DatabaseContextHolder.softDelete(this, rentRecordRoomPOS);

    }

    @Override
    public List<RoomRentRecordRoomPO> getListByRoomIds(List<Long> roomIds) {

        return this.lambdaQuery()
                .in(RoomRentRecordRoomPO::getRoomId, roomIds)
                .select(RoomRentRecordRoomPO::getRecordId, RoomRentRecordRoomPO::getRoomId, RoomRentRecordRoomPO::getRoomName)
                .list();
    }

    @Override
    public List<RentRecordRoomParam> getParamByRentRecordId(Long rentRecordId) {
        List<RoomRentRecordRoomPO> pos = this.lambdaQuery()
                .eq(RoomRentRecordRoomPO::getRecordId, rentRecordId)
                .list();
        return ObjectUtils.copying(pos, RentRecordRoomParam.class);
    }
}
