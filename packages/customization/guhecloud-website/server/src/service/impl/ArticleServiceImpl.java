package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.SysArticleConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysArticleCategoryMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysArticleMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticle;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticleCategory;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.ArticleService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文章Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleServiceImpl extends ServiceImpl<SysArticleMapper, SysArticle>
        implements ArticleService {

    private final SysArticleMapper articleMapper;
    private final SysArticleCategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysArticleParam param) {
        SysArticle entity = SysArticleConverts.toEntity(param);
        entity.setViewCount(0);
        articleMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, SysArticleParam param) {
        SysArticle entity = articleMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("文章不存在");
        }
        SysArticleConverts.applyToEntity(param, entity);
        entity.setId(id);
        articleMapper.updateById(entity);
    }

    @Override
    public Pagination<SysArticleVO> page(SysArticleQueryParam param) {
        LambdaQueryWrapper<SysArticle> wrapper = new LambdaQueryWrapper<>();

        if (ObjectUtils.isNotEmpty(param.getCategoryId())) {
            wrapper.eq(SysArticle::getCategoryId, param.getCategoryId());
        }

        if (ObjectUtils.isNotEmpty(param.getStatus())) {
            wrapper.eq(SysArticle::getStatus, param.getStatus());
        }

        if (ObjectUtils.isNotEmpty(param.getTitle())) {
            wrapper.like(SysArticle::getTitle, param.getTitle());
        }

        wrapper.orderByDesc(SysArticle::getCreateTime);
        Page<SysArticle> result = articleMapper.selectPage(PagingUtils.convert(param), wrapper);
        Pagination<SysArticleVO> pageData = PagingUtils.convert(result, SysArticleVO.class);

        List<SysArticleVO> records = pageData.getRecords();
        if (records.isEmpty()) {
            return PagingUtils.convert(result, SysArticleVO.class);
        }

        // 批量查询分类名称
        Set<Long> categoryIds = records.stream()
                .map(SysArticleVO::getCategoryId)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toSet());

        Map<Long, String> categoryNameMap = new HashMap<>();
        if (!categoryIds.isEmpty()) {
            List<SysArticleCategory> categories = categoryMapper.selectBatchIds(categoryIds);
            categoryNameMap = categories.stream()
                    .collect(Collectors.toMap(SysArticleCategory::getId, SysArticleCategory::getCategoryName));
        }
        SysArticleConverts.fillCategoryName(pageData, categoryNameMap);

        return pageData;
    }

    @Override
    public SysArticleVO detail(Long id) {
        SysArticle entity = articleMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }
        SysArticleVO vo = SysArticleConverts.toVO(entity);
        if (entity.getCategoryId() != null) {
            SysArticleCategory category = categoryMapper.selectById(entity.getCategoryId());
            SysArticleConverts.fillCategoryName(vo, category != null ? category.getCategoryName() : null);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        articleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id) {
        SysArticle entity = articleMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("文章不存在");
        }
        entity.setStatus(entity.getStatus() == 1 ? 0 : 1);
        articleMapper.updateById(entity);
    }

}
