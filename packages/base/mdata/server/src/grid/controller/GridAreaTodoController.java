package com.guhecloud.cube.biz.mdata.server.grid.controller;

import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaTodoNumberParam;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridAreaTodoService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaTodoNumberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网格待办相关
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GridAreaTodoController {
    private final GridAreaTodoService gridAreaTodoService;

    /**
     * 获取网格区域任务数量统计
     *
     * @param param 网格区域id
     *
     * @return 网格区域任务数量统计
     */
    @GetMapping("/grid/area/todo/num")
    public GridAreaTodoNumberVO getGridAreaTodoNum(GridAreaTodoNumberParam param) {
        return gridAreaTodoService.getGridAreaTodoNum(param);
    }

}
