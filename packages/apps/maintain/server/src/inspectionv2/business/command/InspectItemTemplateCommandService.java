package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemBatchCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectItemTemplatePO;

public interface InspectItemTemplateCommandService extends IService<InspectItemTemplatePO> {

    void addInspectItem(InspectItemBatchCommandModel command);

    void updateInspectItem(Long id, InspectItemCommandModel command);

    void deleteInspectItem(Long id);

}
