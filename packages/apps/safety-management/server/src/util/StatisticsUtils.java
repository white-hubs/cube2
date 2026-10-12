package com.guhecloud.cube.srv.gen.safety.management.util;

import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticsUtils {

    private StatisticsUtils() {}

    /**
     * 排名分页
     *
     * @param newList    最新集合
     * @param oldList    环比集合
     * @param pageNumber 页数
     * @param pageSize   每页条数
     *
     * @return 排名分页
     */
    public static Pagination<MarketStatisticsInfo> marketStatisticsPageHandler(List<MarketStatisticsInfo> newList,
                                                                               List<MarketStatisticsInfo> oldList, Integer pageNumber,
                                                                               Integer pageSize
                                                                              ) {
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        if (ObjectUtils.isNotEmpty(newList)) {
            if (ObjectUtils.isNotEmpty(oldList)) {
                Map<Integer, List<MarketStatisticsInfo>> collect = oldList.stream()
                        .collect(Collectors.groupingBy(MarketStatisticsInfo::getMarketTenant));
                newList.forEach(vo -> {
                    MarketStatisticsInfo oldInfo = collect.get(vo.getMarketTenant()).get(0);
                    vo.setRatio(CommonUtil.countChainRatio(vo.getTaskNum(), oldInfo.getTaskNum()));
                });
            }
        }
        newList.forEach(vo -> vo.setRatio(vo.getRatio() == null ? 0D : vo.getRatio()));

        return new Pagination<>((long) newList.size(), (long) Math.ceil((double) newList.size() / pageSize),
                                newList.stream()
                                        .sorted(Comparator.comparing(MarketStatisticsInfo::getTaskNum, Comparator.reverseOrder())
                                                        .thenComparing(MarketStatisticsInfo::getMarketTenant, Comparator.reverseOrder()))
                                        .skip((long) (pageNumber - 1) * pageSize)
                                        .limit(pageSize)
                                        .collect(Collectors.toList()));
    }
}
