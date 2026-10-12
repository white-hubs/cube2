package com.guhecloud.cube.srv.tj.electronic.guide.service;

import com.guhecloud.cube.srv.tj.electronic.guide.param.GuideAreaPageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.GuideAreaParam;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.GuideAreaDetailsVO;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.GuideAreaPageVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:37
 * @Description:
 * @Version: 1.0
 */
public interface TjGuideAreaManageService {
    GuideAreaDetailsVO getGuideAreaById(Long id);

    Pagination<GuideAreaPageVO> getGuideAreaByPage(GuideAreaPageParam param);

    Boolean addGuideArea(GuideAreaParam param);

    Boolean updateGuideArea(GuideAreaParam param);

    Boolean deleteGuideArea(Long id);

}
