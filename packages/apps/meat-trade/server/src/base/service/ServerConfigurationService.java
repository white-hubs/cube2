package com.guhecloud.cube.biz.meattrade.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.base.entity.ServerConfiguration;
import com.guhecloud.cube.biz.meattrade.server.base.param.ServerConfigurationCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.ServerConfigurationUpdateParam;

/**
 * @author lzy
 * @date 2021/9/13
 */
public interface ServerConfigurationService extends IService<ServerConfiguration> {

    /**
     * 服务器配置信息新增
     *
     * @param param 服务器配置新增信息
     */
    void save(ServerConfigurationCreateParam param);

    /**
     * 服务器配置信息编辑
     *
     * @param param 服务器配置编辑信息
     */
    void update(ServerConfigurationUpdateParam param);
}
