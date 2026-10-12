package com.guhecloud.cube.biz.todo.server.service.impl;

import com.guhecloud.cube.biz.todo.server.dao.TodoTaskMapper;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskNumberStatisticsParam;
import com.guhecloud.cube.biz.todo.server.service.TodoTaskStatisticsService;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoTaskTrendStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTaskStatisticsServiceImpl implements TodoTaskStatisticsService {

    private final TodoTaskMapper todoTaskMapper;

    @Override
    public Long allTodoNumTotal(Period period) {
        return this.todoTaskMapper.selectAllTodoNumTotal(period);
    }

    @Override
    public Long todoNumTotal() {
        return this.todoTaskMapper.selectTodoNumTotal(SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    public List<TodoBusinessTypeStatisticsVO> allTodoBusinessTypeTodoStatistics(Period period) {
        return this.todoTaskMapper.selectAllTodoBusinessTypeTodoStatistics(period);
    }

    @Override
    public List<TodoBusinessTypeStatisticsVO> todoBusinessTypeTodoStatistics() {
        return this.todoTaskMapper.selectTodoBusinessTypeTodoStatistics(SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    public List<TodoBusinessTypeVO> todoBusinessTypes() {
        return this.todoTaskMapper.selectTodoBusinessTypes(SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    public List<TodoBusinessTypeVO> todoDoneBusinessTypes() {
        return this.todoTaskMapper.selectTodoDoneBusinessTypes(SecurityContextHolder.getPrincipal().getId());
    }

    @Override
    public List<TodoTaskTrendStatisticsVO> todoTaskLastThirtyDaysTrendStatistics() {
        LocalDateTime endDateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startDateTime = DateUtils.beginOfDay(DateUtils.offsetDay(endDateTime, -30));
        Period period = new Period(startDateTime, endDateTime);
        List<TodoTaskTrendStatisticsVO> todoTaskTrendStatisticsVOS = this.todoTaskMapper.selectTodoTaskTrendStatistics(SecurityContextHolder
                                                                                                                               .getPrincipal()
                                                                                                                               .getId(), period);
        List<TodoTaskTrendStatisticsVO> finalDatas = new ArrayList<>();
        for (LocalDateTime start = startDateTime;
             start.isBefore(endDateTime) || start.isEqual(endDateTime); start = DateUtils.offsetDay(start, 1)) {
            String date = DateUtils.formatDate(start);
            TodoTaskTrendStatisticsVO trendStatVO = todoTaskTrendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date)).findAny()
                    .orElse(null);
            finalDatas.add(trendStatVO == null ? new TodoTaskTrendStatisticsVO(0L, date) : trendStatVO);
        }
        return finalDatas;
    }

    /**
     * 统计任务数量条件统计
     *
     * @param param 查询参数
     *
     * @return 任务数量
     */
    @Override
    public Long todoTaskNumberStatistics(TodoTaskNumberStatisticsParam param) {
        return todoTaskMapper.todoTaskNumberStatistics(param);
    }
}
