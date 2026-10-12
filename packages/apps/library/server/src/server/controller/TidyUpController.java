package com.guhecloud.cube.srv.gen.library.management.server.controller;

import com.guhecloud.cube.srv.gen.library.management.common.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.library.management.server.service.TidyUpService;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpStatisticsPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpRecordResult;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpStatisticsResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 整理(编目，操作员工作量
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TidyUpController {
    private final TidyUpService tidyUpService;

    /**
     * 整理编目记录列表(操作员工作量)
     *
     * @param param 入参
     *
     * @return 分页列表
     */
    @GetMapping("/library/tidyUp/tidyUpRecordPage")
    public Pagination<TidyUpRecordResult> tidyUpRecordPage(TidyUpRecordPageParam param) {
        return tidyUpService.tidyUpRecordPage(param);
    }

    /**
     * 编目整理统计分页列表
     *
     * @param param 入参
     *
     * @return 统计列表
     */
    @GetMapping("/library/tidyUp/tidyUpStatisticsPage")
    public Pagination<TidyUpStatisticsResult> tidyUpStatisticsPage(TidyUpStatisticsPageParam param) {
        return tidyUpService.tidyUpStatisticsPage(param);
    }

    /**
     * UE-月度编目工作量统计
     *
     * @param param 条件
     *
     * @return 月度编目工作量统计
     */
    @GetMapping("/library/ue/tidyUp/tidyUpRecordTrend")
    public List<KeyValueDTO> tidyUpRecordTrend(TidyUpRecordPageParam param) {
        if (param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            LocalDateTime nowDate = DateUtils.now();
            Period period = new Period(DateUtils.beginOfMonth(nowDate), DateUtils.parse(
                    DateUtils.toTimestamp(DateUtils.endOfMonth(nowDate)) - 500));
            param.setPeriod(period);
        }
        param.setPageNumber(1);
        param.setPageSize(100);
        return tidyUpService.tidyUpRecordTrend(param);
    }
}
