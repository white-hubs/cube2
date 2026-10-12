package com.guhecloud.cube.biz.mdata.server.area.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.area.dao.AreaTagMapper;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaTag;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 区域标签服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaTagServiceImpl extends ServiceImpl<AreaTagMapper, AreaTag> implements AreaTagService {
}
