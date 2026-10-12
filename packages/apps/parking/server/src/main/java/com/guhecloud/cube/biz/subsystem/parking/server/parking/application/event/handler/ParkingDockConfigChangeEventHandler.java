package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.event.handler;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter.ParkingThirdProviderFactory;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.event.ParkingDockConfigChangeEvent;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class ParkingDockConfigChangeEventHandler {

    private final ParkingThirdProviderFactory parkingThirdProviderFactory;

    private final ParkingInfoRepository parkingInfoRepository;

    public ParkingDockConfigChangeEventHandler(ParkingThirdProviderFactory parkingThirdProviderFactory,
                                               ParkingInfoRepository parkingInfoRepository
                                              ) {
        this.parkingThirdProviderFactory = parkingThirdProviderFactory;
        this.parkingInfoRepository = parkingInfoRepository;
    }

    /**
     * 处理车场接入方式变更事件
     *
     * @param event 车场接入方式变更事件
     */
    @EventListener
    public void dockConfigChange(final ParkingDockConfigChangeEvent event) {
        if (Objects.nonNull(event.getOldParkingInfo().getDockConfig())) {
            parkingThirdProviderFactory.getProvider(event.getOldParkingInfo().getDockConfig().getProviderCode())
                    .deactivate(event.getOldParkingInfo());
        }
        parkingThirdProviderFactory.getProvider(event.getNewParkingInfo().getDockConfig().getProviderCode())
                .activate(event.getNewParkingInfo());
        parkingInfoRepository.save(event.getNewParkingInfo());
    }
}
