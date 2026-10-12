package com.guhecloud.cube.srv.gen.library.management.server.service;

import com.guhecloud.cube.srv.gen.library.management.common.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpStatisticsPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpRecordResult;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpStatisticsResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * (编目，操作员工作量
 */
public interface TidyUpService {

    Pagination<TidyUpRecordResult> tidyUpRecordPage(TidyUpRecordPageParam param);

    Pagination<TidyUpStatisticsResult> tidyUpStatisticsPage(TidyUpStatisticsPageParam param);

    /**
     * UE-月度编目工作量统计
     *
     * @param param 条件
     *
     * @return 月度编目工作量统计
     */
    List<KeyValueDTO> tidyUpRecordTrend(TidyUpRecordPageParam param);
}
