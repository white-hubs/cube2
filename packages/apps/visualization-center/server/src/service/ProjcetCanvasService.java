package com.guhecloud.cube.base.visualization.server.service;

import com.guhecloud.cube.base.visualization.server.param.ProjcetCanvasParam;
import com.guhecloud.cube.base.visualization.server.vo.ProjcetCanvasVO;

public interface ProjcetCanvasService {
    /**
     * 通过实例id查询信息
     *
     * @param instanceId 实例id
     * @param projectId  项目id
     *
     * @return 项目数据
     */
    ProjcetCanvasVO queryByProjectId(Long instanceId, Long projectId);

    /**
     * 更新数据
     *
     * @param projectManage 实例对象
     *
     * @return 实例对象
     */
    Boolean update(ProjcetCanvasParam projectManage);
}
