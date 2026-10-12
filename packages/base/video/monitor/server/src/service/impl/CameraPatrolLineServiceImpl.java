package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.vmonitor.server.dao.CameraPatrolLineMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.CameraPatrolLine;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolDeviceService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolLineService;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolPersonService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourceConflictException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 视频巡逻路线服务实现
 *
 * @author lanchun
 * @date 2021/12/6 19:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolLineServiceImpl extends ServiceImpl<CameraPatrolLineMapper, CameraPatrolLine> implements CameraPatrolLineService {

    private final CameraPatrolDeviceService cameraPatrolDeviceService;

    private final CameraPatrolPersonService cameraPatrolPersonService;

    private final UserClient userClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull CameraPatrolLineModifyParam param) {
        CameraPatrolLine cameraPatrolLine = ObjectUtils.copying(param, CameraPatrolLine.class);
        cameraPatrolLine.setCameraNum(param.getDeviceModifyParams().size());
        this.baseMapper.insert(cameraPatrolLine);
        //新增巡逻人员配置
        this.cameraPatrolPersonService.save(cameraPatrolLine.getId(), param.getPersonModifyParams());
        //新增巡逻点配置
        cameraPatrolDeviceService.save(cameraPatrolLine.getId(), param.getDeviceModifyParams());
        return cameraPatrolLine.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull CameraPatrolLineModifyParam param) {
        CameraPatrolLine cameraPatrolLine = this.baseMapper.selectById(id);
        Assert.isNotFound(cameraPatrolLine, "巡逻线路不存在");
        ObjectUtils.copying(param, cameraPatrolLine);
        cameraPatrolLine.setCameraNum(param.getDeviceModifyParams().size());
        this.baseMapper.updateById(cameraPatrolLine);
        //移除巡逻点配置
        cameraPatrolDeviceService.removeByCameraPatrolLineId(id);
        //新增巡逻点配置
        cameraPatrolDeviceService.save(id, param.getDeviceModifyParams());
        //移除巡逻人员配置
        cameraPatrolPersonService.removeByCameraPatrolLineId(id);
        //新增巡逻人员配置
        cameraPatrolPersonService.save(id, param.getPersonModifyParams());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastPatrol(@NonNull Long id, @NonNull LocalDateTime patrolTime) {
        this.baseMapper.updateLastPatrol(id, patrolTime);
    }

    @Override
    public void updateEnabled(@NonNull Long id, @NonNull Boolean enabled) {
        CameraPatrolLine cameraPatrolLine = this.getById(id);
        Assert.isNotFound(cameraPatrolLine, "巡逻路线不存在或已被删除");
        cameraPatrolLine.setEnabled(enabled);
        this.baseMapper.updateById(cameraPatrolLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        CameraPatrolLine cameraPatrolLine = this.getById(id);
        if (Objects.nonNull(cameraPatrolLine) && cameraPatrolLine.getEnabled()) {
            throw new ResourceConflictException("巡逻路线正在使用中");
        }
        DatabaseContextHolder.softDelete(this, id);
        cameraPatrolDeviceService.removeByCameraPatrolLineId(id);
        cameraPatrolPersonService.removeByCameraPatrolLineId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(@NonNull List<Long> ids) {
        ids.stream().forEach(id -> this.remove(id));
    }

    @Override
    public CameraPatrolLine getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Page<CameraPatrolLine> page(CameraPatrolLineQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), CameraPatrolLine::getName, param.getKeyword())
                .eq(Objects.nonNull(param.getEnabled()), CameraPatrolLine::getEnabled, param.getEnabled())
                .orderByDesc(CameraPatrolLine::getCreateTime)
                .page(PagingUtils.convert(param));
    }

    @Override
    public Page<CameraPatrolLine> listAuthCameraPatrolLines(CameraPatrolLineQueryParam param) {
        //获取用户信息
        UserDTO userDTO = this.userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        List<String> authIds = new ArrayList<>();
        authIds.add(SecurityContextHolder.getPrincipal().getId().toString());
        if (!CollectionUtils.isEmpty(userDTO.getOrganizations())) {
            //部门信息
            authIds.addAll(userDTO.getOrganizations().stream().map(organization -> organization.getId().toString())
                                   .collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(userDTO.getRoles())) {
            //角色信息
            authIds.addAll(userDTO.getRoles().stream().map(role -> role.getId().toString()).collect(Collectors.toList()));
        }
        param.setAuthIds(authIds);
        return this.baseMapper.selectAuthCameraPatrolLinePage(PagingUtils.convert(param), param);
    }

    @Override
    public List<CameraPatrolLine> getList(CameraPatrolLineQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), CameraPatrolLine::getName, param.getKeyword())
                .orderByDesc(CameraPatrolLine::getCreateTime)
                .list();
    }
}