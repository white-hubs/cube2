package com.guhecloud.cube.base.device.center.scene.controller;

import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.scene.param.DeviceSceneQueryParam;
import com.guhecloud.cube.base.device.center.scene.service.DeviceBimSceneService;
import com.guhecloud.cube.base.device.center.scene.vo.DeviceVisualCollectionVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.AreaSceneConfigClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 设备场景接口
 *
 * @author lanchun
 * @date 2021/12/3 11:19
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceBimSceneController {

    private final DeviceBimSceneService deviceBimSceneService;

    private final AreaSceneConfigClient areaSceneConfigClient;

    private final AreaClient areaClient;

    private final OrganizationClient organizationClient;

    /**
     * 设备分页列表查询(根据场景获取)
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/device-bim-scene/devices")
    public Pagination<DeviceVisualCollectionVO> listBimDevices(@Valid DeviceSceneQueryParam param) {
        return deviceBimSceneService.getDevices(param);
    }

    /**
     * 设备主键列表查询(根据场景获取)
     *
     * @param sceneType
     * @param areaId
     *
     * @return
     */
    @GetMapping("/device-bim-scene/device-ids")
    public Set<Long> getBimDeviceIds(@RequestParam(value = "sceneType") Long sceneType,
                                     @RequestParam(required = false, value = "areaId") Long areaId
                                    ) {
        return this.deviceBimSceneService.getDeviceIds(sceneType.toString(), areaId);
    }

    /**
     * 设备code列表查询(根据场景获取)
     *
     * @param sceneType
     * @param areaId
     *
     * @return
     */
    @GetMapping("/device-bim-scene/device-codes")
    public Set<String> getBimDeviceCodes(@RequestParam(value = "sceneType") Long sceneType,
                                         @RequestParam(required = false, value = "areaId") Long areaId
                                        ) {
        return this.deviceBimSceneService.getDeviceCodes(sceneType.toString(), areaId);
    }

    /**
     * 设备详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/device-bim-scene/device/{id}")
    public DeviceVisualCollectionVO getBimDeviceById(@PathVariable("id") Long id) {
        DeviceVisualCollectionVO deviceVisualCollectionVO = this.deviceBimSceneService.getDevice(id);
        Assert.isNotFound(deviceVisualCollectionVO, "设备不存在");
        if (Objects.nonNull(deviceVisualCollectionVO.getArea())) {
            extendAreaInfo(deviceVisualCollectionVO);
        }
        if (Objects.nonNull(deviceVisualCollectionVO.getDepartment())) {
            extendDeptInfo(deviceVisualCollectionVO);
        }
        return deviceVisualCollectionVO;
    }

    private void extendAreaInfo(DeviceVisualCollectionVO deviceVisualCollectionVO) {
        deviceVisualCollectionVO.setAreaBimListVO(areaSceneConfigClient.getBimArea(Long.valueOf(deviceVisualCollectionVO.getArea()
                                                                                                        .getId())));
        AreaDTO area = this.areaClient.getAreaById(deviceVisualCollectionVO.getArea().getId());
        List<Long> areaIds = Arrays.asList(area.getSequence()
                                                   .substring(0, area.getSequence().lastIndexOf("-")).split("-"))
                .stream().map(areaId -> Long.valueOf(areaId))
                .collect(Collectors.toList());
        Map<Long, String> areaIdNameMapping = areaClient.getByIds(areaIds).stream()
                .collect(Collectors.toMap(AreaDTO::getId, AreaDTO::getName));
        List<String> areaNames = areaIds.stream().map(areaId -> {
            if (areaIdNameMapping.containsKey(areaId)) {
                return areaIdNameMapping.get(areaId);
            }
            return "";
        }).collect(Collectors.toList());
        deviceVisualCollectionVO.getArea().setPathName(areaNames.stream().collect(Collectors.joining("-")));
    }

    private void extendDeptInfo(DeviceVisualCollectionVO deviceVisualCollectionVO) {
        OrganizationDTO organizations = this.organizationClient.getOrganizationsById(deviceVisualCollectionVO.getDepartment().getKey());
        if (Objects.nonNull(organizations)) {
            Pair<Long, String> pair = deviceVisualCollectionVO.getDepartment();
            pair.setVal(organizations.getName());
        }
    }
}
