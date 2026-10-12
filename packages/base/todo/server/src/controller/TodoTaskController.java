package com.guhecloud.cube.biz.todo.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.todo.server.entity.TodoBusinessUrl;
import com.guhecloud.cube.biz.todo.server.entity.TodoTask;
import com.guhecloud.cube.biz.todo.server.param.TodoTaskQueryParam;
import com.guhecloud.cube.biz.todo.server.service.TodoBusinessUrlService;
import com.guhecloud.cube.biz.todo.server.service.TodoTaskService;
import com.guhecloud.cube.biz.todo.server.vo.TodoTaskPageVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 待办任务管理接口
 *
 * @author lanchun
 * @date 2022/7/23 11:20
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTaskController {

    private final TodoTaskService todoTaskService;

    private final TodoBusinessUrlService todoBusinessUrlService;

    /**
     * 待办分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/todo-tasks")
    public Pagination<TodoTaskPageVO> todoPage(TodoTaskQueryParam param) {
        Page<TodoTask> page = this.todoTaskService.todoPage(param);
        List<TodoTaskPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), TodoTaskPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return Pagination.empty();
        }
        this.extend(pageVOS);
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 已办分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/todo-tasks-done")
    public Pagination<TodoTaskPageVO> donePage(TodoTaskQueryParam param) {
        Page<TodoTask> page = this.todoTaskService.donePage(param);
        List<TodoTaskPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), TodoTaskPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return Pagination.empty();
        }
        this.extend(pageVOS);
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 根据业务主键批量删除待办任务
     *
     * @param businessIds 业务主键集合
     */
    @DeleteMapping("/todo-task/business/batch")
    public void removeBatchByBusinessIds(@RequestBody Set<String> businessIds) {
        this.todoTaskService.removeBatchByBusinessIds(businessIds);
    }

    private void extend(List<TodoTaskPageVO> pageVOS) {
        Set<String> businessTypes = pageVOS.stream().map(todoTaskPageVO -> todoTaskPageVO.getBusinessType()).collect(Collectors.toSet());
        Map<String, TodoBusinessUrl> todoBusinessUrlMap = this.todoBusinessUrlService
                .listByBusinessTypes(businessTypes, SessionContextHolder.getRequester().getTerminal()).stream()
                .collect(Collectors.toMap(TodoBusinessUrl::getBusinessType, todoBusinessUrl -> todoBusinessUrl, (o, n) -> n));
        pageVOS.stream().forEach(todoTaskPageVO -> {
            TodoBusinessUrl todoBusinessUrl = todoBusinessUrlMap.get(todoTaskPageVO.getBusinessType());
            if (Objects.nonNull(todoBusinessUrl)) {
                todoTaskPageVO.setUrl(todoBusinessUrl.getUrl());
            }
        });
    }
}
