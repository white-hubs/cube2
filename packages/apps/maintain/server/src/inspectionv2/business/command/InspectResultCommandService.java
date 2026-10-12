package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;

public interface InspectResultCommandService extends IService<InspectResultPO> {

    void save(InspectResultCommandModel commandModel);

    void update(Long id, InspectResultCommandModel commandModel);

    void remove(Long id);

    void removeBatch(String ids);
}
