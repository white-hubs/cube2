package com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway;

import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query.TrafficDetectionAreaQueryParam;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.aggregates.TrafficDetectionArea;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaVisual;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaDeviceRepository;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaRepository;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.TrafficDetectionAreaVisualRepository;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficDetectionAreaQueryService {

    private final TrafficDetectionAreaRepository trafficDetectionAreaRepository;

    private final TrafficDetectionAreaVisualRepository trafficDetectionAreaVisualRepository;

    private final TrafficDetectionAreaDeviceRepository trafficDetectionAreaDeviceRepository;

    public List<TrafficDetectionArea> getTrafficDetectionAreas(TrafficDetectionAreaQueryParam param) {
        return this.trafficDetectionAreaRepository.findList(param.getPeriod(),
                                                            param.getKeyword());
    }

    public TrafficDetectionArea getTrafficDetectionAreaById(Long id) {
        return this.trafficDetectionAreaRepository.findById(id);
    }

    public Pagination<TrafficDetectionArea> getTrafficDetectionAreaPage(TrafficDetectionAreaQueryParam param) {
        return this.trafficDetectionAreaRepository.findPage(param,
                                                            param.getPeriod(),
                                                            param.getKeyword());
    }

    public TrafficDetectionAreaVisual getTrafficDetectionAreaVisualById(Long id) {
        return trafficDetectionAreaVisualRepository.findById(id);
    }

    public List<TrafficDetectionAreaDevice> TrafficDetectionAreaDevicesById(Long id) {
        return trafficDetectionAreaDeviceRepository.findListByTrafficDetectionAreaId(id);
    }

}
