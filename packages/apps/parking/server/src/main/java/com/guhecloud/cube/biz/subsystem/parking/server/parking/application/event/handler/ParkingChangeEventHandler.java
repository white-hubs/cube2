package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.event.handler;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.ParkingThirdProviderFactory;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.event.ParkingChangeEvent;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class ParkingChangeEventHandler {

    private final ParkingThirdProviderFactory parkingThirdProviderFactory;
    private final ParkingInfoRepository parkingInfoRepository;

    public ParkingChangeEventHandler(ParkingThirdProviderFactory parkingThirdProviderFactory, ParkingInfoRepository parkingInfoRepository) {
        this.parkingThirdProviderFactory = parkingThirdProviderFactory;
        this.parkingInfoRepository = parkingInfoRepository;
    }

    /**
     * 处理车场配置变更事件
     *
     * @param event 车场配置变更事件
     */
    @EventListener
    public void handleParkingChangeEvent(final ParkingChangeEvent event) {
        log.info("处理车场配置变更事件: {}", event);
        if (Objects.nonNull(event.getParkingInfo().getDockConfig())) {
            //刷新配置
            var provider = parkingThirdProviderFactory.getProvider(event.getParkingInfo().getDockConfig().getProviderCode());
            if (Objects.isNull(provider)) {
                log.warn("未找到车场接入网关: {}", event.getParkingInfo().getDockConfig().getProviderCode());
                return;
            }
            provider.refreshParkingInfo(event.getParkingInfo());
            parkingInfoRepository.save(event.getParkingInfo());
        }
    }
}
