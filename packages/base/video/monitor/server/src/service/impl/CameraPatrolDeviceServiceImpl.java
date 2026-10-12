package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolDeviceMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolDevice;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolDeviceModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolDeviceService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 巡逻监控服务实现
 *
 * @author lanchun
 * @date 2021/12/6 19:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolDeviceServiceImpl extends ServiceImpl<CameraPatrolDeviceMapper, CameraPatrolDevice> implements CameraPatrolDeviceService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull Long cameraPatrolLineId, @NonNull List<CameraPatrolDeviceModifyParam> params) {
        List<CameraPatrolDevice> cameraPatrolDevices = new ArrayList<>();
        params.stream().forEach(cameraPatrolDeviceModifyParam -> {
            CameraPatrolDevice cameraPatrolDevice = ObjectUtils.copying(cameraPatrolDeviceModifyParam, CameraPatrolDevice.class);
            cameraPatrolDevice.setCameraPatrolLineId(cameraPatrolLineId);
            cameraPatrolDevices.add(cameraPatrolDevice);
        });
        this.saveBatch(cameraPatrolDevices);
    }

    @Override
    public List<CameraPatrolDevice> listCameraPatrolDevices(@NonNull Long cameraPatrolLineId) {
        return this.lambdaQuery().eq(CameraPatrolDevice::getCameraPatrolLineId, cameraPatrolLineId).orderByAsc(CameraPatrolDevice::getSort)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByCameraPatrolLineId(@NonNull Long cameraPatrolLineId) {
        List<CameraPatrolDevice> cameraPatrolDevices = this.listCameraPatrolDevices(cameraPatrolLineId);
        if (!CollectionUtils.isEmpty(cameraPatrolDevices)) {
            DatabaseContextHolder.softDelete(this, cameraPatrolDevices);
        }
    }
}