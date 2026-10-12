package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.core.innerbus.SouthInnerBus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 基于HTTP推送的北向接口目前不实现，后续在处理
 */
@Slf4j
@RestController
public class NorthSubscribeController {
    @Resource
    private SouthInnerBus southInnerBus;

}
