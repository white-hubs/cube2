package com.guhecloud.cube.deliver.project.merchant.job;

import com.guhecloud.cube.deliver.project.merchant.entity.Device;
import com.guhecloud.cube.deliver.project.merchant.service.DeviceService;
import com.guhecloud.cube.deliver.project.merchant.service.DeviceTypeService;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantAreaService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 档位分值（安全生产分值）
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoreScoreJob {

    private final DeviceService deviceService;
    private final DeviceTypeService deviceTypeService;
    private final MerchantAreaService merchantAreaService;
//    private final SafetyEventApi safetyEventApi;

    /**
     * 初始化档位每年的分数
     * @param param
     * @return
     * @throws Exception
     */
//    @JobAnnotation(name = "initStoreScoreEveryYear", cron = "0 0 0,8 * * ?", desc = "initStoreScoreEveryYear")
    public void initStoreScoreEveryYear(String param) throws Exception {
        try {
            // 先删除所有离线的设备的统计，再重新统计（因为在线状态有变化）
//            safetyEventApi.remove("", SafetyEventSourceEnum.DEVICE_OFF_LINE.getCode());
            List<String> deviceTypeIds = this.getDeviceTypeIds();
            // 摄像机类型和关联物联网的设备有在线离线状态
            List<Device> devices = deviceService.lambdaQuery()
                    .in(Device::getTypeId, deviceTypeIds)
                    .or()
                    .ne(Device::getIotDeviceId, "")
                    .list();
            devices.parallelStream().collect(Collectors.groupingBy(Device::getOnline, Collectors.toList()))
                    .forEach((online, list) -> {
                        if (online == 0) {
                            this.saveSafetyEvent(list);
                        }
                    });
        } catch (Exception e) {
            log.error("统计离线设备失败", e);
        }
    }

    // 获取所有摄像机类型的IDS
    private List<String> getDeviceTypeIds() {
        List<String> typeIds = new ArrayList<>();
//        typeIds.add(deviceTypeService.lambdaQuery().eq(DeviceType::getCode, DeviceTypeService.CODE_INFRARED_CAMERA).one().getId());
//        typeIds.add(deviceTypeService.lambdaQuery().eq(DeviceType::getCode, DeviceTypeService.CODE_COM_CAMERA).one().getId());
//        typeIds.add(deviceTypeService.lambdaQuery().eq(DeviceType::getCode, DeviceTypeService.CODE_THERMAL_CAMERA).one().getId());
        return typeIds;
    }

    private void saveSafetyEvent(List<Device> devices) {
        devices.forEach(device -> {
            String areaNames = String.join("/", merchantAreaService.parseNames(Arrays.asList(device.getAreaId1(), device.getAreaId2(), device.getAreaId3())));
//            SafetyEventDTO dto = new SafetyEventDTO();
//            dto.setId(device.getId());
//            dto.setProjectId(device.getProjectId());
//            dto.setCreateTime(LocalDateTime.now());
//            dto.setSource(SafetyEventSourceEnum.DEVICE_OFF_LINE.getCode());
//            dto.setName(areaNames + "-" + device.getName() + "设备离线");
//            safetyEventApi.add(dto);
        });
    }

}
