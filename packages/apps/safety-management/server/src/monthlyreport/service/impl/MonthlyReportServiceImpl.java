package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao.MarketMonthlyReportMapper;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao.MarketMonthlyWindUpMapper;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao.MonthlyReportMapper;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MonthlyReportQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MonthlyMarketCountStatistics;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MonthlyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.enumeration.MonthlyReportStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MonthlyReportService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonthlyReportServiceImpl extends ServiceImpl<MonthlyReportMapper, MonthlyReportPO> implements MonthlyReportService {

    private final MarketMonthlyReportMapper marketMonthlyReportMapper;

    private final MarketMonthlyWindUpMapper marketMonthlyWindUpMapper;

    @Override
    public Pagination<MonthlyReportVO> queryPage(MonthlyReportQuery query) {
        Page<MonthlyReportPO> page = this.baseMapper.selectPage(PagingUtils.convert(query), Wrappers.<MonthlyReportPO>lambdaQuery()
                .select(MonthlyReportPO.class, tableFieldInfo -> !"reportData".equals(tableFieldInfo.getProperty()))
                .like(StringUtils.isNotEmpty(query.getKeyword()), MonthlyReportPO::getTitle, query.getKeyword())
                .orderByDesc(MonthlyReportPO::getCreateTime));
        List<MonthlyReportVO> monthlyReportVOS = ObjectUtils.copying(page.getRecords(), MonthlyReportVO.class);
        if (CollectionUtils.isEmpty(monthlyReportVOS)) {
            return Pagination.empty();
        }
        Set<Long> ids = page.getRecords().stream().map(MonthlyReportPO::getId).collect(Collectors.toSet());
        List<MonthlyMarketCountStatistics> marketCountStatistics = this.marketMonthlyReportMapper.selectMonthlyReportCount(ids);
        List<MonthlyMarketCountStatistics> windUpCountStatistics = this.marketMonthlyWindUpMapper.selectMonthlyWindUpCount(ids);
        monthlyReportVOS.stream().forEach(monthlyReportVO -> {
            Optional<MonthlyMarketCountStatistics> optMonthlyMarketCountStatistics = marketCountStatistics.stream()
                    .filter(marketCount -> monthlyReportVO.getId().equals(marketCount.getId()))
                    .findAny();
            Optional<MonthlyMarketCountStatistics> optWindUpCountStatistics = windUpCountStatistics.stream()
                    .filter(marketCount -> monthlyReportVO.getId().equals(marketCount.getId()))
                    .findAny();
            MonthlyReportVO.ReportStatisticsVO reportStatisticsVO = new MonthlyReportVO.ReportStatisticsVO(0, 0, 0);
            optMonthlyMarketCountStatistics.ifPresent(marketCount -> reportStatisticsVO.setMarketCount(marketCount.getTotalNum()));
            optWindUpCountStatistics.ifPresent(windUpCount -> reportStatisticsVO.setWindUpCount(windUpCount.getTotalNum()));
            monthlyReportVO.setReportStatistics(reportStatisticsVO);
        });
        return PagingUtils.convert(page, monthlyReportVOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMonthlyReport(@NonNull String title, @NonNull String monthly, @NonNull LocalDateTime monthDate) {
        MonthlyReportPO monthlyReportPO = MonthlyReportPO.builder()
                .title(title)
                .monthly(monthly)
                .monthDate(monthDate)
                .status(MonthlyReportStatusEnum.NOT_STARTED.name())
                .build();
        EntityAuditor.populateInnerAudit(monthlyReportPO);
        this.baseMapper.insert(monthlyReportPO);
        return monthlyReportPO.getId();
    }

}
