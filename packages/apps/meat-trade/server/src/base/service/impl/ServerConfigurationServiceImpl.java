package com.guhecloud.cube.biz.meattrade.server.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.dao.SeverConfigurationMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.ServerConfiguration;
import com.guhecloud.cube.biz.meattrade.server.base.param.ServerConfigurationCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.ServerConfigurationUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.ServerConfigurationService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lzy
 * @date 2021/9/13
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerConfigurationServiceImpl extends ServiceImpl<SeverConfigurationMapper, ServerConfiguration> implements ServerConfigurationService {

    @Override
    public void save(ServerConfigurationCreateParam param) {
        ServerConfiguration serverConfiguration = ObjectUtils.copying(param, ServerConfiguration.class);
        this.save(serverConfiguration);
    }

    @Override
    public void update(ServerConfigurationUpdateParam param) {
        ServerConfiguration serverConfiguration = this.getById(param.getId());
        ObjectUtils.copying(param, serverConfiguration);
        this.updateById(serverConfiguration);
    }
}
