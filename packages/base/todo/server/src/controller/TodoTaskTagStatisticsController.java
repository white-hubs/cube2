package com.guhecloud.cube.biz.todo.server.controller;

import com.guhecloud.cube.biz.todo.server.entity.TodoTag;
import com.guhecloud.cube.biz.todo.server.entity.TodoTagReferBusinessType;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskNumberStatisticsParam;
import com.guhecloud.cube.biz.todo.server.service.TodoTagService;
import com.guhecloud.cube.biz.todo.server.service.TodoTaskStatisticsService;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeStatisticsVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoBusinessTypeVO;
import com.guhecloud.cube.biz.todo.server.vo.TodoTagVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 待办任务标签统计管理接口
 *
 * @author lanchun
 * @date 2023/2/27 14:46
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTaskTagStatisticsController {

    private final TodoTaskStatisticsService todoTaskStatisticsService;

    private final TodoTagService todoTagService;

    /**
     * 待办标签统计
     *
     * @return
     */
    @GetMapping("/todo-task-tag-statistics")
    public List<TodoBusinessTypeStatisticsVO> todoTagBusinessTypeTodoStatistics() {
        //待办业务类型分组统计结果
        List<TodoBusinessTypeStatisticsVO> todoBusinessTypeStatisticsVOS = this.todoTaskStatisticsService.todoBusinessTypeTodoStatistics();
        Set<String> businessTypes = todoBusinessTypeStatisticsVOS.stream().map(TodoBusinessTypeStatisticsVO::getBusinessType)
                .collect(Collectors.toSet());
        //获取待办标签
        List<TodoTagVO> todoTags = this.getTodoTagVOS(businessTypes);
        if (CollectionUtils.isEmpty(todoTags)) {
            //不存在标签时,直接返回
            return Collections.emptyList();
        }
        List<TodoBusinessTypeStatisticsVO> todoTagBusinessTypeStatisticsVO = new ArrayList<>(todoTags.size());
        Set<String> todoTagCodes = todoTags.stream().map(TodoTagVO::getCode).collect(Collectors.toSet());
        //获取待办标签关联的业务类型
        Map<String, List<TodoTagReferBusinessType>> todoTaskReferBusinessTypeMaps = this.todoTagService
                .listTodoTagReferBusinessTypesByCodes(todoTagCodes).stream()
                .collect(Collectors.groupingBy(TodoTagReferBusinessType::getTodoTagCode));
        todoTags.stream().forEach(todoTag -> {
            List<TodoTagReferBusinessType> todoTagReferBusinessTypes = todoTaskReferBusinessTypeMaps.get(todoTag.getCode());
            Long todoTagReferBusinessCount = 0L;
            if (!CollectionUtils.isEmpty(todoTagReferBusinessTypes)) {
                Set<String> tagBusinessTypes = todoTagReferBusinessTypes.stream().map(TodoTagReferBusinessType::getBusinessType)
                        .collect(Collectors.toSet());
                todoTagReferBusinessCount = todoBusinessTypeStatisticsVOS.stream()
                        .filter(todoBusinessTypeStatisticsVO -> tagBusinessTypes.contains(todoBusinessTypeStatisticsVO.getBusinessType()))
                        .mapToLong(TodoBusinessTypeStatisticsVO::getTotal).sum();
            }
            todoTagBusinessTypeStatisticsVO.add(TodoBusinessTypeStatisticsVO.builder()
                                                        .businessType(todoTag.getCode())
                                                        .businessTypeName(todoTag.getName())
                                                        .total(todoTagReferBusinessCount)
                                                        .build());
        });
        return todoTagBusinessTypeStatisticsVO;
    }

    /**
     * 待办标签列表
     *
     * @return
     */
    @GetMapping("/todo-task-tags")
    public List<TodoTagVO> todoTags() {
        //待办业务类型列表
        List<TodoBusinessTypeVO> todoBusinessTypeVOS = this.todoTaskStatisticsService.todoBusinessTypes();
        Set<String> businessTypes = todoBusinessTypeVOS.stream().map(TodoBusinessTypeVO::getBusinessType).collect(Collectors.toSet());
        return getTodoTagVOS(businessTypes);
    }

    /**
     * 已办标签列表
     *
     * @return
     */
    @GetMapping("/todo-done-task-tags")
    public List<TodoTagVO> todoDoneTags() {
        //已办业务类型列表
        List<TodoBusinessTypeVO> todoBusinessTypeVOS = this.todoTaskStatisticsService.todoDoneBusinessTypes();
        Set<String> businessTypes = todoBusinessTypeVOS.stream().map(TodoBusinessTypeVO::getBusinessType).collect(Collectors.toSet());
        return getTodoTagVOS(businessTypes);
    }

    private List<TodoTagVO> getTodoTagVOS(Set<String> businessType) {
        if (CollectionUtils.isEmpty(businessType)) {
            return Collections.emptyList();
        }
        //获取业务类型关联的标签
        Set<String> todoTagCodes = this.todoTagService.listTodoTagReferBusinessTypes(businessType).stream()
                .map(TodoTagReferBusinessType::getTodoTagCode).collect(Collectors.toSet());
        //获取自定义标签
        List<TodoTag> todoTags = this.todoTagService.listTodoTagsByCodes(todoTagCodes);
        return ObjectUtils.copying(todoTags, TodoTagVO.class);
    }

    /**
     * 条件统计任务数量
     *
     * @param param 查询参数
     *
     * @return 任务数量
     */
    @PostMapping("/todo-task-tag/number-statistics")
    public Long todoTaskNumberStatistics(@RequestBody TodoTaskNumberStatisticsParam param) {
        return todoTaskStatisticsService.todoTaskNumberStatistics(param);
    }

}
