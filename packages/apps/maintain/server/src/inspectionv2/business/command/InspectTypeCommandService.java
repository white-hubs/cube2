package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTypeCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTypePO;

import jakarta.validation.constraints.NotNull;

public interface InspectTypeCommandService extends IService<InspectTypePO> {

    void save(InspectTypeCommandModel command);

    void update(@NotNull Long id, InspectTypeCommandModel command);

    void remove(Long id);
}
