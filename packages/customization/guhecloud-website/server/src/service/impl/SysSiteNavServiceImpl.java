package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.SysSiteNavConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSiteNavMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteNav;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteNavParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteNavQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSiteNavService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteNavVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysSiteNavServiceImpl extends ServiceImpl<SysSiteNavMapper, SysSiteNav>
        implements SysSiteNavService {

    private final SysSiteNavMapper siteNavMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysSiteNavParam param) {
        SysSiteNav entity = SysSiteNavConverts.toEntity(param);
        if (ObjectUtils.isEmpty(entity.getParentId())) {
            entity.setParentId(0L);
        }
        if (ObjectUtils.isEmpty(entity.getSort())) {
            entity.setSort(0);
        }
        if (ObjectUtils.isEmpty(entity.getStatus())) {
            entity.setStatus(1);
        }
        siteNavMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, SysSiteNavParam param) {
        SysSiteNav entity = siteNavMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("导航不存在");
        }
        entity.setNavName(param.getNavName());
        entity.setParentId(ObjectUtils.isEmpty(param.getParentId()) ? 0L : param.getParentId());
        entity.setNavIcon(param.getNavIcon());
        entity.setNavType(param.getNavType());
        entity.setNavUrl(param.getNavUrl());
        entity.setSort(param.getSort());
        entity.setOpenType(param.getOpenType());
        entity.setNavPageName(param.getNavPageName());
        siteNavMapper.updateById(entity);
    }

    @Override
    public List<SysSiteNavVO> list(SysSiteNavQueryParam param) {
        LambdaQueryWrapper<SysSiteNav> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(param.getNavName())) {
            wrapper.like(SysSiteNav::getNavName, param.getNavName());
        }
        if (ObjectUtils.isNotEmpty(param.getParentId())) {
            wrapper.eq(SysSiteNav::getParentId, param.getParentId());
        }
        if (ObjectUtils.isNotEmpty(param.getStatus())) {
            wrapper.eq(SysSiteNav::getStatus, param.getStatus());
        }
        wrapper.orderByAsc(SysSiteNav::getSort);
        return siteNavMapper.selectList(wrapper).stream()
                .map(SysSiteNavConverts::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public SysSiteNavVO detail(Long id) {
        SysSiteNav entity = siteNavMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }
        return SysSiteNavConverts.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id, Integer status) {
        SysSiteNav entity = siteNavMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("导航不存在");
        }
        entity.setStatus(status);
        siteNavMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        siteNavMapper.deleteById(id);
    }

}
