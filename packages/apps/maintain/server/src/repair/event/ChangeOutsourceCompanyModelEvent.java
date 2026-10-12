package com.guhecloud.cube.srv.gen.maintain.server.repair.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/5/25 11:11
 * @description
 */
@Getter
public class ChangeOutsourceCompanyModelEvent extends ApplicationEvent {
    private final Long outsourceCompanyId;
    private final Set<Long> outsourceModelId;

    public ChangeOutsourceCompanyModelEvent(Object source,
                                            Long outsourceCompanyId,
                                            Set<Long> outsourceModelId
                                           ) {
        super(source);
        this.outsourceCompanyId = outsourceCompanyId;
        this.outsourceModelId = outsourceModelId;
    }
}
