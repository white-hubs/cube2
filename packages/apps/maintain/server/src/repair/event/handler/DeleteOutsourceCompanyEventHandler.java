package com.guhecloud.cube.srv.gen.maintain.server.repair.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceCompanyModelService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceCompanyScopeService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.DeleteOutsourceCompanyEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyModelPO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyScopePO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @date 2022/5/19 11:22
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeleteOutsourceCompanyEventHandler {

    private final OutsourceCompanyModelService outsourceCompanyModelService;
    private final OutsourceCompanyScopeService outsourceCompanyScopeService;

    @EventListener
    public void deleteOutsourceModel(DeleteOutsourceCompanyEvent deleteOutsourceCompanyEvent) {
        outsourceCompanyModelService.remove(
                Wrappers.<OutsourceCompanyModelPO>lambdaQuery()
                        .eq(
                                OutsourceCompanyModelPO::getOutsourceCompanyId,
                                deleteOutsourceCompanyEvent.getOutsourceCompanyId()
                           )
                                           );
    }

    @EventListener
    public void saveOutsourceScope(DeleteOutsourceCompanyEvent deleteOutsourceCompanyEvent) {
        outsourceCompanyScopeService.remove(
                Wrappers.<OutsourceCompanyScopePO>lambdaQuery()
                        .eq(OutsourceCompanyScopePO::getOutsourceCompanyId, deleteOutsourceCompanyEvent.getOutsourceCompanyId())
                                           );
    }
}
