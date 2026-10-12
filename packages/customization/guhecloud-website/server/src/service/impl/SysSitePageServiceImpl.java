package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.SysSitePageConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSitePageMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSitePage;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePageParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePageQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSitePageService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSitePageVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 页面管理Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysSitePageServiceImpl extends ServiceImpl<SysSitePageMapper, SysSitePage>
        implements SysSitePageService {

    private final SysSitePageMapper sitePageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysSitePageParam param) {
        // 校验路由唯一
        checkPageRouteUnique(param.getPageRoute(), null);
        SysSitePage entity = SysSitePageConverts.toEntity(param);
        sitePageMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysSitePageParam param) {
        SysSitePage entity = sitePageMapper.selectById(param.getId());
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("页面不存在，id=" + param.getId());
        }
        // 校验路由唯一（排除自身）
        checkPageRouteUnique(param.getPageRoute(), param.getId());
        entity = SysSitePageConverts.toEntity(param);
        entity.setId(param.getId());
        sitePageMapper.updateById(entity);
    }

    @Override
    public List<SysSitePageVO> list(SysSitePageQueryParam param) {
        LambdaQueryWrapper<SysSitePage> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(param.getMetaTitle())) {
            wrapper.like(SysSitePage::getMetaTitle, param.getMetaTitle());
        }
        if (ObjectUtils.isNotEmpty(param.getStatus())) {
            wrapper.eq(SysSitePage::getStatus, param.getStatus());
        }
        wrapper.orderByDesc(SysSitePage::getCreateTime);
        return sitePageMapper.selectList(wrapper).stream()
                .map(SysSitePageConverts::toPageVO)
                .collect(Collectors.toList());
    }

    @Override
    public SysSitePageVO detail(Long id) {
        SysSitePage entity = sitePageMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }
        return SysSitePageConverts.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    /**
     * 校验页面路由唯一性
     *
     * @param pageRoute 页面路由
     * @param excludeId 排除的ID（编辑时排除自身）
     */
    private void checkPageRouteUnique(String pageRoute, Long excludeId) {
        LambdaQueryWrapper<SysSitePage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysSitePage::getPageRoute, pageRoute);
        wrapper.eq(SysSitePage::getDeleted, 0);
        if (excludeId != null) {
            wrapper.ne(SysSitePage::getId, excludeId);
        }
        Long count = sitePageMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new ServiceException("页面路由已存在：" + pageRoute);
        }
    }

}
