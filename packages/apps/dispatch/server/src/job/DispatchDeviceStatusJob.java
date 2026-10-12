package com.guhecloud.cube.biz.dispatch.server.job;

import com.guhecloud.cube.biz.dispatch.server.dao.DispatchDeviceStatusMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDevice;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGpsHistory;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceStatus;
import com.guhecloud.cube.biz.dispatch.server.map.GaoDeMapApi;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceGpsHistoryService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component("DispatchDeviceStatusJob")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceStatusJob {
    private final DispatchService dispatchService;
    private final DispatchDeviceService dispatchDeviceService;
    private final DispatchDeviceStatusMapper dispatchDeviceStatusMapper;
    private final DispatchDeviceGpsHistoryService dispatchDeviceGpsHistoryService;
    private final GaoDeMapApi gaoDeMapApi;
    private static Boolean runJobFlag = true;

    /**
     * Scheduled 将在线程池中获取线程，为了避免在其他线程中设置了tenant;
     * 必须在在调用相关的接口前，重置tenant或清空tenant
     * 每隔30秒钟 刷新设备状态
     */
    @JobAnnotation(name = "updateDeviceStatus", cron = "0/30 * * * * ?", desc = "指挥调度-定时同步设备状态")
    public void updateDeviceStatus(String param) {
        if (!runJobFlag) {
            return;//对job进行开关
        }
        var start = System.currentTimeMillis();
        log.info("更新调度设备状态开始：{}", Thread.currentThread().getName());
        SessionContextHolder.clearSessionContext();
        //查询所有的终端设备
        var devices = dispatchDeviceService.list();
        var deviceByTenant = devices.stream().collect(Collectors.groupingBy(DispatchDevice::getTenant));
        for (Integer key : deviceByTenant.keySet()) {
            SessionContextHolder.requestTenant(key);
            var devicesInTenant = deviceByTenant.get(key);
            if (!CollectionUtils.isEmpty(devicesInTenant)) {
                var codes = devicesInTenant.stream().map(DispatchDevice::getCode).collect(Collectors.toList());
                var deviceStatusList = dispatchService.deviceStatus(codes);
                if (!CollectionUtils.isEmpty(deviceStatusList)) {
                    var datas = ObjectUtils.copying(deviceStatusList, DispatchDeviceStatus.class);
                    var codeToMap = devicesInTenant.stream()
                            .collect(Collectors.toMap(DispatchDevice::getCode, DispatchDevice::getId, (o, n) -> n));
                    for (var d : datas) {
                        var id = codeToMap.get(d.getCode()) != null ? codeToMap.get(d.getCode()) : d.getCode();
                        d.setId(id);
                        d.setCreateBy("admin");
                        d.setUpdateBy("admin");
                        d.setCreateTime(LocalDateTime.now());
                        d.setUpdateTime(LocalDateTime.now());
                        d.setTenant(key);
                        d.setDeleted(false);
                    }
                    dispatchDeviceStatusMapper.insertOrUpdateDeviceStatusBatch(datas);
                    //历史轨迹
                    addGpsHistory(datas);
                }
                //将没有查询出来的设备统一置为离线
                var offlineDevices = devicesInTenant.stream().map(DispatchDevice::getCode)
                        .filter(code ->
                                        deviceStatusList.stream().filter(ds -> code
                                                        .equals(ds.getCode())).findAny()
                                                .orElse(null) == null)
                        .collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(offlineDevices)) {
                    dispatchDeviceStatusMapper.updateDeviceStatusBatch(false, offlineDevices);
                }
            }
        }
        log.info("更新调度设备状态结束，耗时：{} 毫秒", System.currentTimeMillis() - start);

    }

    public void addGpsHistory(List<DispatchDeviceStatus> datas) {
        List<DispatchDeviceGpsHistory> dispatchDeviceGpsHistories = new ArrayList<>(datas.size());
        Set<String> deviceIds = datas.stream().map(DispatchDeviceStatus::getId).collect(Collectors.toSet());
        Map<String, DispatchDeviceGpsHistory> lastDeviceGpsHistoryMap = this.dispatchDeviceGpsHistoryService
                .getLastGpsHistoryByDeviceIds(deviceIds)
                .stream().collect(Collectors.toMap(DispatchDeviceGpsHistory::getDeviceId, o -> o));
        for (DispatchDeviceStatus d : datas) {
            if (d.getLat() <= 1 || d.getLng() <= 1) {
                continue;
            }
            if (lastDeviceGpsHistoryMap.containsKey(d.getId())) {
                DispatchDeviceGpsHistory lastDeviceGpsHistory = lastDeviceGpsHistoryMap.get(d.getId());
                //过滤掉连续的重复坐标数据(后续替换成计算2个坐标点的直线距离进行过滤)
                if (!d.getLat().equals(lastDeviceGpsHistory.getLat())
                    || !d.getLng().equals(lastDeviceGpsHistory.getLng())) {
                    dispatchDeviceGpsHistories.add(buildDisPatchDeviceGpsHistory(d));
                }
            } else {
                dispatchDeviceGpsHistories.add(buildDisPatchDeviceGpsHistory(d));
            }
        }
        if (!CollectionUtils.isEmpty(dispatchDeviceGpsHistories)) {
            this.dispatchDeviceGpsHistoryService.saveBatch(dispatchDeviceGpsHistories);
        }
    }

    private DispatchDeviceGpsHistory buildDisPatchDeviceGpsHistory(DispatchDeviceStatus d) {
        DispatchDeviceGpsHistory dispatchDeviceGpsHistory = new DispatchDeviceGpsHistory();
        dispatchDeviceGpsHistory.setDeviceId(d.getId());
        dispatchDeviceGpsHistory.setDeviceCode(d.getCode());
        dispatchDeviceGpsHistory.setLat(d.getLat());
        dispatchDeviceGpsHistory.setLng(d.getLng());
        dispatchDeviceGpsHistory.setDirection(d.getDirection());
        dispatchDeviceGpsHistory.setSpeed(d.getSpeed());
        dispatchDeviceGpsHistory.setUploadTime(d.getUploadTime());
        dispatchDeviceGpsHistory.setAddress(gaoDeMapApi.regeoAddress(d.getLng().toString() + "," + d.getLat().toString()));
        return dispatchDeviceGpsHistory;
    }
}
