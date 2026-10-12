package com.guhecloud.cube.base.device.center.statistics.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.client.AlarmStatisticsClient;
import com.guhecloud.cube.base.alarm.client.param.AlarmBusinessKeyStatisticsQueryParam;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.metadata.dto.AreaBaseInfoDTO;
import com.guhecloud.cube.base.device.center.statistics.model.BizDeviceStatistics;
import com.guhecloud.cube.base.device.center.statistics.param.DeviceStatisticsQueryParam;
import com.guhecloud.cube.base.device.center.statistics.service.BizDeviceStatisticsService;
import com.guhecloud.cube.base.device.center.statistics.service.DeviceOperationStatisticsService;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceMaintainStatisticsVO;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceStatisticsNumVO;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceStatisticsVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaListDTO;
import com.guhecloud.cube.biz.mdata.client.param.AreaQueryParam;
import com.guhecloud.cube.srv.gen.maintain.client.DeviceStatisticsClient;
import com.guhecloud.cube.srv.gen.maintain.dto.DeviceInspectStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.DeviceStatisticParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 设备运行统计
 * @date 2024/1/22 10:49
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceOperationStatisticsServiceImpl implements DeviceOperationStatisticsService {

    private final CenterBizDeviceMapper centerBizDeviceMapper;
    private final BizDeviceStatisticsService bizDeviceStatisticsService;
    private final DeviceStatisticsClient deviceStatisticsClient;
    private final AlarmStatisticsClient alarmStatisticsClient;
    private final AreaClient areaClient;

    @Override
    public Pagination<DeviceStatisticsVO> deviceStatistics(DeviceStatisticsQueryParam query) {
        Page<DeviceStatisticsVO> page = centerBizDeviceMapper.deviceStatisticsPage(PagingUtils.convert(query), query);
        setStatisticsInfo(page.getRecords());
        return PagingUtils.convert(page);
    }

    @Override
    public Pagination<DeviceMaintainStatisticsVO> deviceMaintainStatistics(DeviceStatisticsQueryParam query) {
        Page<DeviceStatisticsVO> page = centerBizDeviceMapper.deviceStatisticsPage(PagingUtils.convert(query), query);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<DeviceMaintainStatisticsVO> deviceMaintainStatisticsVOS = buildMaintainStatisticsInfo(page.getRecords());
        return PagingUtils.convert(page, deviceMaintainStatisticsVOS);
    }

    private Integer setDefaultValue(Integer value) {
        return value == null ? 0 : value;
    }

    private Map<Long, DeviceOperationStatisticsDTO> deviceStatisticsListToMap(List<DeviceOperationStatisticsDTO> faultList) {
        if (ObjectUtils.isEmpty(faultList)) {
            return new HashMap<>();
        }
        return faultList.stream()
                .collect(Collectors.toMap(DeviceOperationStatisticsDTO::getDeviceId, s -> s));
    }

    private Map<Long, DeviceInspectStatisticsDTO> deviceInspectStatisticsMap(List<DeviceInspectStatisticsDTO> inspectList) {
        if (ObjectUtils.isEmpty(inspectList)) {
            return new HashMap<>();
        }
        return inspectList.stream()
                .collect(Collectors.toMap(DeviceInspectStatisticsDTO::getDeviceId, s -> s));
    }

    @Override
    public ExcelWriter deviceStatisticsExport(DeviceStatisticsQueryParam query) {
        List<DeviceStatisticsVO> vos = centerBizDeviceMapper.deviceStatistics(query);
        setStatisticsInfo(vos);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("deviceCode", "设备编号");
        writer.addHeaderAlias("deviceName", "设备名称");
        writer.addHeaderAlias("productName", "所属产品");
        writer.addHeaderAlias("productCategoryName", "产品品类");
        writer.addHeaderAlias("onlineState", "当前状态");
        writer.addHeaderAlias("onlineTime", "在线时长（小时）");
        writer.addHeaderAlias("offlineTime", "离线时长（小时）");
        writer.addHeaderAlias("offLineNum", "离线总次数");
        writer.addHeaderAlias("alarmNum", "报警总次数");
        writer.addHeaderAlias("faultNum", "故障总次数");
        writer.addHeaderAlias("faultRate", "设备故障率");
        List<Map<String, Object>> maps = new ArrayList<>();
        vos.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("deviceCode", o.getDeviceCode());
            map.put("deviceName", o.getDeviceName());
            map.put("productName", o.getProductName());
            map.put("productCategoryName", o.getProductCategoryName());
            map.put("onlineState", o.isOnlineState() ? "在线" : "离线");
            map.put("onlineTime", o.getOnlineTime());
            map.put("offlineTime", o.getOfflineTime());
            map.put("offLineNum", o.getOffLineNum());
            map.put("alarmNum", o.getAlarmNum());
            map.put("faultNum", o.getFaultNum());
            map.put("faultRate", o.getFaultRate());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    @Override
    public ExcelWriter deviceMaintainStatisticsExport(DeviceStatisticsQueryParam query) {
        List<DeviceStatisticsVO> vos = centerBizDeviceMapper.deviceStatistics(query);
        List<DeviceMaintainStatisticsVO> maintainStatisticsVOS = buildMaintainStatisticsInfo(vos);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("deviceCode", "设备编号");
        writer.addHeaderAlias("deviceName", "设备名称");
        writer.addHeaderAlias("productName", "所属产品");
        writer.addHeaderAlias("productCategoryName", "产品品类");
        writer.addHeaderAlias("areaName", "所在区域");
        writer.addHeaderAlias("hiddenNum", "隐患总次数");
        writer.addHeaderAlias("hiddenHandlerNum", "已处理隐患数");
        writer.addHeaderAlias("hiddenUnHandlerNum", "待处理隐患数");
        writer.addHeaderAlias("hiddenHandlerRatio", "隐患处理率");
        writer.addHeaderAlias("faultNum", "报修总次数");
        writer.addHeaderAlias("faultHandlerNum", "已处理报修数");
        writer.addHeaderAlias("faultUnHandlerNum", "待处理报修数");
        writer.addHeaderAlias("faultHandlerRatio", "报修处理率");
        writer.addHeaderAlias("inspectNum", "巡检总次数");
        writer.addHeaderAlias("inspectTaskNum", "巡检任务数");
        writer.addHeaderAlias("inspectAbnormalNum", "巡检异常数");
        writer.addHeaderAlias("inspectAbnormalRatio", "巡检异常率");
        writer.addHeaderAlias("upKeepNum", "维保总次数");
        writer.addHeaderAlias("upKeepLastTime", "最近维保时间");
        List<Map<String, Object>> maps = new ArrayList<>();
        maintainStatisticsVOS.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("deviceCode", o.getDeviceCode());
            map.put("deviceName", o.getDeviceName());
            map.put("productName", o.getProductName());
            map.put("productCategoryName", o.getProductCategoryName());
            map.put("areaName", Objects.isNull(o.getArea()) ? null : o.getArea().getPathName());
            map.put("hiddenNum", o.getHiddenDanger().getNum());
            map.put("hiddenHandlerNum", o.getHiddenDanger().getHandlerNum());
            map.put("hiddenUnHandlerNum", o.getHiddenDanger().getUnHandlerNum());
            map.put("hiddenHandlerRatio", o.getHiddenDanger().getHandlerRatio());
            map.put("faultNum", o.getFault().getNum());
            map.put("faultHandlerNum", o.getFault().getHandlerNum());
            map.put("faultUnHandlerNum", o.getFault().getUnHandlerNum());
            map.put("faultHandlerRatio", o.getFault().getHandlerRatio());
            map.put("inspectNum", o.getInspect().getNum());
            map.put("inspectTaskNum", o.getInspect().getTaskNum());
            map.put("inspectAbnormalNum", o.getInspect().getAbnormalNum());
            map.put("inspectAbnormalRatio", o.getInspect().getAbnormalRatio());
            map.put("upKeepNum", o.getUpkeep().getNum());
            map.put("upKeepLastTime", o.getUpkeep().getLastTime());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    @Override
    public List<DeviceStatisticsNumVO> getDevicesNumber(String period) {
        return centerBizDeviceMapper.getDevicesNumber(period);
    }

    /**
     * 设置统计信息
     *
     * @param vos 设备统计信息
     */
    private void setStatisticsInfo(List<DeviceStatisticsVO> vos) {
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }
        Set<Long> deviceIds = vos.stream().map(DeviceStatisticsVO::getId).collect(Collectors.toSet());
        DeviceStatisticParam param = new DeviceStatisticParam();
        param.setDeviceIds(deviceIds);
        Map<Long, DeviceOperationStatisticsDTO> faultMap = deviceStatisticsListToMap(deviceStatisticsClient.deviceFaultNumStatistics(param));
        Map<Long, BizDeviceStatistics> deviceStatisticsMap = bizDeviceStatisticsService.deviceStatistics(deviceIds);
        for (DeviceStatisticsVO vo : vos) {
            if (deviceStatisticsMap.containsKey(vo.getId())) {
                BizDeviceStatistics bizDeviceStatistics = deviceStatisticsMap.get(vo.getId());
                vo.setOnlineTime(bizDeviceStatistics.getOnlineTime());
                vo.setOffLineNum(bizDeviceStatistics.getOfflineNum());
                vo.setOfflineTime(bizDeviceStatistics.getOfflineTime());
            }
            if (faultMap.containsKey(vo.getId())) {
                vo.setFaultNum(setDefaultValue(faultMap.get(vo.getId()).getNum()));
                vo.setFaultRate(faultMap.get(vo.getId()).getRatio());
            }
            AlarmBusinessKeyStatisticsQueryParam alarmParam = new AlarmBusinessKeyStatisticsQueryParam();
            alarmParam.setBusinessKey(String.valueOf(vo.getId()));
            Long alarmNum = alarmStatisticsClient.getBusinessKeyActualTotal(alarmParam);
            vo.setAlarmNum(alarmNum == null ? 0 : alarmNum.intValue());
        }
    }

    /**
     * 设置运维统计信息
     *
     * @param vos
     *
     * @return
     */
    private List<DeviceMaintainStatisticsVO> buildMaintainStatisticsInfo(List<DeviceStatisticsVO> vos) {
        List<AreaListDTO> areas = this.areaClient.listInnerArea(new AreaQueryParam());
        Set<Long> deviceIds = vos.stream().map(DeviceStatisticsVO::getId).collect(Collectors.toSet());
        DeviceStatisticParam param = new DeviceStatisticParam();
        param.setDeviceIds(deviceIds);
        Map<Long, DeviceOperationStatisticsDTO> faultMap = deviceStatisticsListToMap(deviceStatisticsClient.deviceFaultNumStatistics(param));
        Map<Long, DeviceOperationStatisticsDTO> hiddenMap = deviceStatisticsListToMap(deviceStatisticsClient.deviceHiddenNumStatistics(param));
        Map<Long, DeviceInspectStatisticsDTO> inspectMap = deviceInspectStatisticsMap(deviceStatisticsClient.deviceInspectNumStatistics(param));
        Map<Long, DeviceOperationStatisticsDTO> unKeepMap = deviceStatisticsListToMap(deviceStatisticsClient.deviceMaintenanceNumStatistics(param));
        return vos.stream().map(device -> {
            DeviceMaintainStatisticsVO deviceMaintainStatisticsVO = ObjectUtils.copying(device, DeviceMaintainStatisticsVO.class);
            deviceMaintainStatisticsVO.setArea(getArea(areas, device.getAreaId()));
            setFaultStatistics(deviceMaintainStatisticsVO, faultMap);
            setHiddenStatistics(deviceMaintainStatisticsVO, hiddenMap);
            setInspectStatistics(deviceMaintainStatisticsVO, inspectMap);
            setUpKeepStatistics(deviceMaintainStatisticsVO, unKeepMap);
            return deviceMaintainStatisticsVO;
        }).collect(Collectors.toList());
    }

    private void setFaultStatistics(DeviceMaintainStatisticsVO deviceMaintainStatisticsVO, Map<Long, DeviceOperationStatisticsDTO> faultMap
                                   ) {
        if (faultMap.containsKey(deviceMaintainStatisticsVO.getId())) {
            DeviceOperationStatisticsDTO faultStatistics = faultMap.get(deviceMaintainStatisticsVO.getId());
            DeviceMaintainStatisticsVO.MaintainCommonStatistics statistics = new DeviceMaintainStatisticsVO.MaintainCommonStatistics();
            statistics.setNum(faultStatistics.getNum());
            statistics.setHandlerNum(faultStatistics.getHandlerNum());
            statistics.setUnHandlerNum(faultStatistics.getUnHandlerNum());
            statistics.setHandlerRatio(faultStatistics.getRatio());
            deviceMaintainStatisticsVO.setFault(statistics);
        }
    }

    private void setHiddenStatistics(DeviceMaintainStatisticsVO deviceMaintainStatisticsVO,
                                     Map<Long, DeviceOperationStatisticsDTO> hiddenMap
                                    ) {
        if (hiddenMap.containsKey(deviceMaintainStatisticsVO.getId())) {
            DeviceOperationStatisticsDTO hiddenStatistics = hiddenMap.get(deviceMaintainStatisticsVO.getId());
            DeviceMaintainStatisticsVO.MaintainCommonStatistics statistics = new DeviceMaintainStatisticsVO.MaintainCommonStatistics();
            statistics.setNum(hiddenStatistics.getNum());
            statistics.setHandlerNum(hiddenStatistics.getHandlerNum());
            statistics.setUnHandlerNum(hiddenStatistics.getUnHandlerNum());
            statistics.setHandlerRatio(hiddenStatistics.getRatio());
            deviceMaintainStatisticsVO.setHiddenDanger(statistics);
        }
    }

    private void setInspectStatistics(DeviceMaintainStatisticsVO deviceMaintainStatisticsVO,
                                      Map<Long, DeviceInspectStatisticsDTO> inspectMap
                                     ) {
        if (inspectMap.containsKey(deviceMaintainStatisticsVO.getId())) {
            DeviceInspectStatisticsDTO inspectStatistics = inspectMap.get(deviceMaintainStatisticsVO.getId());
            DeviceMaintainStatisticsVO.InspectStatistics statistics = new DeviceMaintainStatisticsVO.InspectStatistics();
            statistics.setNum(inspectStatistics.getNum());
            statistics.setTaskNum(inspectStatistics.getTaskNum());
            statistics.setAbnormalNum(inspectStatistics.getAbnormalNum());
            statistics.setAbnormalRatio(inspectStatistics.getAbnormalRatio());
            deviceMaintainStatisticsVO.setInspect(statistics);
        }
    }

    private void setUpKeepStatistics(DeviceMaintainStatisticsVO deviceMaintainStatisticsVO,
                                     Map<Long, DeviceOperationStatisticsDTO> unKeepMap
                                    ) {
        if (unKeepMap.containsKey(deviceMaintainStatisticsVO.getId())) {
            DeviceOperationStatisticsDTO upKeepStatistics = unKeepMap.get(deviceMaintainStatisticsVO.getId());
            DeviceMaintainStatisticsVO.UpkeepStatistics statistics = new DeviceMaintainStatisticsVO.UpkeepStatistics();
            statistics.setNum(upKeepStatistics.getNum());
            statistics.setLastTime(upKeepStatistics.getLastTime());
            deviceMaintainStatisticsVO.setUpkeep(statistics);
        }
    }

    public AreaBaseInfoDTO getArea(List<AreaListDTO> areas, Long areaId) {
        if (Objects.isNull(areaId)) {
            return null;
        }

        Optional<AreaListDTO> optionalArea = areas.stream()
                .filter(areaListDTO -> areaListDTO.getId().equals(areaId))
                .findFirst();
        if (!optionalArea.isPresent()) {
            return null;
        }
        AreaListDTO deviceArea = optionalArea.get();
        List<Long> areaIds = Arrays.asList(deviceArea.getSequence().split("-")).stream().map(Long::parseLong)
                .collect(Collectors.toList());
        Map<Long, AreaListDTO> areaMap = areas.stream().filter(areaListDTO -> areaIds.contains(areaListDTO.getId()))
                .collect(Collectors.toMap(AreaListDTO::getId, a -> a));
        List<String> areaNames = new ArrayList<>();
        areaIds.forEach(area -> {
            if (areaMap.containsKey(areaId)) {
                AreaListDTO areaListDTO = areaMap.get(areaId);
                areaNames.add(areaListDTO.getName());
            }
        });
        AreaBaseInfoDTO area = new AreaBaseInfoDTO();
        area.setId(areaId);
        area.setName(deviceArea.getName());
        area.setPathId(deviceArea.getSequence());
        area.setPathName(areaNames.stream().collect(Collectors.joining("-")));
        return area;
    }
}
