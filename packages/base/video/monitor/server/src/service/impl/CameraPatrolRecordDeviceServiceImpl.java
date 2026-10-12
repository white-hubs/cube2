package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolRecordDeviceMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecordDevice;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceResultEnum;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordDeviceModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordDeviceRemarksModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordDeviceRemarksService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordDeviceService;
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
 * 视频巡逻设备记录服务实现
 *
 * @author lanchun
 * @date 2021/12/6 19:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolRecordDeviceServiceImpl extends ServiceImpl<CameraPatrolRecordDeviceMapper, CameraPatrolRecordDevice> implements CameraPatrolRecordDeviceService {

    private final CameraPatrolRecordDeviceRemarksService cameraPatrolRecordDeviceRemarksService;

    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull Long cameraPatrolRecordId, @NonNull List<CameraPatrolRecordDeviceModifyParam> params) {
        List<CameraPatrolRecordDevice> cameraPatrolRecordDevices = new ArrayList<>();
        List<CameraPatrolRecordDeviceRemarksModifyParam> deviceRemarksModifyParams = new ArrayList<>();
        params.stream().forEach(param -> {
            CameraPatrolRecordDevice cameraPatrolRecordDevice = ObjectUtils.copying(param, CameraPatrolRecordDevice.class);
            cameraPatrolRecordDevice.setId(IdWorker.getId());
            cameraPatrolRecordDevice.setCameraPatrolRecordId(cameraPatrolRecordId);
            if (CameraPatrolDeviceResultEnum.CAMERA_PATROL_ABNORMAL == param.getResult()) {
                cameraPatrolRecordDevice.setAbnormalNum(param.getDeviceRemarksModifyParams().size());
            }
            param.getDeviceRemarksModifyParams().forEach(deviceRemarkModifyParam -> {
                deviceRemarkModifyParam.setCameraPatrolRecordId(cameraPatrolRecordId);
                deviceRemarkModifyParam.setCameraPatrolRecordDeviceId(cameraPatrolRecordDevice.getId());
                deviceRemarkModifyParam.setDeviceId(param.getDeviceId());
                deviceRemarkModifyParam.setDeviceCode(param.getDeviceCode());
                deviceRemarkModifyParam.setDeviceName(param.getDeviceName());
                deviceRemarkModifyParam.setFilePaths(deviceRemarkModifyParam.getFilePathList());
                deviceRemarksModifyParams.add(deviceRemarkModifyParam);
            });
            cameraPatrolRecordDevices.add(cameraPatrolRecordDevice);
        });
        this.saveBatch(cameraPatrolRecordDevices);
        //备注信息
        this.cameraPatrolRecordDeviceRemarksService.save(deviceRemarksModifyParams);
    }

    @Override
    public CameraPatrolRecordDevice getOne(@NonNull Long cameraPatrolRecordId, @NonNull Long deviceId) {
        return this.baseMapper.selectOne(Wrappers.<CameraPatrolRecordDevice>lambdaQuery()
                                                 .eq(CameraPatrolRecordDevice::getCameraPatrolRecordId, cameraPatrolRecordId)
                                                 .eq(CameraPatrolRecordDevice::getDeviceId, deviceId));
    }

    @Override
    public List<CameraPatrolRecordDevice> listCameraPatrolRecordDevices(@NonNull Long cameraPatrolRecordId) {
        return this.lambdaQuery()
                .eq(CameraPatrolRecordDevice::getCameraPatrolRecordId, cameraPatrolRecordId)
                .orderByAsc(CameraPatrolRecordDevice::getSort)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByCameraPatrolRecordId(@NonNull Long cameraPatrolRecordId) {
        List<CameraPatrolRecordDevice> cameraPatrolRecordDevices = this.listCameraPatrolRecordDevices(cameraPatrolRecordId);
        if (!CollectionUtils.isEmpty(cameraPatrolRecordDevices)) {
            DatabaseContextHolder.softDelete(this, cameraPatrolRecordDevices);
        }
    }

    private void extendFile(List<Long> fileIds, Long businessId) {
        fileIds.forEach(fileId -> {
            fileRefClient.save(businessId, fileId);
        });
    }
}