package com.guhecloud.cube.srv.gen.library.management.server.third.lamp.service;

import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpStatisticsPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpRecordResult;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpStatisticsResult;
import com.guhecloud.platform.base.model.Pagination;

public interface ThirdTidyUpService {
    Pagination<TidyUpRecordResult> tidyUpRecordPage(TidyUpRecordPageParam param);

    Pagination<TidyUpStatisticsResult> tidyUpStatisticsPage(TidyUpStatisticsPageParam param);
}
