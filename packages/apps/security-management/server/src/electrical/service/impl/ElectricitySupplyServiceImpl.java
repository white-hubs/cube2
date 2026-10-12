package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DevicePostByIdSetParam;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.ElectricityNode;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.ElectricitySupplyParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.ElectricityConnectionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.ElectricityNodeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.ElectricitySupplyService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricitySupplyDetailVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElectricitySupplyServiceImpl implements ElectricitySupplyService {
    private final ElectricityConnectionService electricityConnectionService;
    private final ElectricityNodeService electricityNodeService;
    private final DeviceCenterStdClient deviceCenterStdClient;

    /**
     * 保存
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @Override
    public Boolean save(ElectricitySupplyParam param) {
        /* 清除原来的 */
        electricityNodeService.remove(Wrappers.emptyWrapper());
        electricityConnectionService.remove(Wrappers.emptyWrapper());

        /* 插入新的 */
        if (CollectionUtils.isNotEmpty(param.getNodeList())) {
            electricityNodeService.saveBatch(param.covertNodePoList());
        }
        if (CollectionUtils.isNotEmpty(param.getConnectionList())) {
            electricityConnectionService.saveBatch(param.covertConnectionPoList());
        }
        return true;
    }

    /**
     * 详情
     *
     * @return 详情
     */
    @Override
    public ElectricitySupplyDetailVO detail() {
        // 获取节点和连接
        var nodeList = electricityNodeService.lambdaQuery().list();
        var connectionList = electricityConnectionService.lambdaQuery().list();

        var vo = new ElectricitySupplyDetailVO();
        vo.setCovertNodeList(nodeList);
        vo.setCovertConnectionList(connectionList);

        // 获取节点设备信息
        var deviceMap = new HashMap<Long, DeviceBaseArchivesInfoDTO>();
        var deviceIdSet = Optional.ofNullable(nodeList).orElse(CollectionUtils.emptyList()).stream()
                .map(ElectricityNode::getDeviceIds)
                .filter(StringUtils::isNotBlank)
                .flatMap(deviceIds -> Arrays.stream(deviceIds.split(",")))
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(deviceIdSet)) {
            var devicePostByIdSetParam = new DevicePostByIdSetParam();
            devicePostByIdSetParam.setDeviceIdSet(deviceIdSet);
            var deviceBaseList = deviceCenterStdClient.postListByDeviceId(devicePostByIdSetParam);
            Optional.ofNullable(deviceBaseList).orElse(CollectionUtils.emptyList())
                    .forEach(deviceBase -> deviceMap.put(deviceBase.getId(), deviceBase));
            /* 填充 */
            if (CollectionUtils.isNotEmpty(deviceBaseList)) {
                vo.getNodeList().forEach(nodeVO -> {
                    if (StringUtils.isNotBlank(nodeVO.getDeviceIds())) {
                        var deviceList = Arrays.stream(nodeVO.getDeviceIds().split(","))
                                .filter(StringUtils::isNotBlank)
                                .map(Long::valueOf)
                                .filter(Objects::nonNull)
                                .map(deviceMap::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        nodeVO.setDeviceList(deviceList);
                    }
                });
            }
        }

        return vo;
    }
}
