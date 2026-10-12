package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao.MarketMonthlyWindUpMapper;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyWindUpPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyWindUpService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketMonthlyWindUpServiceImpl extends ServiceImpl<MarketMonthlyWindUpMapper, MarketMonthlyWindUpPO> implements MarketMonthlyWindUpService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdate(@NonNull MarketMonthlyReportPO marketMonthlyReportPO) {
        MarketMonthlyWindUpPO marketMonthlyWindUpPO = this.baseMapper.selectOne(Wrappers.<MarketMonthlyWindUpPO>lambdaQuery()
                                                                                        .eq(MarketMonthlyWindUpPO::getMonthlyReportId, marketMonthlyReportPO.getMonthlyReportId())
                                                                                        .eq(MarketMonthlyWindUpPO::getMarketTenant, marketMonthlyReportPO.getMarketTenant()));
        if (Objects.isNull(marketMonthlyWindUpPO)) {
            marketMonthlyWindUpPO = ObjectUtils.copying(marketMonthlyReportPO, MarketMonthlyWindUpPO.class);
            marketMonthlyReportPO.setId(null);
            this.baseMapper.insert(marketMonthlyWindUpPO);
        } else {
            ObjectUtils.copying(marketMonthlyReportPO, marketMonthlyWindUpPO);
            this.baseMapper.updateById(marketMonthlyWindUpPO);
        }
        return marketMonthlyWindUpPO.getId();
    }

    @Override
    public List<MarketMonthlyReportVO> getWindUpMarketReportsByMonthlyReportId(@NonNull Long monthlyReportId) {
        List<MarketMonthlyWindUpPO> marketMonthlyWindUpPOS = this.baseMapper.selectList(Wrappers.<MarketMonthlyWindUpPO>lambdaQuery()
                                                                                                .eq(MarketMonthlyWindUpPO::getMonthlyReportId, monthlyReportId));
        return ObjectUtils.copying(marketMonthlyWindUpPOS, MarketMonthlyReportVO.class);
    }
}
