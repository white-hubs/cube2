package com.guhecloud.cube.biz.mdata.server.stall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallExpand;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallExpandModifyParam;

import java.util.Collection;
import java.util.List;

public interface StallExpandService extends IService<StallExpand> {

    Long save(Long stallId, StallExpandModifyParam param);

    StallExpand getByStallId(Long stallId);

    List<StallExpand> getStallExpands(Collection<Long> stallIds);

    void removeByStallId(Long stallId);
}
