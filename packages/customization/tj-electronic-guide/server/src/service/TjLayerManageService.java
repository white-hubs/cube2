package com.guhecloud.cube.srv.tj.electronic.guide.service;

import com.guhecloud.cube.srv.tj.electronic.guide.param.LayerManagePageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.LayerManageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.LayerManageDetailsVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:35
 * @Description:
 * @Version: 1.0
 */
public interface TjLayerManageService {
    LayerManageDetailsVO getLayerManageById(Long id);

    LayerManageDetailsVO getLayerManageByState();

    Pagination<LayerManageDetailsVO> getLayerManageByPage(LayerManagePageParam param);

    Boolean addLayerManage(LayerManageParam param);

    Boolean updateLayerManage(LayerManageParam param);

    Boolean updateLayerManageByState(LayerManageParam param);

    Boolean deleteLayerManage(Long id);
}
