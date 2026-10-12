package com.guhecloud.cube.biz.mdata.server.grid.controller;

import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaTaskPageParam;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridAreaTaskService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridCodeNameStringStringVO;
import com.guhecloud.cube.biz.work.center.client.vo.WorkHomePageVO;
import com.guhecloud.cube.biz.work.center.client.vo.WorkTaskPageVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网格任务相关
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GridAreaTaskController {
    private final GridAreaTaskService gridAreaTaskService;

    /**
     * 获取网格区域业务类型下拉列表
     *
     * @return 网格区域业务类型下拉列表
     */
    @GetMapping("/grid/area/business/select")
    public List<GridCodeNameStringStringVO> getGridAreaBusinessSelect() {
        return gridAreaTaskService.getGridAreaBusinessSelect();
    }

    /**
     * 获取网格区域任务列表
     *
     * @param param 网格区域id
     *
     * @return 网格区域任务分页列表
     */
    @GetMapping("/grid/area/task/page")
    public Pagination<WorkTaskPageVO> getGridAreaTaskPage(GridAreaTaskPageParam param) {
        return gridAreaTaskService.getGridAreaTaskPage(param);
    }

    /**
     * 获取网格区域任务数量统计
     *
     * @param param 网格区域id
     *
     * @return 网格区域任务数量统计
     */
    @GetMapping("/grid/area/task/num")
    public WorkHomePageVO getGridAreaTaskNum(GridAreaTaskPageParam param) {
        return gridAreaTaskService.getGridAreaTaskNum(param);
    }

}
