package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksRankQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksStatusQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.LooseWorkMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.ProjectReformQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.AppSafetyWorkSituationVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.LooseWorksCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.LooseWorksDetailVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.ProjectRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.StackedBarChartVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm.AlarmEventTypeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksMarketTenantVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksRankVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksReformVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.ProjectReformOrderVO;
import com.guhecloud.platform.base.model.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 小散工程统计
 * @date 2023/9/8 10:59
 */
public interface LooseWorksStatisticService {
    Pagination<ProjectRecordVO> getProjectRecordStatistics(BaseQuery query);

    Pagination<LooseWorksDetailVO> getLooseWorksList(LooseWorksListQuery query);

    int looseWorksNumStatistics(BaseQuery query);

    LooseWorksCountVO countInfo(BaseQuery query);

    /**
     * @Author wql
     * @Description 统计进行中的小散工程数
     * @Date 16:08 2023/9/19
     **/
    Integer countUnderway(BaseQuery query);

    AppSafetyWorkSituationVO.WorkSituationVO getLooseWorksAbnormalSituation(BaseQuery query);

    LooseWorksStatisticsVO getLooseWorksStatusChainStatistics(BaseQuery query);

    Pagination<LooseWorksRankVO> getLooseWorksRankByMarket(@Param("query") LooseWorksRankQuery query);

    List<GroupCountVO> countStatus(BaseStatisticsQuery query);

    Integer count(LooseWorkMetricQuery query);

    Pagination<LooseWorksMarketTenantVO> getLooseWorksByStatus(LooseWorksStatusQuery query);

    LooseWorksDetailVO getLooseWorksById(Long id);

    Pagination<StackedBarChartVO> getLooseWorksStatusCountBar(LooseWorksStatusQuery query);

    List<AlarmEventTypeRatioVO> getLooseWorksStatusPie(LooseWorksStatusQuery query);

    /**
     * 小散工程整改数排名
     *
     * @param query
     *
     * @return
     */
    LooseWorksReformVO getLooseWorksReformList(BaseQuery query);

    /**
     * 小散工程整改单列表
     *
     * @param query
     *
     * @return
     */
    Pagination<ProjectReformOrderVO> getLooseWorksReformPage(ProjectReformQuery query);
}
