package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ChangeParkingInfoCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.DelParkingInfoCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.IncreaseParkingInfoCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ModifyParkingInfoDeviceCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ModifyParkingInfoDockConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.converters.ParkingInfoConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.event.ParkingChangeEvent;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.event.ParkingDelEvent;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.event.ParkingDockConfigChangeEvent;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.file.ParkFileAppService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class ParkingInfoCommandService {

    private final ParkingInfoRepository parkingInfoRepository;

    private final ParkFileAppService parkFileAppService;

    private final ApplicationEventPublisher eventPublisher;

    public ParkingInfoCommandService(ParkingInfoRepository parkingInfoRepository, ParkFileAppService parkFileAppService,
                                     ApplicationEventPublisher eventPublisher
                                    ) {
        this.parkingInfoRepository = parkingInfoRepository;
        this.parkFileAppService = parkFileAppService;
        this.eventPublisher = eventPublisher;
    }

    public void increaseParkingInfo(IncreaseParkingInfoCommand command) {
        var parkingInfo = ParkingInfoConverter.toParkingInfo(command);
        parkingInfoRepository.save(parkingInfo);
        parkFileAppService.modifyParkingInfoFiles(parkingInfo.getId(), command.getFiles());
        // 发布车场配置变更事件
        eventPublisher.publishEvent(new ParkingChangeEvent(this, parkingInfo));
    }

    public void changeParkingInfo(ChangeParkingInfoCommand command) {
        var parkingInfo = parkingInfoRepository.findById(command.getId());
        ParkingInfoConverter.updateParkingInfo(parkingInfo, command);
        parkingInfoRepository.save(parkingInfo);
        parkFileAppService.modifyParkingInfoFiles(parkingInfo.getId(), command.getFiles());
        // 发布车场配置变更事件
        eventPublisher.publishEvent(new ParkingChangeEvent(this, parkingInfo));
    }

    public void modifyParkingInfoDevices(Long id, ModifyParkingInfoDeviceCommand command) {
        var parkingInfo = parkingInfoRepository.findById(id);
        parkingInfo.setDevices(ParkingInfoConverter.toParkingInfoDevices(command));
        parkingInfoRepository.save(parkingInfo);
        // 发布车场配置变更事件
        eventPublisher.publishEvent(new ParkingChangeEvent(this, parkingInfo));
    }

    public void modifyParkingInfoDockConfig(Long id, ModifyParkingInfoDockConfigCommand command) {
        var oldParkingInfo = parkingInfoRepository.findById(id);
        var parkingInfoDockConfig = ParkingInfoConverter.toParkingInfoDockConfig(command);
        var newParkingInfo = parkingInfoRepository.findById(id);
        newParkingInfo.setDockConfig(parkingInfoDockConfig);
        parkingInfoRepository.save(newParkingInfo);
        // 发布车场配置变更事件
        eventPublisher.publishEvent(new ParkingDockConfigChangeEvent(this, oldParkingInfo, newParkingInfo));
    }

    public void delParkingInfo(DelParkingInfoCommand command) {
        parkingInfoRepository.deleteByIds(command.getIds());
        var parkingInfos = parkingInfoRepository.findList(Collections.emptyList(), null);
        eventPublisher.publishEvent(new ParkingDelEvent(this, parkingInfos));
    }
}
