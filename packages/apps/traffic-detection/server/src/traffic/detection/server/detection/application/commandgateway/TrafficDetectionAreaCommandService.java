package com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway;

import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ChangeTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.DelTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.IncreaseTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ModifyTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ModifyTrafficDetectionAreaDeviceCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.converters.TrafficDetectionAreaConverter;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.file.TrafficDetectionAreaFileAppService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaDeviceRepository;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrafficDetectionAreaCommandService {

    private final TrafficDetectionAreaRepository trafficDetectionAreaRepository;

    private final TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository;

    private final TrafficDetectionAreaFileAppService trafficDetectionAreaFileAppService;

    public TrafficDetectionAreaCommandService(TrafficDetectionAreaRepository trafficDetectionAreaRepository,
                                              TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository,
                                              TrafficDetectionAreaFileAppService trafficDetectionAreaFileAppService
                                             ) {
        this.trafficDetectionAreaRepository = trafficDetectionAreaRepository;
        this.trafficDetectionAreaDeviceRepository = trafficDetectionAreaDeviceRepository;
        this.trafficDetectionAreaFileAppService = trafficDetectionAreaFileAppService;
    }

    public void increaseTrafficDetectionArea(IncreaseTrafficDetectionAreaCommand command) {
        var trafficDetectionArea = TrafficDetectionAreaConverter.toTrafficDetectionArea(command);
        trafficDetectionAreaRepository.save(trafficDetectionArea);
        trafficDetectionAreaFileAppService.modifyTrafficDetectionAreaFiles(trafficDetectionArea.getId(), command.getFiles());
    }

    public void changeTrafficDetectionArea(ChangeTrafficDetectionAreaCommand command) {
        var trafficDetectionArea = trafficDetectionAreaRepository.findById(command.getId());
        TrafficDetectionAreaConverter.updateTrafficDetectionArea(trafficDetectionArea, command);
        trafficDetectionAreaRepository.save(trafficDetectionArea);
        trafficDetectionAreaFileAppService.modifyTrafficDetectionAreaFiles(trafficDetectionArea.getId(), command.getFiles());
    }

    public void modifyTrafficDetectionAreaDevices(Long id, ModifyTrafficDetectionAreaCommand command) {
        var trafficDetectionArea = trafficDetectionAreaRepository.findById(id);
        trafficDetectionArea.changeDevices(TrafficDetectionAreaConverter.toTrafficDetectionAreaDevices(command));
        trafficDetectionAreaRepository.updateDevices(trafficDetectionArea);
    }

    public void modifyTrafficDetectionAreaDevice(ModifyTrafficDetectionAreaDeviceCommand command) {
        var trafficDetectionAreaDevice = TrafficDetectionAreaConverter.toTrafficDetectionAreaDevice(command);
        trafficDetectionAreaDeviceRepository.update(command.getId(), trafficDetectionAreaDevice);
    }

    public void delTrafficDetectionArea(DelTrafficDetectionAreaCommand command) {
        trafficDetectionAreaRepository.deleteByIds(command.getIds());
    }
}
