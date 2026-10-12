package com.guhecloud.cube.biz.todo.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.todo.server.entity.TodoTag;
import com.guhecloud.cube.biz.todo.server.entity.TodoTagReferBusinessType;
import com.guhecloud.cube.biz.todo.server.param.TodoTagModifyParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagQueryParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagReferBusinessTypeModifyParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagUpdateParam;

import java.util.List;
import java.util.Set;

public interface TodoTagService extends IService<TodoTag> {

    Long save(TodoTagModifyParam param);

    Long saveReferBusinessType(String tagCode, TodoTagReferBusinessTypeModifyParam param);

    void update(Long id, TodoTagUpdateParam param);

    List<TodoTag> listTodoTags(TodoTagQueryParam param);

    List<TodoTag> listTodoTagsByCodes(Set<String> todoTagCodes);

    List<TodoTagReferBusinessType> listTodoTagReferBusinessTypesByCode(String todoTagCode);

    List<TodoTagReferBusinessType> listTodoTagReferBusinessTypesByCodes(Set<String> todoTagCodes);

    List<TodoTagReferBusinessType> listTodoTagReferBusinessTypes(Set<String> businessTypes);

    Set<String> listBusinessTypesByCode(String todoTagCode);

    void removeById(Long id);

    Set<String> listHandoverBusinessTypes();
}
