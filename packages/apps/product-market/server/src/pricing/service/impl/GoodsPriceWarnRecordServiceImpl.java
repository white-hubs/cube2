package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnRecordLogMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnRecordMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPricingRecordMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRecordLog;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRecordService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：采价预警记录服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnRecordServiceImpl extends ServiceImpl<GoodsPriceWarnRecordMapper, GoodsPriceWarnRecord> implements GoodsPriceWarnRecordService {
    private final GoodsPriceWarnRecordLogMapper goodsPriceWarnRecordLogMapper;
    private final GoodsPricingRecordMapper goodsPricingRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPriceWarnRecordParam param) {
        GoodsPriceWarnRecord goodsPriceWarnRecord = ObjectUtils.copying(param, GoodsPriceWarnRecord.class);
        goodsPriceWarnRecord.setStatus(false);
        this.baseMapper.insert(goodsPriceWarnRecord);
        return goodsPriceWarnRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, GoodsPriceWarnRecordParam param) {
        GoodsPriceWarnRecord goodsPriceWarnRecord = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPriceWarnRecord, "未查询到相关信息");
        //构建修改日志对象
        GoodsPriceWarnRecordLog goodsPriceWarnRecordLog = this.buildGoodsPriceWarnRecordLog(goodsPriceWarnRecord, param);
        ObjectUtils.copying(param, goodsPriceWarnRecord);
        //处理状态
        goodsPriceWarnRecord.setStatus(true);
        this.baseMapper.updateById(goodsPriceWarnRecord);

        //修改采价记录的价格并记录日志
        this.updatePricingRecordAndInsertLog(goodsPriceWarnRecord, goodsPriceWarnRecordLog);
    }

    private GoodsPriceWarnRecordLog buildGoodsPriceWarnRecordLog(GoodsPriceWarnRecord goodsPriceWarnRecord,
                                                                 GoodsPriceWarnRecordParam param
                                                                ) {
        GoodsPriceWarnRecordLog goodsPriceWarnRecordLog = new GoodsPriceWarnRecordLog();
        goodsPriceWarnRecordLog.setOldMaxPrice(goodsPriceWarnRecord.getMaxPrice());
        goodsPriceWarnRecordLog.setOldMidPrice(goodsPriceWarnRecord.getMidPrice());
        goodsPriceWarnRecordLog.setOldMinPrice(goodsPriceWarnRecord.getMinPrice());
        goodsPriceWarnRecordLog.setNewMaxPrice(param.getMaxPrice());
        goodsPriceWarnRecordLog.setNewMidPrice(param.getMidPrice());
        goodsPriceWarnRecordLog.setNewMinPrice(param.getMinPrice());
        goodsPriceWarnRecordLog.setHandlerId(SecurityContextHolder.getPrincipal().getId());
        goodsPriceWarnRecordLog.setHandlerName(SecurityContextHolder.getPrincipal().getName());
        return goodsPriceWarnRecordLog;
    }

    private void updatePricingRecordAndInsertLog(GoodsPriceWarnRecord goodsPriceWarnRecord,
                                                 GoodsPriceWarnRecordLog goodsPriceWarnRecordLog
                                                ) {
        //修改采价记录的价格信息
        GoodsPricingRecord pricingRecord = goodsPricingRecordMapper.selectById(goodsPriceWarnRecord.getPricingRecordId());
        if (pricingRecord != null) {
            pricingRecord.setMaxPrice(goodsPriceWarnRecordLog.getNewMaxPrice());
            pricingRecord.setMidPrice(goodsPriceWarnRecordLog.getNewMidPrice());
            pricingRecord.setMinPrice(goodsPriceWarnRecordLog.getNewMinPrice());
            goodsPricingRecordMapper.updateById(pricingRecord);
        }
        //插入修改日志
        goodsPriceWarnRecordLog.setWarnId(goodsPriceWarnRecord.getId());
        goodsPriceWarnRecordLogMapper.insert(goodsPriceWarnRecordLog);
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
    public GoodsPriceWarnRecordVO getById(Long id) {
        GoodsPriceWarnRecord goodsPriceWarnRecord = this.baseMapper.selectById(id);
        return ObjectUtils.copying(goodsPriceWarnRecord, GoodsPriceWarnRecordVO.class);
    }

    @Override
    public Pagination<GoodsPriceWarnRecordVO> getPage(GoodsPriceWarnRecordQueryParam param) {
        Period period = param.getPeriod();
        //请自行添加查询条件
        Page<GoodsPriceWarnRecord> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getRuleName()), GoodsPriceWarnRecord::getRuleName, param.getRuleName())
                .like(StringUtils.isNotEmpty(param.getUserName()), GoodsPriceWarnRecord::getUserName, param.getUserName())
                .eq(param.getStatus() != null, GoodsPriceWarnRecord::getStatus, param.getStatus())
                .between(period !=
                         null, GoodsPriceWarnRecord::getPricingTime, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                .orderByDesc(GoodsPriceWarnRecord::getPricingTime)
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, GoodsPriceWarnRecordVO.class);
    }

    @Override
    public List<CommonStatVO> goodsPriceWarnStatusStatByTime(GoodsPriceWarnStatParam param) {
        return this.baseMapper.goodsPriceWarnStatusStatByTime(param);
    }
}