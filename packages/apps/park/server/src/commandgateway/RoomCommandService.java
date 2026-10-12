package com.guhecloud.cube.srv.gen.park.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomParam;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomPO;

import java.util.List;

public interface RoomCommandService extends IService<RoomPO> {

    void save(RoomParam param);

    void update(Long id, RoomParam param);

    void remove(Long id);

    void removeBatch(String ids);

    void saveOrUpdateEnterprise(List<Long> roomIds, Long enterpriseId);

}
