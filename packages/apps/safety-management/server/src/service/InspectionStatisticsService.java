package com.guhecloud.cube.srv.gen.safety.management.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.inspect.InspectBaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.InspectTaskVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.InspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.inspect.InspectCompleteStatVO;
import com.guhecloud.cube.srv.gen.safety.management.po.InspectTaskPO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author wql
 * @describe 巡检统计
 * @date 2023/9/8 10:52
 */
public interface InspectionStatisticsService extends IService<InspectTaskPO> {
    InspectionStatisticsVO getInspectionStatistics(BaseQuery query);

    Pagination<InspectTaskVO> getInspectionPage(InspectionListQuery inspectionListParam);

    List<String> getInspectionTypes();

    Double getTaskCompleteRatio(BaseQuery query);

    InspectCompleteStatVO getCompleteStat(BaseQuery query);

    Integer countTaskNum(CountTaskNumQuery query);

    Pagination<MarketStatisticsInfo> getInspectionMarketStatistics(MarketInspectionQuery query);

    /**
     * 统计符合条件的记录数
     *
     * @param query
     *
     * @return
     */
    Integer count(InspectBaseQuery query);

    List<GroupCountVO> countStatus(BaseStatisticsQuery query);

    Pagination<MarketStatisticsInfo> getInspectionMarketErrorRecordStatistics(MarketInspectionQuery query);
}
