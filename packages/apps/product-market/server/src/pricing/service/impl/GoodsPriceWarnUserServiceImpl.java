package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnUserMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnUser;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnUserParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnUserQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnUserService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnUserVO;
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
 * 描述：采价预警通知服务层
 *
 * @author Appet
 * @date 2023-06-05 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnUserServiceImpl extends ServiceImpl<GoodsPriceWarnUserMapper, GoodsPriceWarnUser> implements GoodsPriceWarnUserService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPriceWarnUserParam param) {
        GoodsPriceWarnUser goodsPriceWarnUser = ObjectUtils.copying(param, GoodsPriceWarnUser.class);
        this.baseMapper.insert(goodsPriceWarnUser);
        return goodsPriceWarnUser.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPriceWarnUserParam param) {
        GoodsPriceWarnUser goodsPriceWarnUser = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPriceWarnUser, "未查询到相关信息");
        ObjectUtils.copying(param, goodsPriceWarnUser);
        this.baseMapper.updateById(goodsPriceWarnUser);
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
    public GoodsPriceWarnUserVO getById(Long id) {
        GoodsPriceWarnUser goodsPriceWarnUser = this.baseMapper.selectById(id);
        return ObjectUtils.copying(goodsPriceWarnUser, GoodsPriceWarnUserVO.class);
    }

    @Override
    public Pagination<GoodsPriceWarnUserVO> getPage(GoodsPriceWarnUserQueryParam param) {
        //请自行添加查询条件
        Page<GoodsPriceWarnUser> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, GoodsPriceWarnUserVO.class);
    }
}