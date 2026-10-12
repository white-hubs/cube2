package com.guhecloud.cube.biz.linkage.server.linkage.strategy;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionExecuteRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionValidRequest;

public interface LinkageActionHandler {

    boolean supports(LinkageActionRequest request);

    ExecuteStatusEnum eventActionExecute(LinkageActionExecuteRequest request);

    void verification(LinkageActionValidRequest request);
}
