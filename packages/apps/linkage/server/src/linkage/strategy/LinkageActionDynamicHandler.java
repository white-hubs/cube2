package com.guhecloud.cube.biz.linkage.server.linkage.strategy;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionExecuteRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionValidRequest;
import com.guhecloud.platform.base.standard.exception.ResourceNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LinkageActionDynamicHandler implements ApplicationContextAware, LinkageActionHandler {

    private List<LinkageActionHandler> linkageActionHandlers = new ArrayList<>();

    @Override
    public boolean supports(LinkageActionRequest request) {
        return false;
    }

    @Override
    public ExecuteStatusEnum eventActionExecute(LinkageActionExecuteRequest request) {
        return this.determineLinkageActionHandler(request).eventActionExecute(request);
    }

    @Override
    public void verification(LinkageActionValidRequest request) {
        this.determineLinkageActionHandler(request).verification(request);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(LinkageActionHandler.class).forEach((k, v) -> linkageActionHandlers.add(v));
    }

    public LinkageActionHandler determineLinkageActionHandler(LinkageActionRequest request) {
        return this.linkageActionHandlers.stream().filter(linkageActionHandler -> linkageActionHandler.supports(request)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("没有找到 input=" + request.getActionType() + " 的策略处理器"));
    }
}
