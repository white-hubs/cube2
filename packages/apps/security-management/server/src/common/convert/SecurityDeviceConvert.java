package com.guhecloud.cube.srv.gen.securitymanagement.server.common.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 设备转换工具类
 *
 * @author lanchun
 * @date 2025/4/10 19:06
 */
public class SecurityDeviceConvert {

    private SecurityDeviceConvert() {

    }

    public static List<SecurityDevice> convert(List<Long> binDeviceIds, String appCode,
                                               List<DevicePageInfoVO> devicePageInfos
                                              ) {
        return devicePageInfos.stream()
                .filter(device -> !binDeviceIds.contains(device.getId()))
                .map(device -> {
                    SecurityDevice securityDevice = new SecurityDevice();
                    securityDevice.setApplicationCode(appCode);
                    buildBaseInfo(device, securityDevice);
                    buildAreaInfo(device, securityDevice);
                    buildExpandInfo(device, securityDevice);
                    buildProductInfo(device, securityDevice);
                    return securityDevice;
                }).collect(Collectors.toList());
    }

    private static void buildBaseInfo(DevicePageInfoVO device, SecurityDevice securityDevice) {
        Principal principal = SecurityContextHolder.getPrincipal(true);
        String userName = Objects.nonNull(principal) ? principal.getAccount() : "inner";
        String userId = Objects.nonNull(principal) ? principal.getId().toString() : StringUtils.EMPTY_STRING;
        ActorTypeEnum userType = Objects.nonNull(principal) ? ActorTypeEnum.valueOf(principal.getType().name()) : ActorTypeEnum.SYSTEM;
        securityDevice.setId(IdWorker.getId());
        securityDevice.setDeviceName(device.getName());
        securityDevice.setDeviceId(device.getId());
        securityDevice.setCode(device.getCode());
        securityDevice.setThirdPartyId(device.getThirdPartyId());
        EntityAuditor.populateAudit(securityDevice, userName, userId, userType, LocalDateTime.now());
        if (Objects.nonNull(device.getStatus())) {
            securityDevice.setDeviceStatus(device.getStatus().isOnlineState());
            securityDevice.setCommunicationStatus(device.getStatus().isOnlineState() ? "在线" : "离线");
        }
    }

    private static void buildAreaInfo(DevicePageInfoVO device, SecurityDevice securityDevice) {
        if (Objects.nonNull(device.getArea())) {
            securityDevice.setAreaId(device.getArea().getPathId());
            securityDevice.setAreaName(device.getArea().getPathName());
        }
    }

    private static void buildExpandInfo(DevicePageInfoVO device, SecurityDevice securityDevice) {
        securityDevice.setLntAndLat(device.getLntAndLat());
    }

    private static void buildProductInfo(DevicePageInfoVO device, SecurityDevice securityDevice) {
        if (Objects.nonNull(device.getProductInfo())) {
            securityDevice.setBrandName(device.getProductInfo().getBrandName());
            securityDevice.setDeviceProdId(device.getProductInfo().getId());
            securityDevice.setDeviceProdName(device.getProductInfo().getName());
            if (Objects.nonNull(device.getProductInfo().getProductCategory())) {
                securityDevice.setDeviceProdCategoryCode(device.getProductInfo().getProductCategory().getCode());
                securityDevice.setDeviceProdCategoryCodeConcat(device.getProductInfo().getProductCategory().getPathCode());
                securityDevice.setDeviceProdCategoryName(device.getProductInfo().getProductCategory().getName());
            }
        }
    }
}
