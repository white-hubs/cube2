package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticleCategory;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleCategoryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleCategoryVO;

public final class ArticleCategoryConverts {

    private ArticleCategoryConverts() {
    }

    public static SysArticleCategory toEntity(SysArticleCategoryParam param) {
        SysArticleCategory entity = new SysArticleCategory();
        entity.setCategoryName(param.getCategoryName());
        entity.setParentId(param.getParentId());
        entity.setSort(param.getSort());
        entity.setStatus(param.getStatus());
        return entity;
    }

    public static SysArticleCategoryVO toVO(SysArticleCategory entity) {
        SysArticleCategoryVO vo = new SysArticleCategoryVO();
        vo.setId(entity.getId());
        vo.setCategoryName(entity.getCategoryName());
        vo.setParentId(entity.getParentId());
        vo.setSort(entity.getSort());
        vo.setStatus(entity.getStatus());
        vo.setArticleCount(entity.getArticleCount());
        return vo;
    }
}
