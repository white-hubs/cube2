package com.guhecloud.cube.biz.todo.server.controller;

import com.guhecloud.cube.biz.todo.server.service.TodoTaskStatisticsService;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoTaskTrendStatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 待办统计管理接口
 *
 * @author lanchun
 * @date 2022/7/23 11:11
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTaskStatisticsController {

    private final TodoTaskStatisticsService todoTaskStatisticsService;

    /**
     * 待办数量统计
     *
     * @return
     */
    @GetMapping("/todo-task-statistics")
    public Long todoNumTotal() {
        return this.todoTaskStatisticsService.todoNumTotal();
    }

    /**
     * 待办业务类型统计
     *
     * @return
     */
    @GetMapping("/todo-task-statistics/business-type-statistics")
    public List<TodoBusinessTypeStatisticsVO> todoBusinessTypeTodoStatistics() {
        return this.todoTaskStatisticsService.todoBusinessTypeTodoStatistics();
    }

    /**
     * 待办业务类型
     *
     * @return
     */
    @GetMapping("/todo-task-statistics/business-type")
    public List<TodoBusinessTypeVO> todoBusinessTypes() {
        return this.todoTaskStatisticsService.todoBusinessTypes();
    }

    /**
     * 已办业务类型
     *
     * @return
     */
    @GetMapping("/todo-task-statistics/done/business-type")
    public List<TodoBusinessTypeVO> todoDoneBusinessTypes() {
        return this.todoTaskStatisticsService.todoDoneBusinessTypes();
    }

    /**
     * 近30天待办趋势统计
     *
     * @return
     */
    @GetMapping("/todo-task-statistics/last-thirty-days-trend")
    public List<TodoTaskTrendStatisticsVO> todoTaskLastThirtyDaysTrendStatistics() {
        return this.todoTaskStatisticsService.todoTaskLastThirtyDaysTrendStatistics();
    }
}
