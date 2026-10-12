package com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request;

import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;

public abstract class LinkageActionRequest {

    private LinkageActionTypeEnum actionType;

    public LinkageActionRequest(LinkageActionTypeEnum actionType) {
        this.actionType = actionType;
    }

    public LinkageActionTypeEnum getActionType() {
        return actionType;
    }
}
