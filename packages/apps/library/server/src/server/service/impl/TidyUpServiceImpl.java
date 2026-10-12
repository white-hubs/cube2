package com.guhecloud.cube.srv.gen.library.management.server.service.impl;

import com.guhecloud.cube.srv.gen.library.management.common.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.library.management.server.service.TidyUpService;
import com.guhecloud.cube.srv.gen.library.management.server.third.lamp.service.ThirdTidyUpService;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpStatisticsPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpRecordResult;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpStatisticsResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (编目，操作员工作量
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TidyUpServiceImpl implements TidyUpService {
    private final ThirdTidyUpService thirdTidyUpService;

    /**
     * 整理编目记录列表(操作员工作量)
     *
     * @param param 入参
     *
     * @return 分页列表
     */
    @Override
    public Pagination<TidyUpRecordResult> tidyUpRecordPage(TidyUpRecordPageParam param) {
        return thirdTidyUpService.tidyUpRecordPage(param);
    }

    /**
     * 编目整理统计分页列表
     *
     * @param param 入参
     *
     * @return 统计列表
     */
    @Override
    public Pagination<TidyUpStatisticsResult> tidyUpStatisticsPage(TidyUpStatisticsPageParam param) {
        return thirdTidyUpService.tidyUpStatisticsPage(param);
    }

    /**
     * UE-月度编目工作量统计
     *
     * @param param 条件
     *
     * @return 月度编目工作量统计
     */
    @Override
    public List<KeyValueDTO> tidyUpRecordTrend(TidyUpRecordPageParam param) {
        List<KeyValueDTO> resultList = new ArrayList<>();
        List<TidyUpRecordResult> allList = new ArrayList<>();
        TidyUpRecordPageParam thirdParam = new TidyUpRecordPageParam();
        ObjectUtils.copying(param, thirdParam);
        this.getAllTidyUpRecordResult(allList, thirdParam);
        if (allList.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, List<TidyUpRecordResult>> map = allList.stream()
                .collect(Collectors.groupingBy(r -> DateUtils.formatDate(r.getTidyTime())));
        map.forEach((dateString, list) -> {
            Long sum = list.stream().mapToLong(r -> {
                if (r.getTidyNumber() != null) {
                    return r.getTidyNumber();
                }
                return 0L;
            }).sum();

            KeyValueDTO dto = new KeyValueDTO();
            dto.setKey(dateString);
            dto.setValue(String.valueOf(sum));
            resultList.add(dto);
        });
        // 排序
        resultList.sort(Comparator.comparing(KeyValueDTO::getKey));
        return resultList;
    }

    private void getAllTidyUpRecordResult(List<TidyUpRecordResult> allList, TidyUpRecordPageParam param) {
        Pagination<TidyUpRecordResult> pagination = thirdTidyUpService.tidyUpRecordPage(param);
        if (!CollectionUtils.isEmpty(pagination.getRecords())) {
            allList.addAll(pagination.getRecords());
        }
        if (param.getPageNumber() < pagination.getPages()) {
            param.setPageNumber(param.getPageNumber() + 1);
            this.getAllTidyUpRecordResult(allList, param);
        }
    }

}
