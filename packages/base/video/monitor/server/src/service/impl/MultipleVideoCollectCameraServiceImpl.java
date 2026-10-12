package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.vmonitor.server.dao.MultipleVideoCollectCameraMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollectCamera;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectCameraModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.service.MultipleVideoCollectCameraService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 多路视频摄像机服务实现
 *
 * @author lanchun
 * @date 2021/12/6 19:53
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MultipleVideoCollectCameraServiceImpl extends ServiceImpl<MultipleVideoCollectCameraMapper, MultipleVideoCollectCamera> implements MultipleVideoCollectCameraService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull Long multipleVideoCollectId, @NonNull List<MultipleVideoCollectCameraModifyParam> params) {
        List<MultipleVideoCollectCamera> list = new ArrayList<>();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        params.stream().forEach(multipleVideoCollectCameraModifyParam -> {
            MultipleVideoCollectCamera multipleVideoCollectCamera = ObjectUtils
                    .copying(multipleVideoCollectCameraModifyParam, MultipleVideoCollectCamera.class);
            multipleVideoCollectCamera.setMultipleVideoCollectId(multipleVideoCollectId);
            multipleVideoCollectCamera.setUserId(userId);
            multipleVideoCollectCamera.setSource(SessionContextHolder.getRequester().getTerminal());
            list.add(multipleVideoCollectCamera);
        });
        this.saveBatch(list);
    }

    @Override
    public void save(@NonNull MultipleVideoCollectCameraModifyParam param) {
        MultipleVideoCollectCamera multipleVideoCollectCamera = ObjectUtils.copying(param, MultipleVideoCollectCamera.class);
        multipleVideoCollectCamera.setUserId(SecurityContextHolder.getPrincipal().getId());
        multipleVideoCollectCamera.setSource(SessionContextHolder.getRequester().getTerminal());
        this.baseMapper.insert(multipleVideoCollectCamera);
    }

    @Override
    public List<MultipleVideoCollectCamera> listMultipleVideoCollectCameras(@NonNull Long multipleVideoCollectId) {
        return this.baseMapper.selectListByCollectId(SecurityContextHolder.getPrincipal().getId(), multipleVideoCollectId);
    }

    @Override
    public List<MultipleVideoCollectCamera> listUserMultipleVideosBySource(@NonNull Long userId, @NonNull String source) {
        return this.baseMapper.selectListUserMultipleVideosBySource(userId, source);
    }

    @Override
    public void removeByMultipleVideoCollectId(@NonNull Long multipleVideoCollectId) {
        List<MultipleVideoCollectCamera> list = this.listMultipleVideoCollectCameras(multipleVideoCollectId);
        if (!CollectionUtils.isEmpty(list)) {
            DatabaseContextHolder.softDelete(this, list);
        }
    }

    @Override
    public void removeBySource(@NonNull Long userId, @NonNull String source, @NonNull Long deviceId) {
        List<MultipleVideoCollectCamera> multipleVideoCollectCameras = this.baseMapper
                .selectList(Wrappers.<MultipleVideoCollectCamera>lambdaQuery()
                                    .eq(MultipleVideoCollectCamera::getUserId, userId)
                                    .eq(MultipleVideoCollectCamera::getSource, source)
                                    .eq(MultipleVideoCollectCamera::getDeviceId, deviceId));
        if (!CollectionUtils.isEmpty(multipleVideoCollectCameras)) {
            DatabaseContextHolder.softDelete(this, multipleVideoCollectCameras);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByUser(@NonNull Long userId, @NonNull Long deviceId) {
        List<MultipleVideoCollectCamera> multipleVideoCollectCameras = this.baseMapper
                .selectList(Wrappers.<MultipleVideoCollectCamera>lambdaQuery()
                                    .eq(MultipleVideoCollectCamera::getUserId, userId)
                                    .eq(MultipleVideoCollectCamera::getDeviceId, deviceId));
        if (!CollectionUtils.isEmpty(multipleVideoCollectCameras)) {
            DatabaseContextHolder.softDelete(this, multipleVideoCollectCameras);
        }
    }

    @Override
    public void removeByDeviceIds(Set<Long> deviceIds) {
        List<MultipleVideoCollectCamera> multipleVideoCollectCameras = this.baseMapper
                .selectList(Wrappers.<MultipleVideoCollectCamera>lambdaQuery()
                                    .in(MultipleVideoCollectCamera::getDeviceId, deviceIds));
        if (!CollectionUtils.isEmpty(multipleVideoCollectCameras)) {
            DatabaseContextHolder.softDelete(this, multipleVideoCollectCameras);
        }
    }

    @Override
    public void removeByUserIds(Set<Long> userIds) {
        List<MultipleVideoCollectCamera> multipleVideoCollectCameras = this.baseMapper
                .selectList(Wrappers.<MultipleVideoCollectCamera>lambdaQuery()
                                    .in(MultipleVideoCollectCamera::getUserId, userIds));
        if (!CollectionUtils.isEmpty(multipleVideoCollectCameras)) {
            DatabaseContextHolder.softDelete(this, multipleVideoCollectCameras);
        }
    }
}