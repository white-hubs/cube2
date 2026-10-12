package com.guhecloud.cube.srv.gen.securitymanagement.server.panel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.entity.SecurityPanelConfig;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.request.command.IncreaseSecurityPanelConfigCommand;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.request.query.SecurityPanelConfigQuery;

import java.util.List;

public interface SecurityPanelConfigService extends IService<SecurityPanelConfig> {

    Long save(IncreaseSecurityPanelConfigCommand command);

    List<SecurityPanelConfig> getSecurityPanelConfigs(SecurityPanelConfigQuery query);

    void remove(String businessKey, String sceneType, String applicationCode);
}
