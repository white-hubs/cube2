package com.guhecloud.cube.biz.mdata.server.grid.service.impl;

import com.guhecloud.cube.biz.mdata.server.grid.param.GridAreaTodoNumberParam;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridAreaTodoService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridAreaTodoNumberVO;
import com.guhecloud.cube.biz.todo.client.TodoTaskClient;
import com.guhecloud.cube.biz.todo.client.enumeration.TodoTaskStatusEnum;
import com.guhecloud.cube.biz.todo.client.param.TodoTaskNumberStatisticsParam;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 网格待办
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GridAreaTodoServiceImpl implements GridAreaTodoService {

    private final TodoTaskClient todoTaskClient;
    private final GridDictionaryUtilServiceImpl gridDictionaryUtilService;

    /**
     * 获取待办统计
     *
     * @param param 条件
     *
     * @return 待办数量统计
     */
    @Override
    public GridAreaTodoNumberVO getGridAreaTodoNum(GridAreaTodoNumberParam param) {
        if (param.getUserId() == null) {
            param.setUserId(SecurityContextHolder.getPrincipal().getId());
        }
        var todoParam = new TodoTaskNumberStatisticsParam();
        todoParam.setUserId(param.getUserId());
        todoParam.setBusinessTypes(gridDictionaryUtilService.getDictionaryBusinessTypes("TODO"));
        todoParam.setPeriod(param.getPeriod());
        // 总数
        var total = todoTaskClient.todoTaskNumberStatistics(todoParam);
        todoParam.setStatus(TodoTaskStatusEnum.DONE.name());
        // 已办
        var doneTotal = todoTaskClient.todoTaskNumberStatistics(todoParam);
        return GridAreaTodoNumberVO.builder()
                .totalNum(total)
                .doneTotalNum(doneTotal)
                .build();
    }

}
