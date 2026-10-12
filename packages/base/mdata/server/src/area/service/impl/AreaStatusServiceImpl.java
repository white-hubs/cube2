package com.guhecloud.cube.biz.mdata.server.area.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.area.dao.AreaStatusMapper;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaStatus;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 区域状态服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaStatusServiceImpl extends ServiceImpl<AreaStatusMapper, AreaStatus> implements AreaStatusService {
}
