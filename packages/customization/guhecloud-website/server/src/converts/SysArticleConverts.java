package com.guhecloud.cube.customized.guhecloudwebsite.converts;

import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticle;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SysArticleConverts {

    private SysArticleConverts() {
    }

    public static SysArticle toEntity(SysArticleParam param) {
        SysArticle entity = new SysArticle();
        applyToEntity(param, entity);
        return entity;
    }

    public static void applyToEntity(SysArticleParam param, SysArticle entity) {
        entity.setTitle(param.getTitle());
        entity.setCategoryId(param.getCategoryId());
        entity.setCoverImageUrl(param.getCoverImageUrl());
        entity.setSummary(param.getSummary());
        entity.setContent(param.getContent());
        entity.setAuthor(param.getAuthor());
        entity.setStatus(param.getStatus());
        entity.setDate(param.getDate());
        entity.setSort(param.getSort());
    }

    public static SysArticleVO toVO(SysArticle entity) {
        SysArticleVO vo = new SysArticleVO();
        vo.setId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setCategoryId(entity.getCategoryId());
        vo.setCoverImageUrl(entity.getCoverImageUrl());
        vo.setSummary(entity.getSummary());
        vo.setContent(entity.getContent());
        vo.setAuthor(entity.getAuthor());
        vo.setStatus(entity.getStatus());
        vo.setViewCount(entity.getViewCount());
        vo.setDate(entity.getDate());
        vo.setSort(entity.getSort());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static void fillCategoryName(Pagination<SysArticleVO> voList, Map<Long, String> categoryNameMap) {
        for (SysArticleVO vo : voList.getRecords()) {
            if (vo.getCategoryId() != null) {
                vo.setCategoryName(categoryNameMap.get(vo.getCategoryId()));
            }
        }
    }

    public static void fillCategoryName(SysArticleVO vo, String categoryName) {
        vo.setCategoryName(categoryName);
    }

    public static List<SysArticleVO> toVOList(List<SysArticle> entities) {
        List<SysArticleVO> result = new ArrayList<>();
        for (SysArticle entity : entities) {
            result.add(toVO(entity));
        }
        return result;
    }
}
