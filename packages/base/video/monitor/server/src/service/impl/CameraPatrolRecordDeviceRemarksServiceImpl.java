package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolRecordDeviceRemarksMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolRecordDeviceRemarks;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordDeviceRemarkQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolRecordDeviceRemarksModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolRecordDeviceRemarksService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolRecordDeviceRemarksServiceImpl extends ServiceImpl<CameraPatrolRecordDeviceRemarksMapper, CameraPatrolRecordDeviceRemarks> implements CameraPatrolRecordDeviceRemarksService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull List<CameraPatrolRecordDeviceRemarksModifyParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        List<CameraPatrolRecordDeviceRemarks> patrolRecordDeviceRemarks = ObjectUtils.copying(params, CameraPatrolRecordDeviceRemarks.class);
        this.saveBatch(patrolRecordDeviceRemarks);
    }

    @Override
    public List<CameraPatrolRecordDeviceRemarks> listByCameraPatrolRecordId(@NonNull Long cameraPatrolRecordId) {
        return this.lambdaQuery()
                .eq(CameraPatrolRecordDeviceRemarks::getCameraPatrolRecordId, cameraPatrolRecordId)
                .list();
    }

    @Override
    public Page<CameraPatrolRecordDeviceRemarks> page(CameraPatrolRecordDeviceRemarkQueryParam param) {
        return this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers.<CameraPatrolRecordDeviceRemarks>lambdaQuery()
                .eq(Objects.nonNull(param.getCameraPatrolRecordId()), CameraPatrolRecordDeviceRemarks::getCameraPatrolRecordId, param.getCameraPatrolRecordId())
                .ge(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getBegin()), CameraPatrolRecordDeviceRemarks::getCreateTime, ObjectUtils.ifNull(param.getPeriod(), Period::getBegin))
                .le(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                  .getEnd()), CameraPatrolRecordDeviceRemarks::getCreateTime, ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                .orderByDesc(CameraPatrolRecordDeviceRemarks::getCreateTime));
    }
}
