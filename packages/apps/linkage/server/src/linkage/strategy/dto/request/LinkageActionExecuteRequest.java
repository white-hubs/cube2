package com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request;

import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkageActionExecuteRequest extends LinkageActionRequest {

    public LinkageActionExecuteRequest(LinkageActionTypeEnum actionType) {
        super(actionType);
    }

    /**
     * 事件
     */
    private Event event;

    /**
     * 事件动作
     */
    private LinkageActionDetailRequest linkageActionDetailRequest;
}
