package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.ArticleCategoryConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysArticleCategoryMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticleCategory;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleCategoryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.ArticleCategoryService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleCategoryVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章分类Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleCategoryServiceImpl extends ServiceImpl<SysArticleCategoryMapper, SysArticleCategory>
        implements ArticleCategoryService {

    private final SysArticleCategoryMapper categoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysArticleCategoryParam param) {
        checkDuplicateName(param.getCategoryName(), param.getParentId(), null);
        SysArticleCategory entity = ArticleCategoryConverts.toEntity(param);
        entity.setArticleCount(0);
        if (ObjectUtils.isEmpty(param.getParentId())) {
            entity.setParentId(0L);
        }
        categoryMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, SysArticleCategoryParam param) {
        SysArticleCategory entity = categoryMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("分类不存在");
        }
        checkDuplicateName(param.getCategoryName(), param.getParentId(), id);
        if (param.getParentId().equals(id)) {
            throw new ServiceException("不能将自己设为上级分类");
        }
        entity.setCategoryName(param.getCategoryName());
        entity.setParentId(param.getParentId());
        categoryMapper.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysArticleCategory entity = categoryMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("分类不存在");
        }
        // 查子分类
        List<SysArticleCategory> children = categoryMapper.selectList(
                new LambdaQueryWrapper<SysArticleCategory>()
                        .eq(SysArticleCategory::getParentId, id)
                                                                     );
        if (ObjectUtils.isNotEmpty(children)) {
            throw new ServiceException("该分类下存在子分类，请先删除子分类");
        }
        categoryMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return;
        }
        ids.forEach(this::delete);
    }

    @Override
    public List<SysArticleCategoryVO> getTree() {
        List<SysArticleCategory> allList = categoryMapper.selectList(
                new LambdaQueryWrapper<SysArticleCategory>()
                        .orderByDesc(SysArticleCategory::getSort));
        return buildTree(allList, 0L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleStatus(Long id) {
        SysArticleCategory entity = categoryMapper.selectById(id);
        if (ObjectUtils.isEmpty(entity)) {
            throw new ServiceException("分类不存在");
        }
        entity.setStatus(entity.getStatus() == 1 ? 0 : 1);
        categoryMapper.updateById(entity);
    }

    private void checkDuplicateName(String categoryName, Long parentId, Long excludeId) {
        LambdaQueryWrapper<SysArticleCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysArticleCategory::getCategoryName, categoryName);
        wrapper.eq(SysArticleCategory::getParentId, parentId);
        wrapper.isNull(SysArticleCategory::getDeleted);
        if (ObjectUtils.isNotEmpty(excludeId)) {
            wrapper.ne(SysArticleCategory::getId, excludeId);
        }
        if (categoryMapper.selectCount(wrapper) > 0) {
            throw new ServiceException("同级分类名称已存在");
        }
    }

    private List<SysArticleCategoryVO> buildTree(List<SysArticleCategory> allList, Long parentId) {
        List<SysArticleCategoryVO> result = new ArrayList<>();
        for (SysArticleCategory entity : allList) {
            if (entity.getParentId().equals(parentId)) {
                SysArticleCategoryVO vo = ArticleCategoryConverts.toVO(entity);
                vo.setChildren(buildTree(allList, entity.getId()));
                result.add(vo);
            }
        }
        return result;
    }

}
