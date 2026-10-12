package com.guhecloud.cube.srv.gen.property.management.server.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.property.management.server.common.dao.StorehouseMapper;
import com.guhecloud.cube.srv.gen.property.management.server.common.entity.Storehouse;
import com.guhecloud.cube.srv.gen.property.management.server.common.param.StorehouseParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.param.StorehouseQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.common.service.StorehouseService;
import com.guhecloud.cube.srv.gen.property.management.server.common.vo.StorehouseVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：仓库管理服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StorehouseServiceImpl extends ServiceImpl<StorehouseMapper, Storehouse> implements StorehouseService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(StorehouseParam param) {
        Storehouse storehouse = ObjectUtils.copying(param, Storehouse.class);
        this.baseMapper.insert(storehouse);
        return storehouse.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, StorehouseParam param) {
        Storehouse storehouse = this.baseMapper.selectById(id);
        Assert.isNotFound(storehouse, "未查询到相关信息");
        ObjectUtils.copying(param, storehouse);
        this.baseMapper.updateById(storehouse);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public StorehouseVO getById(Long id) {
        Storehouse storehouse = this.baseMapper.selectById(id);
        return ObjectUtils.copying(storehouse, StorehouseVO.class);
    }

    @Override
    public Pagination<StorehouseVO> getPage(StorehouseQueryParam param) {
        //请自行添加查询条件
        Page<Storehouse> page = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q ->
                        q.like(Storehouse::getName, param.getKeyword())
                                .or().like(Storehouse::getCode, param.getCode()))
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, StorehouseVO.class);
    }

    @Override
    public List<StorehouseVO> getList(StorehouseQueryParam param) {
        List<Storehouse> list = this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q ->
                        q.like(Storehouse::getName, param.getKeyword())
                                .or().like(Storehouse::getCode, param.getCode())).list();
        return ObjectUtils.copying(list, StorehouseVO.class);
    }
}