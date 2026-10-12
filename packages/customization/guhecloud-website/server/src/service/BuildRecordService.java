package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.guhecloud.cube.customized.guhecloudwebsite.param.BuildRecordQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.BuildRecordVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 构建记录Service
 */
public interface BuildRecordService {

    /**
     * 生成制品（构建）
     */
    void build();

    /**
     * 发布制品
     */
    Boolean publish(Long id);

    /**
     * 删除制品
     */
    Boolean deleteProduct(Long id);

    /**
     * 分页查询构建记录
     */
    Pagination<BuildRecordVO> page(BuildRecordQueryParam param);

    /***/
    void pollBuildTasks();
    void pollDeployTasks();
}
