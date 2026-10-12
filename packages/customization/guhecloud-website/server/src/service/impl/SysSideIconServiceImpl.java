package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.SysSideIconConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSideIconMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSideIcon;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSideIconParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSideIconQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSideIconService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSideIconVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 侧边图标Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysSideIconServiceImpl extends ServiceImpl<SysSideIconMapper, SysSideIcon>
        implements SysSideIconService {

    private final SysSideIconMapper sideIconMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysSideIconParam param) {
        SysSideIcon entity = SysSideIconConverts.toEntity(param);
        if (ObjectUtils.isEmpty(param.getSort())) {
            entity.setSort(0);
        }
        if (ObjectUtils.isEmpty(param.getStatus())) {
            entity.setStatus(1);
        }
        if (ObjectUtils.isEmpty(param.getIsOpen())) {
            entity.setIsOpen(0);
        }
        sideIconMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, SysSideIconParam param) {
        SysSideIcon entity = sideIconMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("侧边图标不存在");
        }
        entity.setTitle(param.getTitle());
        entity.setIcon(param.getIcon());
        entity.setSummary(param.getSummary());
        entity.setImage(param.getImage());
        entity.setLink(param.getLink());
        entity.setSort(param.getSort());
        entity.setStatus(param.getStatus());
        entity.setIsOpen(param.getIsOpen());
        sideIconMapper.updateById(entity);
    }

    @Override
    public List<SysSideIconVO> list(SysSideIconQueryParam param) {
        LambdaQueryWrapper<SysSideIcon> wrapper = buildQueryWrapper(param);
        wrapper.orderByAsc(SysSideIcon::getSort);
        return sideIconMapper.selectList(wrapper).stream()
                .map(SysSideIconConverts::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public SysSideIconVO detail(Long id) {
        SysSideIcon entity = sideIconMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }
        return SysSideIconConverts.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer status) {
        SysSideIcon entity = sideIconMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("侧边图标不存在");
        }
        entity.setStatus(status);
        sideIconMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        sideIconMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sort(Long id, Integer sort) {
        SysSideIcon entity = sideIconMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("侧边图标不存在");
        }
        entity.setSort(sort);
        sideIconMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchStatus(List<Long> ids, Integer status) {
        if (ObjectUtils.isEmpty(ids)) {
            return;
        }
        ids.forEach(id -> {
            SysSideIcon entity = sideIconMapper.selectById(id);
            if (ObjectUtils.isNotEmpty(entity)) {
                entity.setIsOpen(status);
                sideIconMapper.updateById(entity);
            }
        });
    }

    private LambdaQueryWrapper<SysSideIcon> buildQueryWrapper(SysSideIconQueryParam param) {
        LambdaQueryWrapper<SysSideIcon> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(param.getTitle())) {
            wrapper.like(SysSideIcon::getTitle, param.getTitle());
        }
        if (ObjectUtils.isNotNull(param.getStatus())) {
            wrapper.eq(SysSideIcon::getStatus, param.getStatus());
        }
        if (ObjectUtils.isNotNull(param.getIsOpen())) {
            wrapper.eq(SysSideIcon::getIsOpen, param.getIsOpen());
        }
        return wrapper;
    }

}
