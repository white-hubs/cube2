package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardTypeReferCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardTypeReferPO;

import java.util.List;

public interface InspectStandardTypeReferCommandService extends IService<InspectStandardTypeReferPO> {

    void saveOrUpdate(Long standardId, List<InspectStandardTypeReferCommandModel> standardTypeRefers);

    void removeByStandardId(Long standardId);
}
