package com.guhecloud.cube.biz.subsystem.parking.server.guidance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.dao.GuideContentConfigMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.entity.GuideContentConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.service.GuideContentConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 车辆引导牌配置表 Service 实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GuideContentConfigServiceImpl extends ServiceImpl<GuideContentConfigMapper, GuideContentConfig> implements GuideContentConfigService {

}

