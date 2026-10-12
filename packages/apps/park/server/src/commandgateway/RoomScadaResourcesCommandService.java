package com.guhecloud.cube.srv.gen.park.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomScadaResourcesParam;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomScadaResourcesPO;

public interface RoomScadaResourcesCommandService extends IService<RoomScadaResourcesPO> {

    void save(RoomScadaResourcesParam param);

    void update(Long id, RoomScadaResourcesParam param);

    void remove(Long id);

    void removeBatch(String ids);

}
