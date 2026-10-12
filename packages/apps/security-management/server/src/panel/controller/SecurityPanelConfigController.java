package com.guhecloud.cube.srv.gen.securitymanagement.server.panel.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.entity.SecurityPanelConfig;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.request.command.IncreaseSecurityPanelConfigCommand;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.request.query.SecurityPanelConfigQuery;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.service.SecurityPanelConfigService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.vo.SecurityPanelConfigVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityPanelConfigController {

    private final SecurityPanelConfigService securityPanelConfigService;

    /**
     * 保存面板配置
     *
     * @param command
     *
     * @return
     */
    @PostMapping("/security-panel-config")
    public Long save(@Valid @RequestBody IncreaseSecurityPanelConfigCommand command) {
        return this.securityPanelConfigService.save(command);
    }

    /**
     * 获取面板配置列表
     *
     * @param query
     *
     * @return
     */
    @PostMapping("/security-panel-config/list-query")
    public List<SecurityPanelConfigVO> getSecurityPanelConfigs(@Valid @RequestBody SecurityPanelConfigQuery query) {
        List<SecurityPanelConfig> securityPanelConfigs = this.securityPanelConfigService.getSecurityPanelConfigs(query);
        return ObjectUtils.copying(securityPanelConfigs, SecurityPanelConfigVO.class);
    }

    /**
     * 获取面板配置详情
     *
     * @param businessKey
     * @param sceneType
     * @param applicationCode
     *
     * @return
     */
    @GetMapping("/security-panel-config")
    public SecurityPanelConfigVO getSecurityPanelConfig(@RequestParam String businessKey,
                                                        @RequestParam String sceneType,
                                                        @RequestParam(required = false) String applicationCode
                                                       ) {
        SecurityPanelConfigQuery query = SecurityPanelConfigQuery.builder()
                .businessKeys(Collections.singleton(businessKey))
                .sceneType(sceneType)
                .applicationCode(applicationCode)
                .build();
        List<SecurityPanelConfig> securityPanelConfigs = this.securityPanelConfigService.getSecurityPanelConfigs(query);
        if (CollectionUtils.isEmpty(securityPanelConfigs)) {
            return null;
        }
        return ObjectUtils.copying(securityPanelConfigs.get(0), SecurityPanelConfigVO.class);
    }
}
