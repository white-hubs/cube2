package com.guhecloud.cube.srv.electron.guide.service;

import com.guhecloud.cube.srv.electron.guide.param.LayerManagePageParam;
import com.guhecloud.cube.srv.electron.guide.param.LayerManageParam;
import com.guhecloud.cube.srv.electron.guide.vo.LayerManageDetailsVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:35
 * @Description:
 * @Version: 1.0
 */
public interface LayerManageService {
    LayerManageDetailsVO getLayerManageById(Long id);

    LayerManageDetailsVO getLayerManageByState();

    LayerManageDetailsVO getLayerManageByAreaId(Long areaId);

    Pagination<LayerManageDetailsVO> getLayerManageByPage(LayerManagePageParam param);

    Boolean addLayerManage(LayerManageParam param);

    Boolean updateLayerManage(LayerManageParam param);

    Boolean updateLayerManageByState(LayerManageParam param);

    Boolean deleteLayerManage(Long id);
}
