package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.FunctionMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Function;
import com.guhecloud.cube.biz.ouaa.server.param.FunctionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.FunctionService;
import com.guhecloud.cube.biz.ouaa.server.service.MenuFunctionService;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
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

/**
 * 功能业务操作实现
 *
 * @author Woody
 * @date 2021/7/1
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FunctionServiceImpl extends ServiceImpl<FunctionMapper, Function> implements FunctionService {

    private final MenuFunctionService menuFunctionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull FunctionModifyParam param) {
        Function function = ObjectUtils.copying(param, Function.class);
        this.baseMapper.insert(function);
        //保存菜单与按钮关联
        menuFunctionService.save(param.getMenuId(), function.getId());
        return function.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull FunctionModifyParam param) {
        Function function = this.getById(id);
        if (Objects.nonNull(function)) {
            ObjectUtils.copying(param, function);
            this.baseMapper.updateById(function);
        }
    }

    @Override
    public Function getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<Function> listFunctions(@NonNull Collection<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : this.lambdaQuery().in(Function::getId, ids).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        Function function = this.getById(id);
        if (Objects.nonNull(function)) {
            DatabaseContextHolder.softDelete(this, id);
            menuFunctionService.remove(id);
        }
    }

    @Override
    public List<MenuFunctionVO> getAllMenuFunction() {
        return this.baseMapper.getAll();
    }
}
