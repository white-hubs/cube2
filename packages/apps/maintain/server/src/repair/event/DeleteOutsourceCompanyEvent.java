package com.guhecloud.cube.srv.gen.maintain.server.repair.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author pengjian
 * @date 2022/5/19 11:18
 * @description
 */
@Getter
public class DeleteOutsourceCompanyEvent extends ApplicationEvent {
    private final Long outsourceCompanyId;

    public DeleteOutsourceCompanyEvent(Object source,
                                       Long outsourceCompanyId
                                      ) {
        super(source);
        this.outsourceCompanyId = outsourceCompanyId;
    }
}
