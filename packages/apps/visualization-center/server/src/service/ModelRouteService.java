package com.guhecloud.cube.base.visualization.server.service;

import com.guhecloud.cube.base.visualization.server.param.ModelRouteParam;
import com.guhecloud.cube.base.visualization.server.vo.ModelRouteVO;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-08-29  14:45
 * @Description:
 * @Version: 1.0
 */
public interface ModelRouteService {
    List<ModelRouteVO> queryAll(Long modelId, String name);

    Boolean insertAreaAll(Long modelId);

    Boolean insertList(Long modelId, List<ModelRouteParam> param);

    Boolean insert(ModelRouteParam param);

    Boolean update(ModelRouteParam param);

    Boolean deleteById(Long id);
}
