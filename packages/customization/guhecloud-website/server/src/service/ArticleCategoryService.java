package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticleCategory;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleCategoryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysArticleCategoryQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysArticleCategoryVO;

import java.util.List;

public interface ArticleCategoryService extends IService<SysArticleCategory> {

    void add(SysArticleCategoryParam param);

    void update(Long id, SysArticleCategoryParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    List<SysArticleCategoryVO> getTree();

    void toggleStatus(Long id);

}
