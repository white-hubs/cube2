package com.guhecloud.cube.base.visualization.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.visualization.server.dao.InstanceManageMapper;
import com.guhecloud.cube.base.visualization.server.entity.InstanceManage;
import com.guhecloud.cube.base.visualization.server.enumeration.InstanceEnum;
import com.guhecloud.cube.base.visualization.server.param.InstanceManagePageParam;
import com.guhecloud.cube.base.visualization.server.param.InstanceManageParam;
import com.guhecloud.cube.base.visualization.server.service.InstanceManageService;
import com.guhecloud.cube.base.visualization.server.vo.InstanceManageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实例管理服务实现类
 *
 * @author : lijie
 * @date : 2023-8-21
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InstanceManageServiceImpl extends ServiceImpl<InstanceManageMapper, InstanceManage> implements InstanceManageService {
    @Override
    public InstanceManageVO queryById(Long id) {
        InstanceManage instanceManage = this.baseMapper.selectById(id);
        return ConvertUtils.convert(InstanceManageVO.class, instanceManage);
    }

    @Override
    public Pagination<InstanceManageVO> queryInstancePage(InstanceManagePageParam param) {
        Page<InstanceManageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = baseMapper.queryInstancePage(page, param);
        return PagingUtils.convert(page, page.getRecords());
    }

    @Override
    public Boolean insert(InstanceManageParam param) {
        this.isExist(null, param.getInstanceName());
        InstanceManage instanceManage = ConvertUtils.convert(InstanceManage.class, param);
        instanceManage.setStatus(InstanceEnum.ENABLE.name());
        return baseMapper.insert(instanceManage) > 0;
    }

    @Override
    public Boolean update(InstanceManageParam param) {
        this.isExist(param.getId(), param.getInstanceName());
        InstanceManage instanceManage = baseMapper.selectById(param.getId());
        if (ObjectUtils.isNotEmpty(param.getStatus())) {
            instanceManage.setStatus(param.getStatus());
        }
        instanceManage.setInstanceName(param.getInstanceName());
        instanceManage.setProjectId(param.getProjectId());
        return baseMapper.updateById(instanceManage) > 0;
    }

    @Override
    public Boolean deleteByProjectId(Long id) {
        List<InstanceManage> count = baseMapper.selectList(new LambdaQueryWrapper<InstanceManage>()
                                                                   .eq(InstanceManage::getProjectId, id)
                                                                   .eq(InstanceManage::getStatus, InstanceEnum.ENABLE.name()));
        if (ObjectUtils.isNotEmpty(count) && count.size() > 0) {
            throw new ServiceException("请停止实例后再删除");
        }
        count.stream().forEach(ex -> DatabaseContextHolder.softDelete(this, ex.getId()));
        return true;
    }

    private void isExist(Long id, String name) {
        InstanceManage instanceManage = baseMapper.selectOne(new LambdaQueryWrapper<InstanceManage>()
                                                                     .eq(InstanceManage::getInstanceName, name));
        Boolean bool = ObjectUtils.isEmpty(instanceManage) ? true : ObjectUtils.isNotEmpty(id) ?
                                                                    instanceManage.getId().equals(id) : false;
        if (!bool) {
            throw new ServiceException("名称已存在，请重新输入");
        }
    }
}
