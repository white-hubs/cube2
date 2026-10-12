package com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request;

import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkageActionValidRequest extends LinkageActionRequest {

    public LinkageActionValidRequest(LinkageActionTypeEnum actionType) {
        super(actionType);
    }

    private String actionConfig;
}
