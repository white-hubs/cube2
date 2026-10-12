package com.guhecloud.cube.srv.gen.safety.management.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.management.dao.SafetyInspectRecordMapper;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionRecordQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyInspectRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyInspectRecordPO;
import com.guhecloud.cube.srv.gen.safety.management.service.SafetyInspectRecordService;
import com.guhecloud.cube.srv.gen.safety.management.util.CommonUtil;
import com.guhecloud.cube.srv.gen.safety.management.util.DateUnitUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyInspectRecordServiceImpl extends ServiceImpl<SafetyInspectRecordMapper, SafetyInspectRecordPO> implements SafetyInspectRecordService {
    private final SafetyInspectRecordMapper safetyInspectRecordMapper;

    /**
     * 巡检排名环比
     *
     * @param query 条件
     *
     * @return 巡检排名环比
     */
    @Override
    public Pagination<MarketStatisticsInfo> getInspectionRecordMarketStatistics(MarketInspectionRecordQuery query) {
        List<MarketStatisticsInfo> newPage = this.baseMapper.getInspectionRecordMarketStatistics(query);
        Period lastPeriod = DateUnitUtils.getPreviousPeriod(query.getType(), query.getPeriod());
        query.setPeriod(lastPeriod);
        List<MarketStatisticsInfo> oldPage = this.baseMapper.getInspectionRecordMarketStatistics(query);
        if (ObjectUtils.isNotEmpty(newPage) && (ObjectUtils.isNotEmpty(oldPage))) {
            Map<Integer, List<MarketStatisticsInfo>> map = oldPage.stream()
                    .collect(Collectors.groupingBy(MarketStatisticsInfo::getMarketTenant));
            newPage.forEach(vo -> {
                List<MarketStatisticsInfo> tempList = map.get(vo.getMarketTenant());
                long oldNum = 0L;
                if (!CollectionUtils.isEmpty(tempList) && tempList.get(0).getTaskNum() != null) {
                    oldNum = tempList.get(0).getTaskNum();
                }
                vo.setRatio(CommonUtil.countChainRatio(vo.getTaskNum(), oldNum));
            });

        }
        newPage.forEach(vo -> vo.setRatio(vo.getRatio() == null ? 0D : vo.getRatio()));
        return new Pagination<>((long) newPage.size(), (long) Math.ceil((double) newPage.size() / query.getPageSize()),
                                newPage.stream()
                                        .sorted(Comparator.comparing(MarketStatisticsInfo::getTaskNum, Comparator.reverseOrder())
                                                        .thenComparing(MarketStatisticsInfo::getTaskNum, Comparator.reverseOrder()))
                                        .skip((long) (query.getPageNumber() - 1) * query.getPageSize())
                                        .limit(query.getPageSize())
                                        .collect(Collectors.toList()));
    }

    /**
     * 巡检记录
     *
     * @param query 条件
     *
     * @return 巡检记录
     */
    @Override
    public Pagination<SafetyInspectRecordVO> safetyRecordPage(MarketInspectionRecordQuery query) {
        Page<SafetyInspectRecordVO> page = safetyInspectRecordMapper.safetyRecordPage(PagingUtils.convert(query), query);
        return PagingUtils.convert(page);
    }

    /**
     * 异常项的巡检记录
     *
     * @param query 条件
     *
     * @return 巡检记录
     */
    @Override
    public Pagination<SafetyInspectRecordVO> safetyErrorRecordPage(MarketInspectionRecordQuery query) {
        Page<SafetyInspectRecordVO> page = safetyInspectRecordMapper.safetyErrorRecordPage(PagingUtils.convert(query), query);
        return PagingUtils.convert(page);
    }

    @Override
    public Pagination<SafetyInspectRecordVO> getSafetyInspectRecordPage(MarketInspectionRecordQuery query) {
        query.setStatus("OVER");
        Page<SafetyInspectRecordVO> page = safetyInspectRecordMapper.safetyRecordPage(PagingUtils.convert(query), query);
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page);
    }

}
