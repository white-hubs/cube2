package com.guhecloud.cube.base.visualization.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.visualization.server.dao.ProjcetCanvasMapper;
import com.guhecloud.cube.base.visualization.server.dao.ProjectManageMapper;
import com.guhecloud.cube.base.visualization.server.entity.ProjcetCanvas;
import com.guhecloud.cube.base.visualization.server.entity.ProjectManage;
import com.guhecloud.cube.base.visualization.server.enumeration.InstanceEnum;
import com.guhecloud.cube.base.visualization.server.param.ProjcetCanvasParam;
import com.guhecloud.cube.base.visualization.server.service.InstanceManageService;
import com.guhecloud.cube.base.visualization.server.service.ProjcetCanvasService;
import com.guhecloud.cube.base.visualization.server.vo.InstanceManageVO;
import com.guhecloud.cube.base.visualization.server.vo.ProjcetCanvasVO;
import com.guhecloud.cube.base.visualization.server.vo.ProjectManageVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目数据源服务实现类
 *
 * @author : lijie
 * @date : 2023-8-22
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjcetCanvasServiceImpl extends ServiceImpl<ProjcetCanvasMapper, ProjcetCanvas> implements ProjcetCanvasService {
    private final ProjectManageMapper projectManageMapper;
    private final InstanceManageService instanceManageService;

    @Override
    public ProjcetCanvasVO queryByProjectId(Long instanceId, Long projectId) {
        InstanceManageVO instanceManage = new InstanceManageVO();
        if (ObjectUtils.isNotEmpty(instanceId)) {
            instanceManage = instanceManageService.queryById(instanceId);
            Assert.parameterIsNull(instanceManage, "实例不存在");
            Assert.isNotAcceptable(InstanceEnum.DISABLE.name().equals(instanceManage.getStatus()), "当前实例的地址不可访问");
            projectId = instanceManage.getProjectId();
        }
        Assert.parameterIsNull(projectId, "项目id不能为空");
        ProjectManage projectManage = projectManageMapper.selectById(projectId);
        Assert.parameterIsNull(projectManage, "项目不存在");
        ProjcetCanvas canvas = baseMapper.selectOne(new LambdaQueryWrapper<ProjcetCanvas>()
                                                            .eq(ProjcetCanvas::getProjectId, projectId));
        ProjcetCanvasVO projcetCanvas = new ProjcetCanvasVO();
        projcetCanvas.setProjectData(ConvertUtils.convert(ProjectManageVO.class, projectManage));
        projcetCanvas.setInstanceData(instanceManage);
        projcetCanvas.setCanvasJson(ObjectUtils.isNotEmpty(canvas) ? canvas.getCanvasJson() : "");
        return projcetCanvas;
    }

    @Override
    public Boolean update(ProjcetCanvasParam canvas) {
        ProjcetCanvas projcetCanvas = baseMapper.selectOne(new LambdaQueryWrapper<ProjcetCanvas>()
                                                                   .eq(ProjcetCanvas::getProjectId, canvas.getProjectId()));
        if (ObjectUtils.isEmpty(projcetCanvas)) {
            projcetCanvas = new ProjcetCanvas();
            projcetCanvas.setCanvasJson(canvas.getCanvasJson());
            projcetCanvas.setProjectId(canvas.getProjectId());
            baseMapper.insert(projcetCanvas);
        } else {
            projcetCanvas.setCanvasJson(canvas.getCanvasJson());
            baseMapper.updateById(projcetCanvas);
        }
        ProjectManage projectManage = projectManageMapper.selectById(canvas.getProjectId());
        projectManage.setThumbnail(canvas.getThumbnail());
        projectManage.setModelIds(canvas.getModelIds());
        return projectManageMapper.updateById(projectManage) > 0;
    }
}
