package com.guhecloud.cube.base.device.center.open.service;

import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagMapper;
import com.guhecloud.cube.base.device.center.metadata.lang.DeviceInstanceStatusEnum;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreasOrGroupsAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.dto.AreaDeviceNumberDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicesVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.cube.base.device.center.open.mapper.DeviceCenterMapper;
import com.guhecloud.cube.base.device.center.open.param.DeviceCenterStatisticsParam;
import com.guhecloud.cube.base.device.center.open.vo.DeviceStatusVo;
import com.guhecloud.cube.base.device.center.open.vo.SecurityDeviceApplicationCodeStatisticsVO;
import com.guhecloud.cube.base.device.center.open.vo.StatusListVo;
import com.guhecloud.cube.base.device.center.scene.constant.VisualConfigConstants;
import com.guhecloud.cube.base.device.center.scene.mapper.DeviceSceneMapper;
import com.guhecloud.cube.base.device.center.scene.service.VisualSceneTagService;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceStatusTotalData;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCenterStatisticsService {

    private final DeviceCenterService deviceCenterService;

    private final DeviceSceneMapper deviceSceneMapper;

    private final ApplicationClient applicationClient;

    private final CenterBizTagMapper centerBizTagMapper;
    private final DeviceCenterMapper deviceCenterMapper;

    private final VisualSceneTagService visualSceneTagService;

    public Map<String, Object> getDeviceStatistics(DeviceCenterStatisticsParam param) {
        HashMap<String, Object> remap = new HashMap<>();
        List<GenreVO> statisticsGenreVo = new ArrayList<>();
        List<DevicesVO> devicesVo = new ArrayList<>();
        Optional<Long> tagIdOptional = this.visualSceneTagService.findTagIdOptional(param.getSceneType().toString());
        if (!tagIdOptional.isPresent()) {
            remap.put("deviceGenreNums", statisticsGenreVo);
            remap.put("devices", devicesVo);
            remap.put("deviceStatusNums", Collections.emptyList());
            remap.put("deviceNum", 0);
            return remap;
        }
        Long tagId = tagIdOptional.get();
        //设备状态数统计
        DeviceStateCountDTO deviceStateCountVo = deviceSceneMapper.selectNumberOfGroupByStatus(tagId, param.getAreaId());
        if (Objects.isNull(deviceStateCountVo) || 0 == deviceStateCountVo.getTotalNumber()) {
            remap.put("deviceGenreNums", statisticsGenreVo);
            remap.put("devices", devicesVo);
            remap.put("deviceStatusNums", Collections.emptyList());
            remap.put("deviceNum", 0);
            return remap;
        }
        remap.put("deviceNum", deviceStateCountVo.getTotalNumber());
        //获取系统数
        Integer equipmentOverview = deviceSceneMapper.selectEquipmentOverview(tagId, param.getAreaId());
        remap.put("equipmentOverview", equipmentOverview);
        //正常
        List<StatusListVo> statusListVos = this.getStatusListVos(deviceStateCountVo);
        //类型比
        statisticsGenreVo = deviceSceneMapper.selectGenreDevicesNum(tagId, param.getAreaId(), SessionContextHolder.getRequester()
                .getTenant());
        int total = deviceStateCountVo.getTotalNumber();
        for (GenreVO genreVo : statisticsGenreVo) {
            BigDecimal a = new BigDecimal(total);
            BigDecimal bigDecimal = new BigDecimal(genreVo.getNum()).divide(a, 2, RoundingMode.HALF_UP);
            genreVo.setProportion(bigDecimal);
        }
        //设备列表
        devicesVo = deviceSceneMapper.selectDevicesVo(tagId, param.getAreaId(), SessionContextHolder.getRequester().getTenant())
                .stream()
                .peek(deviceVo -> deviceVo.setDeviceStatus(
                        (!ObjectUtils.isEmpty(deviceVo.getDeviceStatus()) && 1 == deviceVo.getDeviceStatusCode()) ?
                        DeviceInstanceStatusEnum.ONLINE.name() : DeviceInstanceStatusEnum.OFFLINE.name()))
                .collect(Collectors.toList());
        remap.put("deviceGenreNums", statisticsGenreVo);
        remap.put("devices", devicesVo);
        remap.put("deviceStatusNums", statusListVos);
        return remap;
    }

    public Map<String, Object> getDeviceStatisticsOverview(DeviceCenterStatisticsParam param) {
        HashMap<String, Object> remap = new HashMap<>();
        DeviceStateCountDTO deviceStateCountVo = deviceCenterService.queryNumberOfGroupByStatus(Collections.emptyList());
        if (Objects.isNull(deviceStateCountVo) || 0 == deviceStateCountVo.getTotalNumber()) {
            return remap;
        }
        remap.put("deviceNum", deviceStateCountVo.getTotalNumber());
        List<StatusListVo> list = getStatusListVos(deviceStateCountVo);
        //类型比
        List<GenreVO> statisticsGenreVo = deviceCenterService.queryGenreDevicesNum(Collections.emptyList());
        int total = deviceStateCountVo.getTotalNumber();
        List<GenreVO> collect = statisticsGenreVo.stream().sorted(Comparator.comparing(GenreVO::getNum).reversed())
                .collect(Collectors.toList());
        List<GenreVO> objects = new ArrayList<>();
        BigDecimal a = new BigDecimal(total);
        Integer b = 0;
        for (int i = 0; i < collect.size(); i++) {
            if (i > 2) {
                b += collect.get(i).getNum();
            } else {
                GenreVO genreVo = new GenreVO();
                BigDecimal bigDecimal = new BigDecimal(collect.get(i).getNum()).divide(a, 2, BigDecimal.ROUND_HALF_UP);
                genreVo.setProportion(bigDecimal);
                genreVo.setGenreName(collect.get(i).getGenreName());
                genreVo.setNum(collect.get(i).getNum());
                objects.add(genreVo);
            }
        }
        if (collect.size() > 3) {
            GenreVO genreVo = new GenreVO();
            genreVo.setProportion(new BigDecimal(b).divide(a, 2, RoundingMode.HALF_UP));
            genreVo.setGenreName("其他");
            genreVo.setNum(b);
            objects.add(genreVo);
        }

        remap.put("deviceGenreNums", objects);
        remap.put("deviceStatusNums", list);
        return remap;

    }

    private List<StatusListVo> getStatusListVos(DeviceStateCountDTO deviceStateCountVo) {
        ArrayList<StatusListVo> list = new ArrayList<>();
        //正常
        StatusListVo statusListVo = new StatusListVo();
        statusListVo.setName("NORMAL");
        statusListVo.setNum(deviceStateCountVo == null ? 0 : deviceStateCountVo.getNormalNumber());
        list.add(statusListVo);
        //离线
        StatusListVo statusListVo1 = new StatusListVo();
        statusListVo1.setName("OFFLINE");
        statusListVo1.setNum(deviceStateCountVo == null ? 0 : deviceStateCountVo.getOfflineNumber());
        list.add(statusListVo1);
        return list;
    }

    public Map<String, Object> getApplicationDeviceStatisticsOverview(DeviceCenterStatisticsParam param) {
        HashMap<String, Object> remap = new HashMap<>();
        DeviceStateCountDTO deviceStateCountVo = deviceCenterService.queryNumberOfGroupByStatus(Collections.emptyList());
        if (Objects.isNull(deviceStateCountVo) || 0 == deviceStateCountVo.getTotalNumber()) {
            return remap;
        }
        remap.put("deviceNum", deviceStateCountVo.getTotalNumber());
        List<StatusListVo> list = getStatusListVos(deviceStateCountVo);
        //应用分组统计集合
        List<SecurityDeviceApplicationCodeStatisticsVO> securityDeviceApplicationCodeStatisticsVOS = centerBizTagMapper.selectSecurityDeviceApplicationStatistics();
        int total = deviceStateCountVo.getTotalNumber();
        List<SecurityDeviceApplicationCodeStatisticsVO> collect = securityDeviceApplicationCodeStatisticsVOS.stream()
                .sorted(Comparator.comparing(SecurityDeviceApplicationCodeStatisticsVO::getNum).reversed()).collect(Collectors.toList());
        List<SecurityDeviceApplicationCodeStatisticsVO> objects = new ArrayList<>();
        BigDecimal a = new BigDecimal(total);
        for (SecurityDeviceApplicationCodeStatisticsVO deviceApplicationCodeStatisticsVO : collect) {
            SecurityDeviceApplicationCodeStatisticsVO securityDeviceApplicationCodeStatisticsVO = new SecurityDeviceApplicationCodeStatisticsVO();
            BigDecimal bigDecimal = new BigDecimal(deviceApplicationCodeStatisticsVO.getNum()).divide(a, 2, RoundingMode.HALF_UP);
            ApplicationDTO application = this.applicationClient.getApplicationByCode(deviceApplicationCodeStatisticsVO.getApplicationCode()
                                                                                             .replace("builtin.app::", ""));
            securityDeviceApplicationCodeStatisticsVO.setApplicationCode(deviceApplicationCodeStatisticsVO.getApplicationCode());
            securityDeviceApplicationCodeStatisticsVO.setProportion(bigDecimal);
            if (Objects.nonNull(application)) {
                securityDeviceApplicationCodeStatisticsVO.setApplicationName(application.getName());
            }
            securityDeviceApplicationCodeStatisticsVO.setNum(deviceApplicationCodeStatisticsVO.getNum());
            objects.add(securityDeviceApplicationCodeStatisticsVO);
        }
        remap.put("deviceApplicationNums", objects);
        remap.put("deviceStatusNums", list);
        return remap;
    }

    public Map<String, Object> getMonitoringSituation() {
        HashMap<String, Object> remap = new HashMap<>();
        List<Long> ids = centerBizTagMapper.getIds("SECURITY_MONITORING");
        if (ids.size() == 0) {
            return remap;
        } else {
            DeviceStateCountDTO deviceStateCountVo = deviceCenterService.queryNumberOfGroupByStatus(ids);
            remap.put("deviceNum", ids.size());
            List<StatusListVo> list = getStatusListVos(deviceStateCountVo);
            //区域
            List<AreaDeviceNumberDTO> deviceAreaNum = deviceCenterService.queryDeviceAreaNum(ids);
            remap.put("deviceStatusNums", list);
            remap.put("deviceAreaNum", deviceAreaNum);
            return remap;
        }
    }

    public List<DeviceStatusVo> getApplicationDeviceStatusNum(DeviceCenterStatisticsParam param) {
        Optional<Long> tagIdOptional = this.visualSceneTagService.findTagIdOptional(param.getSceneType().toString());
        if (tagIdOptional.isEmpty()) {
            return Collections.emptyList();
        }
        List<DeviceStatusVo> applicationDeviceNum = deviceSceneMapper.selectApplicationCodeNum(tagIdOptional.get(), param.getAreaId());
        if (CollectionUtils.isNotEmpty(applicationDeviceNum)) {
            List<String> collect = applicationDeviceNum.stream()
                    .map(deviceStatus -> deviceStatus.getApplicationCode().replace("builtin.app::", "")).collect(Collectors.toList());
            List<ApplicationDTO> applicationsByCodes = applicationClient.getApplicationsByCodes(collect);
            Map<String, ApplicationDTO> collect1 = applicationsByCodes.stream().collect(Collectors.toMap(ApplicationDTO::getCode, s -> s));
            for (DeviceStatusVo deviceStatusVo : applicationDeviceNum) {
                ApplicationDTO applicationDTO = collect1.get(deviceStatusVo.getApplicationCode().replace("builtin.app::", ""));
                if (Objects.nonNull(applicationDTO)) {
                    deviceStatusVo.setApplicationName(applicationDTO.getName());
                }
            }
        }
        return applicationDeviceNum;
    }

    public List<DeviceOfflineCountVO> getDeviceOffline(DeviceCenterStatisticsParam param) {
        Optional<Long> tagIdOptional = this.visualSceneTagService.findTagIdOptional(param.getSceneType().toString());
        return tagIdOptional.map(aLong -> deviceSceneMapper.selectDeviceOffline(aLong, param.getAreaId()).stream()
                .peek(vo -> vo.setProportion(new BigDecimal(vo.getOffLineNum()
                                                                    .toString())
                                                     .divide(new BigDecimal(vo.getNum()
                                                                                    .toString()), 2, RoundingMode.HALF_UP)))
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    /**
     * @Description 设备状态 在线&离线&百分率统计
     * @Author xiaofeng
     * [
     * {
     * "name": "在线",
     * "value": 364,
     * "ringRatio": 90.1
     * },
     * {
     * "name": "离线",
     * "value": 40,
     * "ringRatio": 9.9
     * }
     * ]
     */
    public List<Map<String, Object>> getVisualizationDeviceStatus(DeviceCenterStatisticsParam param) {
        List<Map<String, Object>> deviceStatusMap = new ArrayList<>();
        Map<String, Object> onlineDeviceMap = new HashMap<>();
        Map<String, Object> offlineDeviceMap = new HashMap<>();

        DeviceStatusTotalData statusTotalData = deviceCenterMapper.getStatusTotalData(param.getAreaId()
                , VisualConfigConstants.VISUAL_TAG_PREFIX + "." + param.getSceneType());
        if (!ObjectUtils.isEmpty(statusTotalData) && !ObjectUtils.isEmpty(statusTotalData.getTotalNumber()) &&
            0 != statusTotalData.getTotalNumber()) {

            onlineDeviceMap.put("name", "在线");
            onlineDeviceMap.put("value", ObjectUtils.isEmpty(statusTotalData.getOnlineNumber()) ? 0 : statusTotalData.getOnlineNumber());
            if (!ObjectUtils.isEmpty(statusTotalData.getOnlineNumber()) && 0 != statusTotalData.getOnlineNumber()) {
                onlineDeviceMap.put("ringRatio", new BigDecimal(statusTotalData.getOnlineNumber().toString())
                        .divide(new BigDecimal(statusTotalData.getTotalNumber().toString()), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")) // 转换为百分比
                        .setScale(2, RoundingMode.HALF_UP));
            } else {
                onlineDeviceMap.put("ringRatio", 0);
            }
            deviceStatusMap.add(onlineDeviceMap);

            offlineDeviceMap.put("name", "离线");
            offlineDeviceMap.put("value", ObjectUtils.isEmpty(statusTotalData.getOfflineNumber()) ? 0 : statusTotalData.getOfflineNumber());
            if (!ObjectUtils.isEmpty(statusTotalData.getOfflineNumber()) && 0 != statusTotalData.getOfflineNumber()) {
                offlineDeviceMap.put("ringRatio", new BigDecimal(statusTotalData.getOfflineNumber().toString())
                        .divide(new BigDecimal(statusTotalData.getTotalNumber().toString()), 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100")) // 转换为百分比
                        .setScale(2, RoundingMode.HALF_UP));
            } else {
                onlineDeviceMap.put("ringRatio", 0);
            }
            deviceStatusMap.add(offlineDeviceMap);

        } else {
            onlineDeviceMap.put("name", "在线");
            onlineDeviceMap.put("value", 0);
            onlineDeviceMap.put("ringRatio", 0);
            deviceStatusMap.add(onlineDeviceMap);

            offlineDeviceMap.put("name", "离线");
            offlineDeviceMap.put("value", 0);
            offlineDeviceMap.put("ringRatio", 0);
            deviceStatusMap.add(offlineDeviceMap);
        }
        return deviceStatusMap;
    }

    public Integer deviceCountByAreasOrGroupsAndTag(QueryDeviceByAreasOrGroupsAndTagParam param) {
        return deviceCenterMapper.deviceCountByAreasOrGroupsAndTag(param);
    }
}
