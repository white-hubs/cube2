package com.guhecloud.cube.biz.vmonitor.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.vmonitor.server.dao.MultipleVideoCollectMapper;
import com.guhecloud.cube.biz.vmonitor.server.entity.MultipleVideoCollect;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.param.MultipleVideoCollectQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.MultipleVideoCollectCameraService;
import com.guhecloud.cube.biz.vmonitor.server.service.MultipleVideoCollectService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Objects;

/**
 * 多路视频服务实现
 *
 * @author lanchun
 * @date 2021/12/6 19:53
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MultipleVideoCollectServiceImpl extends ServiceImpl<MultipleVideoCollectMapper, MultipleVideoCollect> implements MultipleVideoCollectService {

    private final MultipleVideoCollectCameraService multipleVideoCollectCameraService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(MultipleVideoCollectModifyParam param) {
        MultipleVideoCollect multipleVideoCollect = ObjectUtils.copying(param, MultipleVideoCollect.class);
        Long userId = SecurityContextHolder.getPrincipal().getId();
        multipleVideoCollect.setUserId(userId);
        this.baseMapper.insert(multipleVideoCollect);
        //新增摄像机列表
        multipleVideoCollectCameraService.save(multipleVideoCollect.getId(), param.getCameraParams());
        return multipleVideoCollect.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull MultipleVideoCollectModifyParam param) {
        MultipleVideoCollect multipleVideoCollect = this.getById(id);
        Assert.isNotFound(multipleVideoCollect, "多路视频收藏不存在");
        ObjectUtils.copying(param, multipleVideoCollect);
        this.baseMapper.updateById(multipleVideoCollect);
        //先移除之前配置的监控设备
        multipleVideoCollectCameraService.removeByMultipleVideoCollectId(id);
        //新增配置的监控设备
        multipleVideoCollectCameraService.save(id, param.getCameraParams());
    }

    @Override
    public MultipleVideoCollect getById(@NonNull Serializable id) {
        //必须带上用户主键条件
        return this.baseMapper.selectOne(Wrappers.<MultipleVideoCollect>lambdaQuery()
                                                 .eq(MultipleVideoCollect::getId, id)
                                                 .eq(MultipleVideoCollect::getUserId, SecurityContextHolder.getPrincipal().getId()));
    }

    @Override
    public Page<MultipleVideoCollect> page(MultipleVideoCollectQueryParam param) {
        Page<MultipleVideoCollect> page = new Page<>(param.getPageNumber(), param.getPageSize());
        return baseMapper.getPage(page, param, SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        MultipleVideoCollect multipleVideoCollect = this.getById(id);
        if (Objects.nonNull(multipleVideoCollect)) {
            DatabaseContextHolder.softDelete(this, multipleVideoCollect);
            multipleVideoCollectCameraService.removeByMultipleVideoCollectId(id);
        }
    }
}