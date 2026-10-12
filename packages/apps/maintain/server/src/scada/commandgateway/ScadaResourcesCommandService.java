package com.guhecloud.cube.srv.gen.maintain.server.scada.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.scada.commandgateway.command.ScadaResourcesCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.scada.po.ScadaResourcesPO;

public interface ScadaResourcesCommandService extends IService<ScadaResourcesPO> {

    Long save(ScadaResourcesCommandModel commandModel);

    void update(Long id, ScadaResourcesCommandModel commandModel);

    void remove(Long id);

    void removeBatch(String ids);

}
