package com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.mqtt.HikSpccParkMqttClientProtocol;
import com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.service.HikSpccParkingService;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author xiaofeng
 * @describe 海康 智慧监管综合平台 停车场对接
 * @date 2024/10/31 15:07
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HikSpccParkingController {
    private final HikSpccParkingService hikParkingService;
    private final HikSpccParkMqttClientProtocol hikSpccParkMqttClientProtocol;
    private final SubsystemRedisUtils subsystemRedisUtils;

    /**
     * 获取停车场信息
     *
     * @return 停车场信息
     */
    @GetMapping("/hikspcc/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.hikParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param 停车场订阅参数
     *
     * @return 操作结果
     */
    @PostMapping("/hikspcc/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        log.info("海康车场数据订阅,param={}", param);
        //连接第三方mqtt
        hikSpccParkMqttClientProtocol.connect(getBasicConfig());
        return this.hikParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param 停车场订阅参数
     *
     * @return 操作结果
     */
    @PostMapping("/hikspcc/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        hikSpccParkMqttClientProtocol.disconnect();
        return this.hikParkingService.parkingUnSubscribe(param);
    }

    /**
     * 获取基础配置
     *
     * @return
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "hikspcc" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }
}
