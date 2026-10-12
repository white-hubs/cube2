package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.SysSiteLayoutConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSiteLayoutMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteLayout;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteLayoutAddParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteLayoutUpdateParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSiteLayoutService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteLayoutVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 布局 Service 实现
 */
@Service
@RequiredArgsConstructor
public class SysSiteLayoutServiceImpl implements SysSiteLayoutService {

    private final SysSiteLayoutMapper sysSiteLayoutMapper;

    @Override
    public List<SysSiteLayoutVO> list() {
        List<SysSiteLayout> layouts = sysSiteLayoutMapper.selectList(
                new LambdaQueryWrapper<SysSiteLayout>()
                        .eq(SysSiteLayout::getDeleted, 0)
                        .orderByAsc(SysSiteLayout::getId)
                                                                    );
        return SysSiteLayoutConverts.toVOList(layouts);
    }

    @Override
    public SysSiteLayoutVO detail(Long id) {
        SysSiteLayout layout = sysSiteLayoutMapper.selectOne(
                new LambdaQueryWrapper<SysSiteLayout>()
                        .eq(SysSiteLayout::getId, id)
                        .eq(SysSiteLayout::getDeleted, 0)
                                                            );
        if (layout == null) {
            throw new ServiceException("布局不存在");
        }
        return SysSiteLayoutConverts.toVO(layout);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysSiteLayoutAddParam param) {
        if (sysSiteLayoutMapper.selectCount(
                new LambdaQueryWrapper<SysSiteLayout>()
                        .eq(SysSiteLayout::getLayoutName, param.getLayoutName())
                        .eq(SysSiteLayout::getDeleted, 0)
                                           ) > 0) {
            throw new ServiceException("布局名称已存在");
        }
        SysSiteLayout layout = new SysSiteLayout();
        layout.setLayoutName(param.getLayoutName());
        layout.setConfig(param.getConfig());
        sysSiteLayoutMapper.insert(layout);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysSiteLayoutUpdateParam param) {
        SysSiteLayout layout = sysSiteLayoutMapper.selectById(param.getId());
        if (layout == null) {
            throw new ServiceException("布局不存在");
        }
        if (!layout.getLayoutName().equals(param.getLayoutName())) {
            if (sysSiteLayoutMapper.selectCount(
                    new LambdaQueryWrapper<SysSiteLayout>()
                            .eq(SysSiteLayout::getLayoutName, param.getLayoutName())
                            .ne(SysSiteLayout::getId, param.getId())
                                               ) > 0) {
                throw new ServiceException("布局名称已存在");
            }
        }
        layout.setLayoutName(param.getLayoutName());
        layout.setConfig(param.getConfig());
        sysSiteLayoutMapper.updateById(layout);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        sysSiteLayoutMapper.deleteById(id);
    }
}
