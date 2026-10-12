package com.guhecloud.cube.srv.gen.park.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomRentRecordParam;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordPO;

public interface RoomRentRecordCommandService extends IService<RoomRentRecordPO> {

    void save(RoomRentRecordParam param);

    void update(Long id, RoomRentRecordParam param);

    void remove(Long id);

    void removeBatch(String ids);

    void updateRoomRentInfo(String rentRecordIds);

    void updateRoomRentInfo(RoomRentRecordPO po);

}
