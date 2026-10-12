package com.guhecloud.cube.srv.electron.guide.service;

import com.guhecloud.cube.srv.electron.guide.param.PointManagePageParam;
import com.guhecloud.cube.srv.electron.guide.param.PointManageParam;
import com.guhecloud.cube.srv.electron.guide.vo.PointManageDetailsVO;
import com.guhecloud.cube.srv.electron.guide.vo.PointManagePageVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  11:50
 * @Description:
 * @Version: 1.0
 */
public interface PointManageService {
    PointManageDetailsVO getPointManageById(Long id);

    Pagination<PointManagePageVO> getPointManageByPage(PointManagePageParam param);

    Long getPointManageByTypeId(Long typeId);

    Boolean addPointManage(@RequestBody PointManageParam param);

    Boolean updatePointManage(@RequestBody PointManageParam param);

    Boolean deletePointManage(Long id);
}
