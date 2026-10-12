package com.guhecloud.cube.biz.mdata.server.area.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.area.dao.AreaFeatureMapper;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaFeature;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaFeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 区域特征
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaFeatureServiceImpl extends ServiceImpl<AreaFeatureMapper, AreaFeature> implements AreaFeatureService {
}
