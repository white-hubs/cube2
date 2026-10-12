package com.guhecloud.cube.biz.meattrade.server.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.dao.ConfigMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Config;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.base.param.ConfigCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.ConfigUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.ConfigService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Override
    public void save(@NonNull ConfigCreateParam param) {
        Config config = ObjectUtils.copying(param, Config.class);
        this.save(config);
    }

    @Override
    public void update(@NonNull ConfigUpdateParam param) {
        Config config = this.getById(param.getId());
        ObjectUtils.copying(param, config);
        this.updateById(config);
    }

    @Override
    public Config getByType(@NonNull ConfigEnum type) {
        return this.lambdaQuery()
                .eq(Config::getType, type)
                .one();
    }
}
