package com.guhecloud.cube.base.device.center.scene.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.open.service.DeviceCenterService;
import com.guhecloud.cube.base.device.center.scene.mapper.DeviceSceneMapper;
import com.guhecloud.cube.base.device.center.scene.param.DeviceSceneQueryParam;
import com.guhecloud.cube.base.device.center.scene.vo.DeviceVisualCollectionVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaListDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 设备场景
 *
 * @author lanchun
 * @date 2021/12/3 10:45
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceBimSceneService {

    private final DeviceSceneMapper deviceSceneMapper;

    private final VisualSceneTagService visualSceneTagService;

    private final DeviceCenterService deviceCenterService;

    private final AreaClient areaClient;

    public DeviceVisualCollectionVO getDevice(Long id) {
        return deviceSceneMapper.getVisualDevice(id);
    }

    public Set<Long> getDeviceIds(@NonNull String sceneType, Long areaId) {
        try {
            Optional<Long> tagIdOptional = this.visualSceneTagService.findTagIdOptional(sceneType);
            if (!tagIdOptional.isPresent()) {
                return Collections.emptySet();
            }
            return this.deviceSceneMapper.getDeviceIds(tagIdOptional.get(), areaId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Collections.emptySet();
    }

    public Pagination<DeviceVisualCollectionVO> getDevices(DeviceSceneQueryParam param) {
        try {
            Optional<Long> tagIdOptional = this.visualSceneTagService.findTagIdOptional(param.getSceneType().toString());
            if (!tagIdOptional.isPresent()) {
                return Pagination.empty();
            }
            Page<DeviceVisualCollectionVO> page = deviceSceneMapper
                    .getVisualDeviceList(PagingUtils.convert(param), param, tagIdOptional.get());
            if (CollectionUtils.isEmpty(page.getRecords())) {
                return Pagination.empty();
            }
            List<AreaListDTO> areas = this.areaClient.listInnerArea(new AreaQueryParam());
            page.getRecords().forEach(device -> deviceCenterService.extendArea(areas, device.getArea()));
            return PagingUtils.convert(page);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Pagination.empty();
    }

    public Set<String> getDeviceCodes(String sceneType, Long areaId) {
        Set<Long> deviceIds = getDeviceIds(sceneType, areaId);
        if (ListUtils.isEmpty(deviceIds)) {
            log.info("未获取到设备code，查询参数 sceneType:{} areaId:{}", sceneType, areaId);
            return Collections.emptySet();
        }
        //根据设备id获取设备code
        return deviceCenterService.deviceCodesByDeviceIds(deviceIds);

    }
}
