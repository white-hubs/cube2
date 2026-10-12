package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;

import java.util.List;

public interface InspectRecordItemCommandService extends IService<InspectRecordItemPO> {

    int saveByRecord(Long recordId, List<InspectRecordItemCommandModel> commandModels);
}
