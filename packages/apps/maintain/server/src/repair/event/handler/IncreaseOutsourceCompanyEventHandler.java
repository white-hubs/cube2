package com.guhecloud.cube.srv.gen.maintain.server.repair.event.handler;

import com.guhecloud.cube.srv.gen.maintain.server.repair.event.ChangeOutsourceCompanyModelEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.ChangeOutsourceCompanyScopeEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.IncreaseOutsourceCompanyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @date 2022/5/19 11:22
 * @description
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseOutsourceCompanyEventHandler {

    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public void saveOutsourceModel(IncreaseOutsourceCompanyEvent increaseOutsourceCompanyEvent) {
        applicationEventPublisher.publishEvent(
                new ChangeOutsourceCompanyModelEvent(
                        this,
                        increaseOutsourceCompanyEvent.getOutsourceCompanyId(),
                        increaseOutsourceCompanyEvent.getIncreaseOutsourceCompanyCommand().getOutsourceModelIdList()
                )
                                              );

    }

    @EventListener
    public void saveOutsourceScope(IncreaseOutsourceCompanyEvent increaseOutsourceCompanyEvent) {
        applicationEventPublisher.publishEvent(new ChangeOutsourceCompanyScopeEvent(
                this,
                increaseOutsourceCompanyEvent.getOutsourceCompanyId(),
                increaseOutsourceCompanyEvent.getIncreaseOutsourceCompanyCommand().getOutsourceScopeIdList()
        ));
    }
}
