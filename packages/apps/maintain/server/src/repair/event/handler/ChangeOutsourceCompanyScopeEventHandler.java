package com.guhecloud.cube.srv.gen.maintain.server.repair.event.handler;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceCompanyScopeService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.ChangeOutsourceCompanyScopeEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyScopePO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/5/25 11:19
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeOutsourceCompanyScopeEventHandler {
    private final OutsourceCompanyScopeService outsourceCompanyScopeService;

    @EventListener
    public void saveOutsourceScope(ChangeOutsourceCompanyScopeEvent changeOutsourceCompanyScopeEvent) {
        outsourceCompanyScopeService.remove(
                Wrappers.<OutsourceCompanyScopePO>lambdaQuery()
                        .eq(OutsourceCompanyScopePO::getOutsourceCompanyId, changeOutsourceCompanyScopeEvent.getOutsourceCompanyId())
                                           );

        Set<String> deviceGenreIds = changeOutsourceCompanyScopeEvent.getOutsourceScopeId();
        if (CollectionUtils.isNotEmpty(deviceGenreIds)) {
            List<OutsourceCompanyScopePO> waitSaveForOutsourceCompanyScopes = new ArrayList<>(deviceGenreIds.size());
            deviceGenreIds.forEach(deviceGenreId -> waitSaveForOutsourceCompanyScopes.add(
                    new OutsourceCompanyScopePO(deviceGenreId, changeOutsourceCompanyScopeEvent.getOutsourceCompanyId())
                                                                                         ));
            this.outsourceCompanyScopeService.saveBatch(waitSaveForOutsourceCompanyScopes);
        }

    }
}
