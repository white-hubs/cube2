package com.guhecloud.cube.biz.todo.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.todo.server.entity.TodoTask;
import com.guhecloud.cube.biz.todo.server.param.TodoHomePageParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskNumberStatisticsParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskQueryParam;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoTaskGroupTagVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoTaskPageVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoTaskTrendStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TodoTaskMapper extends BaseMapper<TodoTask> {

    Page<TodoTask> selectTodoPage(@Param("param") TodoTaskQueryParam param, @Param("userId") Long userId, Page<TodoTaskQueryParam> page);

    Page<TodoTask> selectDonePage(@Param("param") TodoTaskQueryParam param, @Param("userId") Long userId, Page<TodoTaskQueryParam> page);

    Long selectAllTodoNumTotal(@Param("period") Period period);

    Long selectTodoNumTotal(@Param("userId") Long userId);

    List<TodoBusinessTypeStatisticsVO> selectAllTodoBusinessTypeTodoStatistics(@Param("period") Period period);

    List<TodoBusinessTypeStatisticsVO> selectTodoBusinessTypeTodoStatistics(@Param("userId") Long userId);

    List<TodoBusinessTypeVO> selectTodoBusinessTypes(@Param("userId") Long userId);

    List<TodoBusinessTypeVO> selectTodoDoneBusinessTypes(@Param("userId") Long userId);

    List<TodoTaskTrendStatisticsVO> selectTodoTaskTrendStatistics(@Param("userId") Long userId, @Param("period") Period period);

    Integer selectAllowedHandoverTaskList(@Param("taskIdList") List<Long> taskIdList);

    List<TodoTaskGroupTagVO> selectTodoTaskNumTotal(@Param("status") String status, @Param("param") TodoHomePageParam param);

    Page<TodoTaskPageVO> selectTask(@Param("page") Page<?> page, @Param("param") TodoTaskQueryParam param);

    Long todoTaskNumberStatistics(@Param("param") TodoTaskNumberStatisticsParam param);
}
