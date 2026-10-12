package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceAccountCollectCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.dao.DeviceAccountCollectMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceAccountCollectPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceAccountCollectCommandServiceImpl extends ServiceImpl<DeviceAccountCollectMapper, DeviceAccountCollectPO> implements DeviceAccountCollectCommandService {

    /**
     * 收藏
     *
     * @param deviceId 设备ID
     */
    @Override
    public void collect(Long deviceId) {
        DeviceAccountCollectPO po = this.getDeviceAccountCollectPO(deviceId);
        Assert.isNotAcceptable(po.getId() != null, "设备已被收藏");
        po.setTime(LocalDateTime.now());
        this.save(po);
    }

    /**
     * 取消
     *
     * @param deviceId 设备ID
     */
    @Override
    public void cancel(Long deviceId) {
        DeviceAccountCollectPO po = this.getDeviceAccountCollectPO(deviceId);
        Assert.isNotAcceptable(po.getId() == null, "设备未被收藏");
        this.removeById(po.getId());
    }

    private DeviceAccountCollectPO getDeviceAccountCollectPO(Long deviceId) {
        DeviceAccountCollectPO po;
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);
        Long userId = SecurityContextHolder.getPrincipal().getId();

        po = this.lambdaQuery()
                .eq(DeviceAccountCollectPO::getTenant, tenant)
                .eq(DeviceAccountCollectPO::getUserId, userId)
                .eq(DeviceAccountCollectPO::getDeviceId, deviceId)
                .one();
        if (po == null) {
            po = new DeviceAccountCollectPO();
            po.setUserId(SecurityContextHolder.getPrincipal().getId());
            po.setDeviceId(deviceId);
            po.setTenant(SessionContextHolder.getRequesterProperty(Requester::getTenant));
        }

        return po;
    }

}
