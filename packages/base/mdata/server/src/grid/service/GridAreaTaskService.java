package com.guhecloud.cube.biz.mdata.server.grid.service;

import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaTaskPageParam;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridCodeNameStringStringVO;
import com.guhecloud.cube.biz.work.center.client.vo.WorkHomePageVO;
import com.guhecloud.cube.biz.work.center.client.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface GridAreaTaskService {
    List<GridCodeNameStringStringVO> getGridAreaBusinessSelect();

    Pagination<WorkTaskPageVO> getGridAreaTaskPage(GridAreaTaskPageParam param);

    WorkHomePageVO getGridAreaTaskNum(GridAreaTaskPageParam param);
}
