package com.guhecloud.cube.biz.mdata.server.config.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.config.entity.StreamMediaConfig;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigQueryParam;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigUpdateParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface StreamMediaConfigService extends IService<StreamMediaConfig> {

    Long save(StreamMediaConfigModifyParam param);

    void update(Long id, StreamMediaConfigUpdateParam param);

    StreamMediaConfig getOne();

    StreamMediaConfig getById(Serializable id);

    List<StreamMediaConfig> listStreamMediaConfigs(Collection<Long> ids);

    Page<StreamMediaConfig> listStreamMediaConfigs(StreamMediaConfigQueryParam param);

    void remove(Long id);
}
