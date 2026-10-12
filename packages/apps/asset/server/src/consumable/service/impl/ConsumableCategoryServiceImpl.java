package com.guhecloud.cube.srv.gen.property.management.server.consumable.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.dao.ConsumableCategoryMapper;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCategory;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCategoryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCategoryQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.service.ConsumableCategoryService;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCategoryVO;
import com.guhecloud.cube.srv.gen.property.management.server.exception.PropertyStatus;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：耗材分类服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConsumableCategoryServiceImpl extends ServiceImpl<ConsumableCategoryMapper, ConsumableCategory> implements ConsumableCategoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ConsumableCategoryParam param) {
        ConsumableCategory consumableCategory = ObjectUtils.copying(param, ConsumableCategory.class);
        this.baseMapper.insert(consumableCategory);
        return consumableCategory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ConsumableCategoryParam param) {
        ConsumableCategory consumableCategory = this.baseMapper.selectById(id);
        Assert.parameterIsNull(consumableCategory, PropertyStatus.CONSUMABLE_NO_DETAILS);
        ObjectUtils.copying(param, consumableCategory);
        this.baseMapper.updateById(consumableCategory);
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
    public ConsumableCategoryVO getById(Long id) {
        ConsumableCategory consumableCategory = this.baseMapper.selectById(id);
        return ObjectUtils.copying(consumableCategory, ConsumableCategoryVO.class);
    }

    @Override
    public Pagination<ConsumableCategoryVO> getPage(ConsumableCategoryQueryParam param) {
        //请自行添加查询条件
        Page<ConsumableCategory> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, ConsumableCategoryVO.class);
    }
}