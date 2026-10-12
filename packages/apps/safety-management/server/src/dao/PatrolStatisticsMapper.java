package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.MarketInspectionQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.PatrolListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketStatisticsInfo;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.StatusNumStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.po.PatrolTaskPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wql
 * @describe 电子巡更统计
 * @date 2023/9/11 13:46
 */
@Mapper
public interface PatrolStatisticsMapper extends BaseMapper<PatrolTaskPO> {
    List<StatusNumStatisticsVO> getPatrolStatusStatistics(@Param("query") BaseQuery query);

    Integer countTaskNum(@Param("query") CountTaskNumQuery query);

    List<PatrolTaskPO> getPatrolTaskList(@Param("query") BaseQuery query, @Param("status") String status);

    Page<PatrolTaskPO> getPatrolTaskPage(Page page, @Param("query") PatrolListQuery patrolListQuery);

    @Select({
            "<script>",
            "SELECT",
            "t.status_code AS groupKey,",
            "COUNT(1) AS count",
            "FROM safety_patrol_task t",
            "left join market_config mc on mc.market_tenant = t.market_tenant",
            "<where>",
            "mc.status = true",
            "<if test=\"query != null\">",
            "<if test=\"query.marketTenantIDs != null and query.marketTenantIDs.size() > 0\">" +
            "AND t.market_tenant IN " +
            "<foreach item=\"id\" collection=\"query.marketTenantIDs\" open=\"(\" separator=\",\" close=\")\">" +
            "#{id}" +
            "</foreach>" +
            "</if>",
            "<if test=\"query.createdPeriod != null\">" +
            "AND t.create_time BETWEEN #{query.createdPeriod.begin} AND #{query.createdPeriod.end}" +
            "</if>",
            "</if>",
            "</where>",
            "GROUP BY t.status_code",
            "</script>"
    }
    )
    List<GroupCountVO> countStatus(@Param("query") BaseStatisticsQuery query);

    /**
     * 获取错误数
     *
     * @param query period、marketTenant
     *
     * @return 数量
     */
    Integer getPatrolErrorNum(@Param("query") BaseQuery query);

    /**
     * 获取巡更市场统计
     *
     * @param query
     *
     * @return
     */
    List<MarketStatisticsInfo> getPatrolMarketStatistics(@Param("query") MarketInspectionQuery query);
}
