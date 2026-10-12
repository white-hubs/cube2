package com.guhecloud.subsystem.config.listener.web;

import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.context.SubsystemContextHolder;
import com.guhecloud.subsystem.config.listener.web.http.SubsystemHttpConstants;
import com.guhecloud.subsystem.config.listener.web.model.SubsystemRequester;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

@WebListener
public class SubsystemRequestContextListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        SubsystemContextHolder.setRequester(this.buildSubsystemRequester((HttpServletRequest) sre.getServletRequest()));
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        SubsystemContextHolder.clearSessionContext();
    }

    private SubsystemRequester buildSubsystemRequester(HttpServletRequest request) {
        String subsystem = ObjectUtils.ifNull(request.getHeader(SubsystemHttpConstants.HEADER_SUBSYSTEM), StringUtils.EMPTY_STRING).trim();
        String manufacturer = ObjectUtils.ifNull(request.getHeader(SubsystemHttpConstants.HEADER_MANUFACTURER), StringUtils.EMPTY_STRING)
                .trim();
        return SubsystemRequester.builder()
                .subsystemCode(subsystem)
                .manufacturer(manufacturer)
                .build();
    }
}
