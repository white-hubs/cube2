package com.guhecloud.cube.biz.mdata.server.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;

public interface StallSceneService {

    Page<Stall> getStalls(StallSceneQueryParam param);

    Page<Stall> getSceneNotBindStalls(StallSceneQueryParam param);
}