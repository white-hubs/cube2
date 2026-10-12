package com.guhecloud.cube.biz.mdata.server.grid.service;

import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaTodoNumberParam;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaTodoNumberVO;

public interface GridAreaTodoService {
    GridAreaTodoNumberVO getGridAreaTodoNum(GridAreaTodoNumberParam param);
}
