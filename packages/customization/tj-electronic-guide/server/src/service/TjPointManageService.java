package com.guhecloud.cube.srv.tj.electronic.guide.service;

import com.guhecloud.cube.srv.tj.electronic.guide.param.PointManagePageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.PointManageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.PointManageDetailsVO;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.PointManagePageVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  11:50
 * @Description:
 * @Version: 1.0
 */
public interface TjPointManageService {
    PointManageDetailsVO getPointManageById(Long id);

    Pagination<PointManagePageVO> getPointManageByPage(PointManagePageParam param);

    Boolean addPointManage(@RequestBody PointManageParam param);

    Boolean updatePointManage(@RequestBody PointManageParam param);

    Boolean deletePointManage(Long id);
}
