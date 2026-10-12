package com.guhecloud.cube.biz.todo.server.controller;

import com.guhecloud.cube.biz.todo.server.param.TodoBusinessUrlModifyParam;
import com.guhecloud.cube.biz.todo.server.service.TodoBusinessUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 待办业务路径配置
 *
 * @author lanchun
 * @date 2022/8/2 11:31
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoBusinessUrlController {

    private final TodoBusinessUrlService todoBusinessUrlService;

    /**
     * 新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/todo-business-url")
    public Long save(@Valid @RequestBody TodoBusinessUrlModifyParam param) {
        return this.todoBusinessUrlService.save(param);
    }

    /**
     * 修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/todo-business-url/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody TodoBusinessUrlModifyParam param) {
        this.todoBusinessUrlService.update(id, param);
    }
}
