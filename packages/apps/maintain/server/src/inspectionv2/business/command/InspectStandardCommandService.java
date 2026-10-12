package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardToTypeCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;

public interface InspectStandardCommandService extends IService<InspectStandardPO> {

    void save(InspectStandardCommandModel param);

    void saveToType(InspectStandardToTypeCommandModel param);

    void enabled(Long id, Boolean enable);

    void update(Long id, InspectStandardCommandModel param);

    void remove(Long id);

    void removeBatch(String ids);
}
