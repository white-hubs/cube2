package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectCronCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectCronPO;

public interface InspectCronCommandService extends IService<InspectCronPO> {
    void save(InspectCronCommandModel param);

    void update(Long id, InspectCronCommandModel param);

    void remove(Long id);

    void removeBatch(String ids);
}
