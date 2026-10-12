package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.InspectionListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.po.InspectTaskPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 日常巡检统计
 * @date 2023/9/11 13:45
 */
@Mapper
public interface InspectionStatisticsMapper extends BaseMapper<InspectTaskPO> {

    List<MarketStatisticsInfo> getInspectionMarketStatistics(@Param("query") MarketInspectionQuery query);

    /**
     * 异常巡检统计
     *
     * @param query
     *
     * @return
     */
    List<MarketStatisticsInfo> getInspectionMarketErrorRecordStatistics(@Param("query") MarketInspectionQuery query);

    List<InspectTaskPO> getInspectionTaskList(@Param("query") BaseQuery query);

    Page<InspectTaskPO> getInspectionTaskPage(Page page, @Param("query") InspectionListQuery query);

    List<String> getInspectionTypes(@Param("query") BaseQuery query);

    Integer countTaskNum(@Param("query") CountTaskNumQuery query);

    List<GroupCountVO> countStatus(@Param("query") BaseStatisticsQuery query);
}
