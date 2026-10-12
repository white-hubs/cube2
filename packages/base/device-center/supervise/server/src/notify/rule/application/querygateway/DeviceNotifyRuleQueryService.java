package com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway;

import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.application.querygateway.query.DeviceNotifyRuleQuery;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.domain.entity.DeviceNotifyRule;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.converter.DeviceNotifyRuleEntityTOVOConvert;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.infrastructure.repository.DeviceNotifyRuleRepository;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRulePageVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.notify.rule.interfaces.vo.DeviceNotifyRuleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceNotifyRuleQueryService {

    private final DeviceNotifyRuleRepository deviceNotifyRuleRepository;

    private final DeviceCenterStdClient deviceCenterStdClient;

    public DeviceNotifyRuleQueryService(DeviceNotifyRuleRepository deviceNotifyRuleRepository, DeviceCenterStdClient deviceCenterStdClient
                                       ) {
        this.deviceNotifyRuleRepository = deviceNotifyRuleRepository;
        this.deviceCenterStdClient = deviceCenterStdClient;
    }

    public DeviceNotifyRuleVO getRuleDetailByDeviceId(Long deviceId) {
        var deviceNotifyRule = deviceNotifyRuleRepository.findByDeviceId(deviceId);
        var device = deviceCenterStdClient.getDeviceById(deviceId);
        var deviceNotifyRuleVO = new DeviceNotifyRuleVO();
        if (Objects.isNull(deviceNotifyRule)) {
            deviceNotifyRuleVO = new DeviceNotifyRuleVO();
            deviceNotifyRuleVO.setId(deviceId);
            deviceNotifyRuleVO.setDeviceId(deviceId);
        } else {
            deviceNotifyRuleVO = DeviceNotifyRuleEntityTOVOConvert.deviceNotifyRuleToVO(deviceNotifyRule);
        }
        deviceNotifyRuleVO.setName(device.getBaseArchivesInfo().getName());
        if (Objects.nonNull(device.getProductInfo())) {
            deviceNotifyRuleVO.setProdName(device.getProductInfo().getName());
            deviceNotifyRuleVO.setProductCategoryName(device.getProductInfo()
                                                              .getProductCategory().getName());
        }
        return deviceNotifyRuleVO;
    }

    public Pagination<DeviceNotifyRulePageVO> page(DeviceNotifyRuleQuery query) {
        var qry = new DeviceFilterPageParam();
        qry.setKeyword(query.getKeyword());
        qry.setPageNumber(query.getPageNumber());
        qry.setPageSize(query.getPageSize());
        Pagination<DevicePageInfoVO> pagination = deviceCenterStdClient.deviceInfoPage(qry);
        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return new Pagination<>();
        }
        List<DeviceNotifyRulePageVO> deviceNotifyRulePageVOS = new ArrayList<>();
        pagination.getRecords().forEach(device -> {
            DeviceNotifyRulePageVO deviceNotifyRulePageVO = new DeviceNotifyRulePageVO();
            deviceNotifyRulePageVO.setId(device.getId());
            deviceNotifyRulePageVO.setName(device.getName());
            if (Objects.nonNull(device.getProductInfo())) {
                deviceNotifyRulePageVO.setProductCategoryName(device.getProductInfo()
                                                                      .getProductCategory().getName());
                deviceNotifyRulePageVO.setProdName(device.getProductInfo().getName());
            }
            deviceNotifyRulePageVOS.add(deviceNotifyRulePageVO);
        });
        if (CollectionUtils.isEmpty(deviceNotifyRulePageVOS)) {
            return new Pagination<>();
        }
        Map<Long, DeviceNotifyRule> map = deviceNotifyRuleRepository.findPageByDeviceIds(
                        deviceNotifyRulePageVOS.stream().map(DeviceNotifyRulePageVO::getId).collect(Collectors.toSet())).stream()
                .collect(Collectors.toMap(DeviceNotifyRule::getDeviceId, v -> v));
        deviceNotifyRulePageVOS.forEach(deviceRule -> {
            DeviceNotifyRule deviceNotifyRule = map.get(deviceRule.getId());
            if (Objects.nonNull(deviceNotifyRule)) {
                deviceRule.setEnable(deviceNotifyRule.getEnabled());
            }
        });
        return new Pagination<>(pagination.getTotal(), pagination.getPages(), deviceNotifyRulePageVOS);
    }
}
