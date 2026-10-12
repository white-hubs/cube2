package com.guhecloud.cube.srv.gen.maintain.server.repair.event.handler;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceCompanyModelService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.ChangeOutsourceCompanyModelEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyModelPO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/5/25 11:16
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeOutsourceCompanyModelEventHandler {
    private final OutsourceCompanyModelService outsourceCompanyModelService;

    @EventListener
    public void saveOutsourceModel(ChangeOutsourceCompanyModelEvent changeOutsourceCompanyModelEvent) {
        // 先删除再保存
        outsourceCompanyModelService.remove(
                Wrappers.<OutsourceCompanyModelPO>lambdaQuery()
                        .eq(
                                OutsourceCompanyModelPO::getOutsourceCompanyId,
                                changeOutsourceCompanyModelEvent.getOutsourceCompanyId()
                           )
                                           );
        // 委外模式
        Set<Long> outsourceModelIds = changeOutsourceCompanyModelEvent.getOutsourceModelId();
        if (CollectionUtils.isNotEmpty(outsourceModelIds)) {
            List<OutsourceCompanyModelPO> waitSaveForOutsourceCompanyModels = new ArrayList<>(outsourceModelIds.size());
            outsourceModelIds.stream().forEach(outsourceModelId -> waitSaveForOutsourceCompanyModels.add(
                                                       new OutsourceCompanyModelPO(changeOutsourceCompanyModelEvent.getOutsourceCompanyId(), outsourceModelId))
                                              );

            this.outsourceCompanyModelService.saveBatch(waitSaveForOutsourceCompanyModels);
        }

    }
}
