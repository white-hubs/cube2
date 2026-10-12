package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.domain.worker;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.DeviceHealthProblemMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthProblemPO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;

import java.util.Collections;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/1 13:37
 * @description
 */
public class DeviceStatusWorker extends HealthyWorker {

    public DeviceStatusWorker(Long deviceId, Long healthValueId, Integer tenantId, Requester requester, Principal principal) {
        super(deviceId, healthValueId, tenantId, requester, principal);
    }

    private DeviceBaseArchivesInfoDTO.Status status;

    @Override
    protected Integer run() {
        SessionContextHolder.setRequester(requester);
        SecurityContextHolder.setPrincipal(principal);
        SessionContextHolder.requestTenant(tenantId);
        // 获取设备状态
        DeviceCenterStdClient deviceCenterStdClient = ApplicationContextHolder.getBean(DeviceCenterStdClient.class);
        List<DeviceBaseArchivesInfoDTO> byDeviceIds = deviceCenterStdClient.getListByDeviceId(Collections.singleton(deviceId));
        if (ObjectUtils.isNotEmpty(byDeviceIds)) {
            this.status = byDeviceIds.getFirst().getStatus();
            //设备状态目前只有在线离线，不扣分
            return 0;
        }
        return 0;
    }

    @Override
    protected void remind() {
        if (!status.isOnlineState()) {
            DeviceHealthProblemPO deviceHealthProblemPO = new DeviceHealthProblemPO();
            deviceHealthProblemPO.setDeviceHealthValueId(healthValueId);
            deviceHealthProblemPO.setDesc("该设备当前处于离线状态");
            DeviceHealthProblemMapper problemMapper = ApplicationContextHolder.getBean(DeviceHealthProblemMapper.class);
            problemMapper.insert(deviceHealthProblemPO);
        }
    }
}
