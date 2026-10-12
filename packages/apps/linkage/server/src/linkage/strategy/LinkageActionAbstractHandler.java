package com.guhecloud.cube.biz.linkage.server.linkage.strategy;

import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionRequest;

public abstract class LinkageActionAbstractHandler implements LinkageActionHandler {

    private LinkageActionTypeEnum actionType;

    public LinkageActionAbstractHandler(LinkageActionTypeEnum actionType) {
        this.actionType = actionType;
    }

    @Override
    public boolean supports(LinkageActionRequest request) {
        return this.actionType == request.getActionType();
    }
}
