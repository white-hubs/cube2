package com.guhecloud.cube.customized.guhecloudwebsite.service;

import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteLayoutAddParam;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteLayoutUpdateParam;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteLayoutVO;
import java.util.List;

/**
 * 布局 Service
 */
public interface SysSiteLayoutService {

    /**
     * 获取布局列表
     */
    List<SysSiteLayoutVO> list();

    /**
     * 获取布局详情
     */
    SysSiteLayoutVO detail(Long id);

    /**
     * 新增布局
     */
    void add(SysSiteLayoutAddParam param);

    /**
     * 编辑布局
     */
    void update(SysSiteLayoutUpdateParam param);

    /**
     * 删除布局
     */
    void delete(Long id);
}
