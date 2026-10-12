package com.guhecloud.cube.biz.mdata.server.grid.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridModelConfig;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridModelConfigVO;

/**
 * 网格模型全局配置表 Service
 */
public interface GridModelConfigService extends IService<GridModelConfig> {
    /**
     * 获取网格模型全局配置
     *
     * @return 网格模型全局配置
     */
    GridModelConfigVO getGridModelConfig();

    /**
     * 设置网格模型全局配置
     *
     * @param modelItemId 模型项目id
     */
    void setGridModelConfig(Long modelItemId);
}

