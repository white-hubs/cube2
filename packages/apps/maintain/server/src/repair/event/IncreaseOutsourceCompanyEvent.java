package com.guhecloud.cube.srv.gen.maintain.server.repair.event;

import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.company.IncreaseOutsourceCompanyCommand;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author pengjian
 * @date 2022/5/19 11:18
 * @description
 */
@Getter
public class IncreaseOutsourceCompanyEvent extends ApplicationEvent {
    private final IncreaseOutsourceCompanyCommand increaseOutsourceCompanyCommand;
    private final Long outsourceCompanyId;

    public IncreaseOutsourceCompanyEvent(Object source,
                                         IncreaseOutsourceCompanyCommand increaseOutsourceCompanyCommand,
                                         Long outsourceCompanyId
                                        ) {
        super(source);
        this.increaseOutsourceCompanyCommand = increaseOutsourceCompanyCommand;
        this.outsourceCompanyId = outsourceCompanyId;
    }
}
