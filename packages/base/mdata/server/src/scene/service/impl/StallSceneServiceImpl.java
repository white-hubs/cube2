package com.guhecloud.cube.biz.mdata.server.scene.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneQueryParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.StallSceneService;
import com.guhecloud.cube.biz.mdata.server.stall.dao.StallMapper;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 档位场景实现
 *
 * @author lanchun
 * @date 2021/12/2 16:56
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSceneServiceImpl implements StallSceneService {

    private final StallMapper stallMapper;

    @Override
    public Page<Stall> getStalls(StallSceneQueryParam param) {
        return stallMapper.selectSceneStalls(PagingUtils.convert(param), param);
    }

    @Override
    public Page<Stall> getSceneNotBindStalls(StallSceneQueryParam param) {
        return stallMapper.selectSceneNotBindStalls(PagingUtils.convert(param), param);
    }
}
