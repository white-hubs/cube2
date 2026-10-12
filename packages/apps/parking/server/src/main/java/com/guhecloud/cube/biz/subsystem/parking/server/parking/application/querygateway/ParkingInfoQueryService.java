package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoDevicePO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingInfoPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDockConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoVisual;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoDeviceRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoDockConfigRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.ParkingInfoVisualRepository;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.converters.ParkingInfoPersistenceConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.converters.ParkingInfoConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingInfoQueryService {

    private final ParkingInfoRepository parkingInfoRepository;

    private final ParkingInfoVisualRepository parkingInfoVisualRepository;

    private final ParkingInfoDeviceRepository parkingInfoDeviceRepository;

    private final ParkingInfoDockConfigRepository parkingInfoDockConfigRepository;

    public List<ParkingInfo> getParkingInfo(ParkingInfoQueryParam param) {
        return this.parkingInfoRepository.findList(param.getParkingInfoCodes(),
                                                   Objects.nonNull(param.getSource()) ? param.getSource().name() : null);
    }

    public ParkingInfo getParkingInfoById(Long id) {
        return this.parkingInfoRepository.findById(id);
    }

    public Pagination<ParkingInfo> getParkingInfoPage(ParkingInfoQueryParam param) {
        return this.parkingInfoRepository.findPage(param,
                                                   param.getParkingInfoCodes(),
                                                   Objects.nonNull(param.getSource()) ? param.getSource().name() : null,
                                                   param.getKeyword());
    }

    public Pagination<ParkingInfo> getHasDockConfigParkingPage(ParkingInfoQueryParam param) {
        return this.parkingInfoRepository.findHasDockConfigPage(param,
                                                                Objects.nonNull(param.getSource()) ? param.getSource().name() : null,
                                                                param.getKeyword(),
                                                                null);
    }

    public ParkingInfoVisual getParkingInfoVisualById(Long id) {
        return parkingInfoVisualRepository.findById(id);
    }

    public List<ParkingInfoDevice> getParkingInfoDevicesById(Long id) {
        return parkingInfoDeviceRepository.findListByParkingId(id);
    }

    public ParkingInfoDockConfig getParkingInfoDockConfigById(Long id) {
        return parkingInfoDockConfigRepository.findById(id);
    }

    public List<ParkingInfoVO> getParkingInfoByIds(List<Long> parkingIds) {
        Assert.isParameterError(ListUtils.isEmpty(parkingIds), "车场id不能为空");
        var list = parkingInfoRepository.lambdaQuery().in(ParkingInfoPO::getId, parkingIds).list();
        var parkingDevices = parkingInfoDeviceRepository.list(Wrappers.<ParkingInfoDevicePO>lambdaQuery()
                                                                      .in(!CollectionUtils.isEmpty(parkingIds), ParkingInfoDevicePO::getParkingInfoId, parkingIds));
        var parkingDeviceMap = parkingDevices.stream().collect(Collectors.groupingBy(ParkingInfoDevicePO::getParkingInfoId));
        var infos = list.stream()
                .map(parking -> ParkingInfoPersistenceConverter.toParkingInfo(parking, null, parkingDeviceMap.get(parking.getId())))
                .collect(Collectors.toList());
        return ParkingInfoConverter.toParkingVOList(infos);
    }
}
