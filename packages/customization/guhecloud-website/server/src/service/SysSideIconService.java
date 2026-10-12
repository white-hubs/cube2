package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSideIconParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSideIconQueryParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSideIconVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 侧边图标Service
 */
public interface SysSideIconService {

    /**
     * 新增
     */
    void add(SysSideIconParam param);

    /**
     * 编辑
     */
    void update(Long id, SysSideIconParam param);

    /**
     * 列表查询
     */
    List<SysSideIconVO> list(SysSideIconQueryParam param);

    /**
     * 详情
     */
    SysSideIconVO detail(Long id);

    /**
     * 启用、禁用
     */
    void toggleStatus(Long id, Integer status);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 排序
     */
    void sort(Long id, Integer sort);

    /**
     * 批量启用、禁用
     */
    void batchStatus(List<Long> ids, Integer status);

}
