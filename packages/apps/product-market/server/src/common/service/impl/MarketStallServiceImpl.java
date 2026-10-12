package com.guhecloud.cube.srv.gen.product.market.server.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.common.dao.MarketStallMapper;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketStallParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketStallQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.service.MarketStallService;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketStallGroupByBossVO;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketStallVO;
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
 * 描述：档位管理服务层
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketStallServiceImpl extends ServiceImpl<MarketStallMapper, MarketStall> implements MarketStallService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(MarketStallParam param) {
        MarketStall marketStall = ObjectUtils.copying(param, MarketStall.class);
        this.baseMapper.insert(marketStall);
        return marketStall.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, MarketStallParam param) {
        MarketStall marketStall = this.baseMapper.selectById(id);
        Assert.isNotFound(marketStall, "未查询到相关信息");
        ObjectUtils.copying(param, marketStall);
        this.baseMapper.updateById(marketStall);
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
    public MarketStallVO getById(Long id) {
        MarketStall marketStall = this.baseMapper.selectById(id);
        return ObjectUtils.copying(marketStall, MarketStallVO.class);
    }

    @Override
    public Pagination<MarketStallVO> getPage(MarketStallQueryParam param) {
        //请自行添加查询条件
        Page<MarketStall> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), MarketStall::getName, param.getName())
                .like(StringUtils.isNotBlank(param.getBossName()), MarketStall::getBossName, param.getBossName())
                .eq(StringUtils.isNotBlank(param.getType()), MarketStall::getType, param.getType())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, MarketStallVO.class);
    }

    @Override
    public Pagination<MarketStallGroupByBossVO> getStallGroupByBossPage(MarketStallQueryParam param) {
        Page<MarketStallGroupByBossVO> page = this.baseMapper.getStallGroupByBoss(PagingUtils.convert(param), param);
        return PagingUtils.convert(page, MarketStallGroupByBossVO.class);
    }
}