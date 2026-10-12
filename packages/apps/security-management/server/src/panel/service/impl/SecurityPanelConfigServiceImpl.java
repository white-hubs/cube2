package com.guhecloud.cube.srv.gen.securitymanagement.server.panel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.dao.SecurityPanelConfigMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.entity.SecurityPanelConfig;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.request.command.IncreaseSecurityPanelConfigCommand;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.request.query.SecurityPanelConfigQuery;
import com.guhecloud.cube.srv.gen.securitymanagement.server.panel.service.SecurityPanelConfigService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityPanelConfigServiceImpl extends ServiceImpl<SecurityPanelConfigMapper, SecurityPanelConfig> implements SecurityPanelConfigService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull IncreaseSecurityPanelConfigCommand command) {
        this.remove(command.getBusinessKey(), command.getSceneType(), command.getApplicationCode());
        SecurityPanelConfig securityPanelConfig = ObjectUtils.copying(command, SecurityPanelConfig.class);
        this.baseMapper.insert(securityPanelConfig);
        return securityPanelConfig.getId();
    }

    @Override
    public List<SecurityPanelConfig> getSecurityPanelConfigs(SecurityPanelConfigQuery query) {
        return this.baseMapper.selectList(Wrappers.<SecurityPanelConfig>lambdaQuery()
                                                  .in(!CollectionUtils.isEmpty(query.getBusinessKeys()), SecurityPanelConfig::getBusinessKey, query.getBusinessKeys())
                                                  .eq(StringUtils.isNotEmpty(query.getSceneType()), SecurityPanelConfig::getSceneType, query.getSceneType())
                                                  .eq(StringUtils.isNotEmpty(query.getApplicationCode()), SecurityPanelConfig::getApplicationCode, query.getApplicationCode())
                                         );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull String businessKey, String sceneType, String applicationCode) {
        this.baseMapper.delete(Wrappers.<SecurityPanelConfig>lambdaQuery()
                                       .eq(SecurityPanelConfig::getBusinessKey, businessKey)
                                       .eq(SecurityPanelConfig::getSceneType, sceneType)
                                       .eq(SecurityPanelConfig::getApplicationCode, applicationCode));
    }

}
