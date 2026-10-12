package com.guhecloud.cube.srv.gen.park.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.TypeDataParam;
import com.guhecloud.cube.srv.gen.park.management.server.po.TypeDataPO;

public interface TypeDataCommandService extends IService<TypeDataPO> {

    void save(String tag, TypeDataParam param);

    void update(Long id, TypeDataParam param);

    void remove(Long id);

    void removeBatch(String ids);
}
