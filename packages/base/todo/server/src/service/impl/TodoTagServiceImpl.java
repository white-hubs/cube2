package com.guhecloud.cube.biz.todo.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.todo.server.dao.TodoTagMapper;
import com.guhecloud.cube.biz.todo.server.entity.TodoTag;
import com.guhecloud.cube.biz.todo.server.entity.TodoTagReferBusinessType;
import com.guhecloud.cube.biz.todo.server.param.TodoTagModifyParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagQueryParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagReferBusinessTypeModifyParam;
import com.guhecloud.cube.biz.todo.server.param.TodoTagUpdateParam;
import com.guhecloud.cube.biz.todo.server.service.TodoTagReferBusinessTypeService;
import com.guhecloud.cube.biz.todo.server.service.TodoTagService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoTagServiceImpl extends ServiceImpl<TodoTagMapper, TodoTag> implements TodoTagService {

    private final TodoTagReferBusinessTypeService todoTagReferBusinessTypeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull TodoTagModifyParam param) {
        TodoTag todoTag = ObjectUtils.copying(param, TodoTag.class);
        this.baseMapper.insert(todoTag);
        return todoTag.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveReferBusinessType(@NonNull String tagCode, @NonNull TodoTagReferBusinessTypeModifyParam param) {
        TodoTagReferBusinessType todoTagReferBusinessType = TodoTagReferBusinessType.builder()
                .todoTagCode(tagCode)
                .businessType(param.getBusinessType())
                .build();
        todoTagReferBusinessTypeService.save(todoTagReferBusinessType);
        return todoTagReferBusinessType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull TodoTagUpdateParam param) {
        TodoTag todoTag = this.getById(id);
        if (Objects.nonNull(todoTag)) {
            todoTag.setName(param.getName());
            this.baseMapper.updateById(todoTag);
        }
    }

    @Override
    public List<TodoTag> listTodoTags(TodoTagQueryParam param) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(TodoTag::getCode, param.getKeyword())
                        .or()
                        .like(TodoTag::getName, param.getKeyword()))
                .list();
    }

    @Override
    public List<TodoTag> listTodoTagsByCodes(@NonNull Set<String> todoTagCodes) {
        return CollectionUtils.isEmpty(todoTagCodes) ? Collections.emptyList() : this.lambdaQuery()
                .in(TodoTag::getCode, todoTagCodes)
                .list();
    }

    @Override
    public List<TodoTagReferBusinessType> listTodoTagReferBusinessTypesByCode(@NonNull String todoTagCode) {
        return this.listTodoTagReferBusinessTypesByCodes(Collections.singleton(todoTagCode));
    }

    @Override
    public List<TodoTagReferBusinessType> listTodoTagReferBusinessTypesByCodes(@NonNull Set<String> todoTagCodes) {
        return CollectionUtils.isEmpty(todoTagCodes) ? Collections.emptyList() : this.todoTagReferBusinessTypeService.list(Wrappers
                                                                                                                                   .<TodoTagReferBusinessType>lambdaQuery()
                                                                                                                                   .in(TodoTagReferBusinessType::getTodoTagCode, todoTagCodes));
    }

    @Override
    public List<TodoTagReferBusinessType> listTodoTagReferBusinessTypes(@NonNull Set<String> businessTypes) {
        return CollectionUtils.isEmpty(businessTypes) ? Collections.emptyList() : this.todoTagReferBusinessTypeService.list(Wrappers
                                                                                                                                    .<TodoTagReferBusinessType>lambdaQuery()
                                                                                                                                    .in(TodoTagReferBusinessType::getBusinessType, businessTypes));
    }

    @Override
    public Set<String> listBusinessTypesByCode(@NonNull String todoTagCode) {
        List<TodoTagReferBusinessType> todoTagReferBusinessTypes = this.listTodoTagReferBusinessTypesByCode(todoTagCode);
        if (CollectionUtils.isEmpty(todoTagReferBusinessTypes)) {
            return Collections.emptySet();
        }
        return todoTagReferBusinessTypes.stream().map(TodoTagReferBusinessType::getBusinessType).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Set<String> listHandoverBusinessTypes() {
        List<TodoTag> list = this.lambdaQuery().eq(TodoTag::getHandover, true)
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptySet();
        }
        List<TodoTagReferBusinessType> todoTagReferBusinessTypes = this.listTodoTagReferBusinessTypesByCodes(list.stream()
                                                                                                                     .map(TodoTag::getCode)
                                                                                                                     .collect(Collectors.toSet()));
        if (CollectionUtils.isEmpty(todoTagReferBusinessTypes)) {
            return Collections.emptySet();
        }
        return todoTagReferBusinessTypes.stream().map(TodoTagReferBusinessType::getBusinessType).collect(Collectors.toSet());
    }
}
