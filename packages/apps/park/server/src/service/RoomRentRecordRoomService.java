package com.guhecloud.cube.srv.gen.park.management.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RentRecordRoomParam;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordRoomPO;

import java.util.List;

public interface RoomRentRecordRoomService extends IService<RoomRentRecordRoomPO> {

    void saveOrUpdate(Long rentRecordId, List<RentRecordRoomParam> rentRooms);

    void removeByRecordId(Long rentRecordId);

    List<RoomRentRecordRoomPO> getListByRoomIds(List<Long> roomIds);

    List<RentRecordRoomParam> getParamByRentRecordId(Long rentRecordId);

}
