package com.guhecloud.cube.base.visualization.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.visualization.server.entity.ModelViewConfig;
import com.guhecloud.cube.base.visualization.server.param.ModelViewConfigParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.vo.ModelViewConfigVO;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 18:35
 * @Description:
 */
public interface ModelViewConfigService extends IService<ModelViewConfig> {

    /**
     * 通过项目id查询视角配置
     *
     * @param param
     *
     * @return
     */
    List<ModelViewConfigVO> queryModelViewConfigByProjectId(VisualizationBaseQueryParam param);

    /**
     * 新增或更新视角配置
     *
     * @param modelViewConfigParam
     *
     * @return
     */
    Boolean saveOrUpdateModelViewConfig(ModelViewConfigParam modelViewConfigParam);
}
