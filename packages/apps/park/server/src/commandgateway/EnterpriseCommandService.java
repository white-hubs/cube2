package com.guhecloud.cube.srv.gen.park.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseParam;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterprisePO;

public interface EnterpriseCommandService extends IService<EnterprisePO> {

    void save(EnterpriseParam param);

    void update(Long id, EnterpriseParam param);

    void remove(Long id);

    void removeBatch(String ids);

    void addRoomRentTime(Long enterpriseId);

    void reduceRoomRentTime(Long enterpriseId);
}
