package com.guhecloud.cube.srv.electron.guide.service;

import com.guhecloud.cube.srv.electron.guide.param.GuideAreaPageParam;
import com.guhecloud.cube.srv.electron.guide.param.GuideAreaParam;
import com.guhecloud.cube.srv.electron.guide.param.GuideAreaQueryParam;
import com.guhecloud.cube.srv.electron.guide.vo.GuideAreaDetailsVO;
import com.guhecloud.cube.srv.electron.guide.vo.GuideAreaPageVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:37
 * @Description:
 * @Version: 1.0
 */
public interface GuideAreaManageService {
     GuideAreaDetailsVO getGuideAreaById(Long id);

     Pagination<GuideAreaPageVO> getGuideAreaByPage(GuideAreaPageParam param) ;

     List<GuideAreaPageVO> getGuideAreas(GuideAreaQueryParam param);

     Boolean addGuideArea(GuideAreaParam param);

     Boolean updateGuideArea(GuideAreaParam param);
     Boolean deleteGuideArea(Long id);

}
