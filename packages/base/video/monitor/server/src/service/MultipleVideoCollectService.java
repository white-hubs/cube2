package com.guhecloud.cube.biz.vmonitor.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollect;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectQueryParam;

import java.io.Serializable;

public interface MultipleVideoCollectService extends IService<MultipleVideoCollect> {

    Long save(MultipleVideoCollectModifyParam param);

    void update(Long id, MultipleVideoCollectModifyParam param);

    MultipleVideoCollect getById(Serializable id);

    Page<MultipleVideoCollect> page(MultipleVideoCollectQueryParam param);

    void remove(Long id);

}