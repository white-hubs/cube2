package com.guhecloud.cube.srv.gen.product.market.server.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.IncomingQuantityMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.IncomingQuantity;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.IncomingQuantityParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.IncomingQuantityQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.IncomingQuantityService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.IncomingQuantityVO;
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
 * 描述：农产品来货量服务层
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncomingQuantityServiceImpl extends ServiceImpl<IncomingQuantityMapper, IncomingQuantity> implements IncomingQuantityService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(IncomingQuantityParam param) {
        IncomingQuantity incomingQuantity = ObjectUtils.copying(param, IncomingQuantity.class);
        this.baseMapper.insert(incomingQuantity);
        return incomingQuantity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, IncomingQuantityParam param) {
        IncomingQuantity incomingQuantity = this.baseMapper.selectById(id);
        Assert.isNotFound(incomingQuantity, "未查询到相关信息");
        ObjectUtils.copying(param, incomingQuantity);
        this.baseMapper.updateById(incomingQuantity);
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
    public IncomingQuantityVO getById(Long id) {
        IncomingQuantity incomingQuantity = this.baseMapper.selectById(id);
        return ObjectUtils.copying(incomingQuantity, IncomingQuantityVO.class);
    }

    @Override
    public Pagination<IncomingQuantityVO> getPage(IncomingQuantityQueryParam param) {
        //请自行添加查询条件
        Page<IncomingQuantity> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, IncomingQuantityVO.class);
    }
}