package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardItemPO;

import java.util.List;

public interface InspectStandardItemCommandService extends IService<InspectStandardItemPO> {

    void saveOrUpdate(Long standardId, List<InspectStandardItemCommandModel> standardItems);
}
