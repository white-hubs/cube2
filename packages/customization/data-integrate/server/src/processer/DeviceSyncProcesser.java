package com.guhecloud.cube.base.dataintegrate.server.processer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dataintegrate.server.dao.DeviceManageMapper;
import com.guhecloud.cube.base.dataintegrate.server.dao.SafetyAppDeviceMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.DeviceManage;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyAppDevice;
import com.guhecloud.cube.base.dataintegrate.server.enumeration.JobHandlerEnum;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.service.MarketConfigService;
import com.guhecloud.cube.base.dataintegrate.server.util.HttpUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceSyncProcesser extends BaseMarketDataSyncProcesser<DeviceManage> {
    private final DeviceManageMapper deviceManageMapper;
    private final SafetyAppDeviceMapper safetyAppDeviceMapper;

    public DeviceSyncProcesser(MarketConfigService marketConfigService, DeviceManageMapper deviceManageMapper,
                               SafetyAppDeviceMapper safetyAppDeviceMapper
                              ) {
        super(marketConfigService);
        this.deviceManageMapper = deviceManageMapper;
        this.safetyAppDeviceMapper = safetyAppDeviceMapper;
    }

    @Override
    public List<DeviceManage> getSyncData(ContextParam param) {
        try {
            var startTime = System.currentTimeMillis();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("period", this.getQueryPeriod(param));
            log.info("{} 设备采集开始", param.getMarketName());
            var datas = HttpUtils.restGet(param.getFormatUrl(JobHandlerEnum.DEVICE.getDefaultPath()), params, getAuthorization(param), new TypeReference<List<DeviceManage>>() {});
            if (CollectionUtils.isNotEmpty(datas)) {
                log.info("{} 设备采集时间：{}秒 采集数据量：{} 条", param.getMarketName(),
                         (System.currentTimeMillis() - startTime) / 1000, datas.size());
                return datas;
            }

        } catch (Exception e) {
            log.error("设备数据同步异常：", e);
            throw e;
        }
        return Collections.emptyList();
    }

    private String getQueryPeriod(ContextParam param) {
        var lastestData = deviceManageMapper.selectOne(Wrappers.<DeviceManage>lambdaQuery()
                                                               .eq(DeviceManage::getMarketTenant, param.getMarketTenant())
                                                               .orderByDesc(DeviceManage::getUpdateTime)
                                                               .last("limit 0,1"));
        return getPeriod(param, ObjectUtils.isNotNull(lastestData) ? lastestData.getUpdateTime() : null);
    }

    @Override
    public List<DeviceManage> cleanData(List<DeviceManage> dataList, ContextParam param) {
        //不清洗  直接返回
        if (!CollectionUtils.isEmpty(dataList)) {
            dataList.parallelStream().forEach(data -> {
                data.setMarketName(param.getMarketName());
                data.setMarketTenant(param.getMarketTenant());
                data.setTenant(param.getTenant());
            });
        }
        return dataList;
    }

    @Override
    public void storeData(List<DeviceManage> dataList, ContextParam param) {
        var startTime = System.currentTimeMillis();
        if (!CollectionUtils.isEmpty(dataList)) {
            try {
                int batchSize = 500; // 每批500条
                int total = dataList.size();
                for (int i = 0; i < total; i += batchSize) {
                    int end = Math.min(i + batchSize, total);
                    List<DeviceManage> subList = dataList.subList(i, end);
                    deviceManageMapper.insertOrUpdateBatch(subList);
                    //先删除
                    Set<Long> deviceIds = subList.stream().map(DeviceManage::getId).collect(Collectors.toSet());
                    safetyAppDeviceMapper.delete(Wrappers.<SafetyAppDevice>lambdaQuery().in(SafetyAppDevice::getId, deviceIds));
                }
                List<SafetyAppDevice> safetyAppDevices = new ArrayList<>();
                //处理设备应用
                for (DeviceManage deviceManage : dataList) {
                    if (StringUtils.isBlank(deviceManage.getApplicationCode())) {
                        continue;
                    }
                    var appCodes = deviceManage.getApplicationCode().split(",");
                    for (String app : appCodes) {
                        safetyAppDevices.add(new SafetyAppDevice(deviceManage.getId(), app));
                    }
                }
                if (!CollectionUtils.isEmpty(safetyAppDevices)) {
                    for (int i = 0; i < safetyAppDevices.size(); i += batchSize) {
                        int end = Math.min(i + batchSize, safetyAppDevices.size());
                        List<SafetyAppDevice> subList = safetyAppDevices.subList(i, end);
                        safetyAppDeviceMapper.insertOrUpdateBatch(subList);
                    }
                }
            } catch (Exception e) {
                log.error("设备数据插入失败，原因：", e);
                throw e;
            }
        }
        log.info("{} 设备插入时间：{}", param.getMarketName(), (System.currentTimeMillis() - startTime) / 1000);
    }
}
