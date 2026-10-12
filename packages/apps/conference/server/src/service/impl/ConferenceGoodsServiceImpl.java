package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.ConferenceGoodsMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceGoods;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceGoodsParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceGoodsQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceGoodsService;
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
 * @author zengadyon
 * @description conference_goods服务默认实现
 * @createDate 2024-04-16 14:43:42
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceGoodsServiceImpl extends ServiceImpl<ConferenceGoodsMapper, ConferenceGoods> implements ConferenceGoodsService {

    private final ConferenceGoodsMapper conferenceGoodsMapper;

    @Override
    public Pagination<ConferenceGoods> getPage(ConferenceGoodsQueryParam param) {
        Page<ConferenceGoods> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getGoodsName()), ConferenceGoods::getGoodsName, param.getGoodsName())
                .orderByDesc(ConferenceGoods::getCreateTime)
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, ConferenceGoods.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addConferenceGoods(ConferenceGoodsParam param) {
        Assert.isParameterError(StringUtils.isBlank(param.getGoodsName()), "物品名称为空");
        Assert.isParameterError(StringUtils.isBlank(param.getAreaName()), "区域为空");
        ConferenceGoods conferenceGoods = ObjectUtils.copying(param, ConferenceGoods.class);
        conferenceGoodsMapper.insert(conferenceGoods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConferenceGoods(Long id, ConferenceGoodsParam param) {
        Assert.isParameterError(StringUtils.isBlank(param.getGoodsName()), "物品名称为空");
        Assert.isParameterError(StringUtils.isBlank(param.getAreaName()), "区域为空");
        ConferenceGoods byId = this.getById(id);
        ObjectUtils.copying(param, byId);
        conferenceGoodsMapper.updateById(byId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public List<ConferenceGoods> getGoodsList(ConferenceGoodsQueryParam param) {
        return this.list(Wrappers.<ConferenceGoods>lambdaQuery()
                                 .orderByDesc(ConferenceGoods::getCreateTime));
    }
}




