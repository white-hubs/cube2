package com.guhecloud.cube.biz.todo.server.service;

import com.guhecloud.cube.biz.todo.server.param.TodoTaskNumberStatisticsParam;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoTaskTrendStatisticsVO;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

public interface TodoTaskStatisticsService {

    /**
     * 所有待办数量
     *
     * @return
     */
    Long allTodoNumTotal(Period period);

    /**
     * 当前用户待办数量
     *
     * @return
     */
    Long todoNumTotal();

    /**
     * 所有待办业务类型统计数量
     *
     * @return
     */
    List<TodoBusinessTypeStatisticsVO> allTodoBusinessTypeTodoStatistics(Period period);

    /**
     * 当前用户代办业务类型统计数量
     *
     * @return
     */
    List<TodoBusinessTypeStatisticsVO> todoBusinessTypeTodoStatistics();

    /**
     * 当前用户获取待办业务类型列表
     *
     * @return
     */
    List<TodoBusinessTypeVO> todoBusinessTypes();

    /**
     * 当前用户获取已办业务类型列表
     *
     * @return
     */
    List<TodoBusinessTypeVO> todoDoneBusinessTypes();

    /**
     * 当前用户近30天待办趋势
     *
     * @return
     */
    List<TodoTaskTrendStatisticsVO> todoTaskLastThirtyDaysTrendStatistics();
    
    /**
     * 统计任务数量条件统计
     *
     * @param param 查询参数
     *
     * @return 任务数量
     */
    Long todoTaskNumberStatistics(TodoTaskNumberStatisticsParam param);
}
