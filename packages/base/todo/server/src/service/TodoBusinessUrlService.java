package com.guhecloud.cube.biz.todo.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.todo.server.entity.TodoBusinessUrl;
import com.guhecloud.cube.biz.todo.server.param.TodoBusinessUrlModifyParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface TodoBusinessUrlService extends IService<TodoBusinessUrl> {

    Long save(TodoBusinessUrlModifyParam param);

    void update(Long id, TodoBusinessUrlModifyParam param);

    TodoBusinessUrl getById(Serializable id);

    List<TodoBusinessUrl> listByBusinessTypes(Collection<String> businessTypes, String terminal);
}
