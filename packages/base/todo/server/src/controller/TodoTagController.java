package com.guhecloud.cube.biz.todo.server.controller;

import com.guhecloud.cube.biz.todo.server.entity.TodoTag;
import com.guhecloud.cube.biz.todo.server.param.TodoTagModifyParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagQueryParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagReferBusinessTypeModifyParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagUpdateParam;
import com.guhecloud.cube.biz.todo.server.service.TodoTagService;
import com.guhecloud.cube.biz.todo.server.vo.TodoTagVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 待办标签接口管理
 *
 * @author lanchun
 * @date 2023/2/27 13:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTagController {

    private final TodoTagService todoTagService;

    /**
     * 待办任务标签保存
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/todo-tag")
    public Long save(@Valid @RequestBody TodoTagModifyParam param) {
        return this.todoTagService.save(param);
    }

    /**
     * 待办任务标签修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/todo-tag/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody TodoTagUpdateParam param) {
        this.todoTagService.update(id, param);
    }

    /**
     * 待办任务标签列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/todo-tags")
    public List<TodoTagVO> listTodoTags(@Valid TodoTagQueryParam param) {
        List<TodoTag> todoTags = this.todoTagService.listTodoTags(param);
        return ObjectUtils.copying(todoTags, TodoTagVO.class);
    }

    /**
     * 待办任务标签删除
     *
     * @param id
     */
    @DeleteMapping("/todo-tag/{id}")
    public void removeById(@PathVariable("id") Long id) {
        this.todoTagService.removeById(id);
    }

    /**
     * 待办任务标签关联业务类型
     *
     * @param todoTagCode
     * @param param
     *
     * @return
     */
    @PostMapping("/todo-tag/{todoTagCode}/refer/business-type")
    public Long saveTodoTagReferBusinessType(@PathVariable("todoTagCode") String todoTagCode,
                                             @Valid @RequestBody TodoTagReferBusinessTypeModifyParam param
                                            ) {
        return this.todoTagService.saveReferBusinessType(todoTagCode, param);
    }
}
