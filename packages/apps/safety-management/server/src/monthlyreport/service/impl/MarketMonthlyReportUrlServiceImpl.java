package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao.MarketMonthlyReportUrlMapper;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.MarketMonthlyReportUrlModifyRequest;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MarketMonthlyReportUrlQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportUrlVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportUrlPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyReportUrlService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MarketMonthlyReportUrlServiceImpl extends ServiceImpl<MarketMonthlyReportUrlMapper, MarketMonthlyReportUrlPO> implements MarketMonthlyReportUrlService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull MarketMonthlyReportUrlModifyRequest param) {
        MarketMonthlyReportUrlPO marketMonthlyReportUrlPO = ObjectUtils.copying(param, MarketMonthlyReportUrlPO.class);
        this.baseMapper.insert(marketMonthlyReportUrlPO);
        return marketMonthlyReportUrlPO.getId();
    }

    @Override
    public Page<MarketMonthlyReportUrlVO> getPage(MarketMonthlyReportUrlQuery query) {
        return this.baseMapper.selectPage(PagingUtils.convert(query), query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMarketMonthlyReportUrlEnabled(@NonNull Long id, @NonNull Boolean enabled) {
        MarketMonthlyReportUrlPO marketMonthlyReportUrlPO = this.baseMapper.selectById(id);
        Assert.isNotFound(marketMonthlyReportUrlPO, "链接信息不存在或已被删除");
        marketMonthlyReportUrlPO.setEnabled(enabled);
        this.baseMapper.updateById(marketMonthlyReportUrlPO);
    }
}
