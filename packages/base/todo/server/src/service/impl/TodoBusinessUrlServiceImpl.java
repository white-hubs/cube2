package com.guhecloud.cube.biz.todo.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.todo.server.dao.TodoBusinessUrlMapper;
import com.guhecloud.cube.biz.todo.server.entity.TodoBusinessUrl;
import com.guhecloud.cube.biz.todo.server.param.TodoBusinessUrlModifyParam;
import com.guhecloud.cube.biz.todo.server.service.TodoBusinessUrlService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoBusinessUrlServiceImpl extends ServiceImpl<TodoBusinessUrlMapper, TodoBusinessUrl> implements TodoBusinessUrlService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull TodoBusinessUrlModifyParam param) {
        TodoBusinessUrl todoBusinessUrl = ObjectUtils.copying(param, TodoBusinessUrl.class);
        this.baseMapper.insert(todoBusinessUrl);
        return todoBusinessUrl.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull TodoBusinessUrlModifyParam param) {
        TodoBusinessUrl todoBusinessUrl = this.getById(id);
        if (Objects.nonNull(todoBusinessUrl)) {
            ObjectUtils.copying(param, todoBusinessUrl);
            this.baseMapper.updateById(todoBusinessUrl);
        }
    }

    @Override
    public TodoBusinessUrl getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<TodoBusinessUrl> listByBusinessTypes(@NonNull Collection<String> businessTypes, @NonNull String terminal) {
        return CollectionUtils.isEmpty(businessTypes) ? Collections.emptyList() : this.lambdaQuery()
                .in(TodoBusinessUrl::getBusinessType, businessTypes)
                .eq(TodoBusinessUrl::getTerminal, terminal)
                .list();
    }
}
