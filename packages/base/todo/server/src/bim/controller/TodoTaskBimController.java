package com.guhecloud.cube.biz.todo.server.bim.controller;

import com.guhecloud.cube.biz.todo.server.entity.TodoTag;
import com.guhecloud.cube.biz.todo.server.entity.TodoTagReferBusinessType;
import com.guhecloud.cube.biz.todo.server.param.TodoTagQueryParam;
import com.guhecloud.cube.biz.todo.server.service.TodoTagService;
import com.guhecloud.cube.biz.todo.server.service.TodoTaskStatisticsService;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 待办三维统计
 *
 * @author lanchun
 * @date 2022/8/10 14:33
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTaskBimController {

    private final TodoTaskStatisticsService todoTaskStatisticsService;

    private final TodoTagService todoTagService;

    /**
     * 待办数量统计
     *
     * @return
     */
    @GetMapping("/todo-task-bim-statistics")
    public Long todoNumTotal() {
        LocalDateTime dateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offset(dateTime, ChronoUnit.DAYS, -6));
        Period period = new Period(startTime, dateTime);
        return this.todoTaskStatisticsService.allTodoNumTotal(period);
    }

    /**
     * 类型统计待办数量
     *
     * @return
     */
    @GetMapping("/todo-task-bim-statistics/type")
    public List<TodoBusinessTypeStatisticsVO> todoBusinessTypeTodoStatistics() {
        LocalDateTime dateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offset(dateTime, ChronoUnit.DAYS, -6));
        Period period = new Period(startTime, dateTime);
        return this.todoTaskStatisticsService.allTodoBusinessTypeTodoStatistics(period);
    }

    /**
     * 标签统计待办数量
     *
     * @return
     */
    @GetMapping("/todo-task-bim-statistics/tag")
    public List<TodoBusinessTypeStatisticsVO> todoTagTodoStatistics() {
        LocalDateTime dateTime = DateUtils.endOfDay(DateUtils.now());
        LocalDateTime startTime = DateUtils.beginOfDay(DateUtils.offset(dateTime, ChronoUnit.DAYS, -7));
        Period period = new Period(startTime, dateTime);
        List<TodoBusinessTypeStatisticsVO> todoBusinessTypeStatisticsVOS = this.todoTaskStatisticsService.allTodoBusinessTypeTodoStatistics(period);
        return getTodoBusinessTypeStatisticsVOS(todoBusinessTypeStatisticsVOS);
    }

    private List<TodoBusinessTypeStatisticsVO> getTodoBusinessTypeStatisticsVOS(
            List<TodoBusinessTypeStatisticsVO> todoBusinessTypeStatisticsVOS
                                                                               ) {
        //获取待办标签
        List<TodoTag> todoTags = this.todoTagService.listTodoTags(new TodoTagQueryParam());
        if (CollectionUtils.isEmpty(todoTags)) {
            //不存在标签时,不聚合直接返回
            return todoBusinessTypeStatisticsVOS;
        }
        List<TodoBusinessTypeStatisticsVO> todoTagBusinessTypeStatisticsVO = new ArrayList<>();
        Set<String> todoTagCodes = todoTags.stream().map(TodoTag::getCode).collect(Collectors.toSet());
        //获取待办标签关联的业务类型
        Map<String, List<TodoTagReferBusinessType>> todoTaskReferBusinessTypeMaps = this.todoTagService
                .listTodoTagReferBusinessTypesByCodes(todoTagCodes).stream()
                .collect(Collectors.groupingBy(TodoTagReferBusinessType::getTodoTagCode));
        //存在标签的业务类型
        Set<String> tagHasBusinessTypes = new LinkedHashSet<>();
        todoTags.stream().forEach(todoTag -> {
            List<TodoTagReferBusinessType> todoTagReferBusinessTypes = todoTaskReferBusinessTypeMaps.get(todoTag.getCode());
            Long todoTagReferBusinessCount = 0L;
            if (!CollectionUtils.isEmpty(todoTagReferBusinessTypes)) {
                Set<String> businessTypes = todoTagReferBusinessTypes.stream().map(TodoTagReferBusinessType::getBusinessType)
                        .collect(Collectors.toSet());
                todoTagReferBusinessCount = todoBusinessTypeStatisticsVOS.stream()
                        .filter(todoBusinessTypeStatisticsVO -> businessTypes.contains(todoBusinessTypeStatisticsVO.getBusinessType()))
                        .mapToLong(TodoBusinessTypeStatisticsVO::getTotal).sum();
                tagHasBusinessTypes.addAll(businessTypes);
            }
            todoTagBusinessTypeStatisticsVO.add(TodoBusinessTypeStatisticsVO.builder()
                                                        .businessType(todoTag.getCode())
                                                        .businessTypeName(todoTag.getName())
                                                        .total(todoTagReferBusinessCount)
                                                        .build());
        });
        //不包含在标签里的业务类型
        todoTagBusinessTypeStatisticsVO.addAll(todoBusinessTypeStatisticsVOS.stream()
                                                       .filter(todoBusinessTypeStatisticsVO -> !tagHasBusinessTypes.contains(todoBusinessTypeStatisticsVO.getBusinessType()))
                                                       .collect(Collectors.toList()));
        return todoTagBusinessTypeStatisticsVO;
    }
}
