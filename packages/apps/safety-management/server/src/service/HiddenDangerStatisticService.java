package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.HiddenDangerListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.HiddenDangerVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerStatProcessVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenFinishedStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.po.HiddenDangerPO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * @author wql
 * @describe 隐患统计
 * @date 2023/9/8 10:57
 */
public interface HiddenDangerStatisticService {
    HiddenDangerStatisticsVO getHiddenDangerStatistics(BaseQuery query);

    Pagination<StackedBarChartVO> getHiddenDangerOverview(BaseQuery query);

    Pagination<HiddenDangerVO> getHiddenDangerList(HiddenDangerListQuery listQuery);

    List<String> getHiddenDangerCategoryList();

    Double getHandleRatio(BaseQuery query);

    HiddenFinishedStatisticsVO getHiddenFinishedStat(BaseQuery query);

    AppSafetyWorkSituationVO.WorkSituationVO getHandlerSituation(BaseQuery query);

    Integer countTaskNum(CountTaskNumQuery query);

    List<HiddenDangerPO> getHiddenDangerRecord(Period period, Integer marketTenant);

    List<GroupCountVO> countProcessStatus(BaseStatisticsQuery query);

    List<HiddenDangerStatProcessVO> statMarketProcessStatus(BaseStatisticsQuery query);
}
