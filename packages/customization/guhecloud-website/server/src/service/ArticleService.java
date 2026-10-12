package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticle;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 文章Service
 */
public interface ArticleService extends IService<SysArticle> {

    /**
     * 新增文章
     */
    void add(SysArticleParam param);

    /**
     * 编辑文章
     */
    void update(Long id, SysArticleParam param);

    /**
     * 分页查询
     */
    Pagination<SysArticleVO> page(SysArticleQueryParam param);

    /**
     * 详情
     */
    SysArticleVO detail(Long id);

    /**
     * 删除文章
     */
    void delete(Long id);

    /**
     * 启用、禁用
     */
    void toggleStatus(Long id);

}
