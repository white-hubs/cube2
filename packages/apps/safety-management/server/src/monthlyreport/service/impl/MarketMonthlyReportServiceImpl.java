package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao.MarketMonthlyReportMapper;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MarketMonthlyReportQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.enumeration.MarketMonthlyReportStatusEnum;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.event.MarketMonthlyReportWindUpEvent;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportPO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyReportService;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-07  16:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketMonthlyReportServiceImpl extends ServiceImpl<MarketMonthlyReportMapper, MarketMonthlyReportPO> implements MarketMonthlyReportService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Pagination<MarketMonthlyReportVO> queryByPage(MarketMonthlyReportQuery query) {
        Page<MarketMonthlyReportPO> page = this.baseMapper.selectPage(PagingUtils.convert(query),
                                                                      Wrappers.<MarketMonthlyReportPO>lambdaQuery()
                                                                              .select(MarketMonthlyReportPO.class, tableFieldInfo -> !"reportData".equals(tableFieldInfo.getProperty()))
                                                                              .like(StringUtils.isNotEmpty(query.getKeyword()), MarketMonthlyReportPO::getTitle, query.getKeyword())
                                                                              .eq(Objects.nonNull(query.getMarketTenant()), MarketMonthlyReportPO::getMarketTenant, query.getMarketTenant())
                                                                              .orderByDesc(MarketMonthlyReportPO::getMonthDate));
        List<MarketMonthlyReportVO> marketMonthlyReportVOS = ObjectUtils.copying(page.getRecords(), MarketMonthlyReportVO.class);
        marketMonthlyReportVOS.stream().forEach(marketMonthlyReportVO -> {
            if (StringUtils.isNotEmpty(marketMonthlyReportVO.getStatus())) {
                marketMonthlyReportVO.setStatusName(MarketMonthlyReportStatusEnum.valueOf(marketMonthlyReportVO.getStatus()).getRemarks());
            }
        });
        return PagingUtils.convert(page, marketMonthlyReportVOS);
    }

    @Override
    public List<MarketMonthlyReportVO> getMarketReportsByMonthlyReportId(Long monthlyReportId) {
        List<MarketMonthlyReportPO> marketMonthlyReportPOS = this.baseMapper.selectList(Wrappers.<MarketMonthlyReportPO>lambdaQuery()
                                                                                                .eq(MarketMonthlyReportPO::getMonthlyReportId, monthlyReportId));
        if (CollectionUtils.isEmpty(marketMonthlyReportPOS)) {
            return Collections.emptyList();
        }
        List<MarketMonthlyReportVO> marketMonthlyReportVOS = ObjectUtils.copying(marketMonthlyReportPOS, MarketMonthlyReportVO.class);
        return marketMonthlyReportVOS;
    }

    @Override
    public Boolean escalation(@NonNull Long id) {
        MarketMonthlyReportPO marketMonthlyReportPO = this.baseMapper.selectById(id);
        Assert.isNotFound(marketMonthlyReportPO, "提交失败,月报信息不存在");
        var monthDate = marketMonthlyReportPO.getMonthDate();
        var offsetMonth = DateUtils.offsetDay(DateUtils.offsetMonth(monthDate, 1), 10);
        var endOffsetMonth = DateUtils.endOfDay(offsetMonth);
        var now = LocalDateTime.now();
        Assert.isNotAcceptable(endOffsetMonth.isBefore(now) && !endOffsetMonth.isEqual(now), "无法填报,月报需要生成后次月10号前完成填报");
        marketMonthlyReportPO.setStatus(MarketMonthlyReportStatusEnum.WIND_UP.name());
        marketMonthlyReportPO.setReportTime(LocalDateTime.now());
        marketMonthlyReportPO.setReportUserId(
                Objects.nonNull(SecurityContextHolder.getPrincipal(true)) ? SecurityContextHolder.getPrincipal().getId() : null);
        SessionContextHolder.requestTenant(marketMonthlyReportPO.getTenant());
        boolean updateResult = this.updateById(marketMonthlyReportPO);
        //发送上报事件
        this.applicationEventPublisher.publishEvent(new MarketMonthlyReportWindUpEvent(this, marketMonthlyReportPO));
        return updateResult;
    }

    @Override
    public Long createMonthlyReport(@NonNull String title,
                                    @NonNull Long monthlyReportId,
                                    @NonNull String monthly,
                                    @NonNull LocalDateTime monthDate,
                                    @NonNull MarketConfigPO marketConfig
                                   ) {
        MarketMonthlyReportPO marketMonthlyReportPO = MarketMonthlyReportPO.builder()
                .title(title)
                .monthlyReportId(monthlyReportId)
                .monthly(monthly)
                .monthDate(monthDate)
                .status(MarketMonthlyReportStatusEnum.NOT_STARTED.name())
                .tenant(SessionContextHolder.getRequester().getTenant())
                .marketTenant(marketConfig.getMarketTenant())
                .marketName(marketConfig.getMarketName())
                .build();
        EntityAuditor.populateInnerAudit(marketMonthlyReportPO);
        this.baseMapper.insert(marketMonthlyReportPO);
        return marketMonthlyReportPO.getId();
    }
}
