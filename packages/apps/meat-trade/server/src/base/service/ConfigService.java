package com.guhecloud.cube.biz.meattrade.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Config;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.base.param.ConfigCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.ConfigUpdateParam;

public interface ConfigService extends IService<Config> {

    /**
     * 基础配置新增
     *
     * @param param 基础配置新增信息
     */
    void save(ConfigCreateParam param);

    /**
     * 基础配置更新
     *
     * @param param 基础配置更新信息
     */
    void update(ConfigUpdateParam param);

    /**
     * 根据业务类型查看基础配置
     *
     * @param type 业务类型
     *
     * @return 基础配置信息
     */
    Config getByType(ConfigEnum type);

}
