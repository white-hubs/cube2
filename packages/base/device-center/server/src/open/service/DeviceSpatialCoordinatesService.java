package com.guhecloud.cube.base.device.center.open.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceExpandMapper;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceExpandInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceSpatialCoordinateQueryParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceExpandVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.VIDEO_DEVICE_TAG_CODE;

@Slf4j
@Service
public class DeviceSpatialCoordinatesService {

    private final DeviceCenterMapper deviceCenterMapper;

    private final CenterBizDeviceExpandMapper centerBizDeviceExpandMapper;

    private final DeviceCenterTagService deviceCenterTagService;

    private final DataDictionaryAdapter dataDictionaryAdapter;

    private final DeviceCenterService deviceCenterService;

    private static final String DICTIONARY_ITEM_CODE = "SERVICE_CONFIGURATION";

    private static final String DICTIONARY_DATA_CODE = "DEVICE::SPATIAL_COORDINATES::REF";

    public DeviceSpatialCoordinatesService(DeviceCenterMapper deviceCenterMapper, CenterBizDeviceExpandMapper centerBizDeviceExpandMapper,
                                           DeviceCenterTagService deviceCenterTagService, DataDictionaryAdapter dataDictionaryAdapter,
                                           DeviceCenterService deviceCenterService
                                          ) {
        this.deviceCenterMapper = deviceCenterMapper;
        this.centerBizDeviceExpandMapper = centerBizDeviceExpandMapper;
        this.deviceCenterTagService = deviceCenterTagService;
        this.dataDictionaryAdapter = dataDictionaryAdapter;
        this.deviceCenterService = deviceCenterService;
    }

    /**
     * 获取三维空间关联的摄像机列表
     *
     * @param deviceId
     *
     * @return
     */
    public Set<DeviceBaseArchivesInfoDTO> getAssociatedSpatialCoordinatesVideos(Long deviceId) {
        Long tagId = deviceCenterTagService.findTagId(VIDEO_DEVICE_TAG_CODE);
        Set<Long> rangeDevices = getRangeDevices(deviceId);
        if (CollectionUtils.isEmpty(rangeDevices)) {
            return Collections.emptySet();
        }
        return this.deviceCenterMapper.getVideosByIds(rangeDevices, tagId);
    }

    /**
     * 获取设备三维空间关联的设备分页列表
     *
     * @param param
     *
     * @return
     */
    public Pagination<DevicePageInfoVO> spatialCoordinatesDeviceInfoPage(DeviceSpatialCoordinateQueryParam param) {
        Set<Long> rangeDevices = getRangeDevices(param.getDeviceId());
        if (CollectionUtils.isEmpty(rangeDevices)) {
            return Pagination.empty();
        }
        DeviceFilterPageParam deviceFilterPageParam = new DeviceFilterPageParam();
        deviceFilterPageParam.setMatchedDeviceIds((new ArrayList<>(rangeDevices)));
        deviceFilterPageParam.setPageNumber(param.getPageNumber());
        deviceFilterPageParam.setPageSize(param.getPageSize());
        return deviceCenterService.deviceInfoPage(deviceFilterPageParam);
    }

    public Set<Long> getRangeDevices(Long deviceId) {
        List<DeviceBaseArchivesInfoDTO> devices = this.deviceCenterMapper.getDeviceBaseArchivesInfoListByDeviceId(Collections.singleton(deviceId));
        if (CollectionUtils.isEmpty(devices)) {
            return Collections.emptySet();
        }
        DeviceBaseArchivesInfoDTO deviceBaseArchivesInfoDTO = devices.get(0);
        if (Objects.isNull(deviceBaseArchivesInfoDTO.getArea())) {
            return Collections.emptySet();
        }
        DeviceExpandInfoDTO expand = centerBizDeviceExpandMapper.getExpandByDeviceId(deviceBaseArchivesInfoDTO.getId());
        if (StringUtils.isBlank(expand.getSpatialCoordinates())) {
            return Collections.emptySet();
        }
        //获取同区域设备空间信息
        List<DeviceExpandVO> spatialCoordinatesDevices = this.deviceCenterMapper.getSpatialCoordinatesDevices(deviceBaseArchivesInfoDTO
                                                                                                                      .getArea().getKey());
        Set<Long> rangeDevices = getRangeDevices(expand.getSpatialCoordinates(), spatialCoordinatesDevices);
        if (CollectionUtils.isEmpty(rangeDevices)) {
            return Collections.emptySet();
        }
        return rangeDevices;
    }

    public Set<Long> getRangeDevices(String startSpatialCoordinates, List<DeviceExpandVO> spatialCoordinatesDevices) {
        Double range = getRangeConfig();
        if (Objects.isNull(range)) {
            return Collections.emptySet();
        }
        // 将基准点的空间坐标字符串解析为double数组
        String[] startCoordsArray = getCoordsArray(startSpatialCoordinates);
        if (Objects.isNull(startCoordsArray) || startCoordsArray.length != 3) {
            log.error("Invalid start spatial coordinates format: {}", startSpatialCoordinates);
            return Collections.emptySet();
        }
        double startX = Double.parseDouble(startCoordsArray[0]);
        double startY = Double.parseDouble(startCoordsArray[1]);
        double startZ = Double.parseDouble(startCoordsArray[2]);
        // 存储符合条件的设备ID
        Set<Long> rangeDevices = new HashSet<>();
        // 遍历所有设备的空间坐标，计算距离并筛选
        for (DeviceExpandVO device : spatialCoordinatesDevices) {
            String spatialCoordinates = device.getSpatialCoordinates();
            if (StringUtils.isBlank(spatialCoordinates)) {
                continue;
            }
            String[] coordsArray = getCoordsArray(spatialCoordinates);
            if (Objects.isNull(coordsArray) || coordsArray.length != 3) {
                log.error("Invalid spatial coordinates format for device ID {}: {}", device.getId(), spatialCoordinates);
                continue;
            }
            double x = Double.parseDouble(coordsArray[0]);
            double y = Double.parseDouble(coordsArray[1]);
            double z = Double.parseDouble(coordsArray[2]);
            // 计算欧几里得距离
            double distance = Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2) + Math.pow(z - startZ, 2));
            // 如果距离小于等于10米，则将设备ID添加到结果集合中
            if (distance <= range * 100) {
                rangeDevices.add(device.getId());
            }
        }
        return rangeDevices;
    }

    private String[] getCoordsArray(String spatialCoordinates) {
        try {
            return spatialCoordinates.split(",");
        } catch (Exception e) {
            log.error("parse spatial coordinates error: {}", spatialCoordinates);
        }
        return null;
    }

    private Double getRangeConfig() {
        try {
            DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
            param.setItemCode(DICTIONARY_ITEM_CODE);
            param.setCode(DICTIONARY_DATA_CODE);
            DataDictionaryDTO dataDictionaryDTO = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
            if (Objects.isNull(dataDictionaryDTO) || StringUtils.isBlank(dataDictionaryDTO.getVariable())) {
                return null;
            }
            Map<String, Double> variables = JsonUtils.deserialize(dataDictionaryDTO.getVariable(), new TypeReference<Map<String, Double>>() {});
            if (variables != null) {
                return variables.get("refRange");
            }
        } catch (Exception e) {
            log.error("获取设备空间坐标关联范围配置异常", e);
        }
        return null;
    }
}
