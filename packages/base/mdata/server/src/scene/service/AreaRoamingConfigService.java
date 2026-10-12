package com.guhecloud.cube.biz.mdata.server.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.scene.entity.AreaRoamingConfig;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaRoamingConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaRoamingConfigQueryParam;

import java.io.Serializable;
import java.util.List;

public interface AreaRoamingConfigService extends IService<AreaRoamingConfig> {

    Long save(AreaRoamingConfigModifyParam param);

    void update(Long id, AreaRoamingConfigModifyParam param);

    List<AreaRoamingConfig> listAreaRoamingConfigs(AreaRoamingConfigQueryParam param);

    Page<AreaRoamingConfig> page(AreaRoamingConfigQueryParam param);

    AreaRoamingConfig getById(Serializable id);

    void remove(Long id);
}
