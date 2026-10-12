package com.guhecloud.cube.biz.mdata.server.area.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaExtend;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaExtendParam;

import java.util.Set;

public interface AreaExtendService extends IService<AreaExtend> {

    Long save(Long areaId, AreaExtendParam areaExtendParam);

    AreaExtend getById(Long id);

    Set<AreaExtend> getByIds(Set<Long> areaIds);

    void remove(Long areaId);
}
