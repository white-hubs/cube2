package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksRankQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.LooseWorksStatusQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.LooseWorkMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework.ProjectReformQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.ProjectRecordVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksMarketStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksMarketTenantVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.LooseWorksRankVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.loosework.ProjectReformOrderVO;
import com.guhecloud.cube.srv.gen.safety.management.po.LooseWorksPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wql
 * @describe 小散工程统计
 * @date 2023/9/19 15:18
 */
@Mapper
public interface LooseWorksStatisticMapper extends BaseMapper<LooseWorksPO> {
    Page<ProjectRecordVO> getProjectRecordStatistics(Page<?> page, @Param("query") BaseQuery query);

    Page<LooseWorksRankVO> selectLooseWorksRankByMarket(Page page, @Param("query") LooseWorksRankQuery query);

    Integer looseWorksNumStatistics(@Param("query") BaseQuery query);

    Page<LooseWorksPO> getLooseWorksPage(Page page, @Param("query") LooseWorksListQuery query);

    List<LooseWorksPO> getLooseWorksList(@Param("query") BaseQuery query);

    Integer count(@Param("query") LooseWorkMetricQuery query);

    Integer getLooseWorksCountByStatus(@Param("query") BaseQuery query, @Param("status") String status,
                                       @Param("hasProblem") Boolean hasProblem
                                      );

    @Select({
            "<script>",
            "SELECT",
            "lw.status AS groupKey,",
            "COUNT(1) AS count",
            "FROM loose_works lw",
            "left join market_config mc on mc.market_tenant = lw.market_tenant",
            "<where>",
            "mc.status = true ",
            "<if test=\"query != null\">",
            "<if test=\"query.marketTenantIDs != null and query.marketTenantIDs.size() > 0\">" +
            "AND market_tenant IN " +
            "<foreach item=\"id\" collection=\"query.marketTenantIDs\" open=\"(\" separator=\",\" close=\")\">" +
            "#{id}" +
            "</foreach>" +
            "</if>",
            "<if test=\"query.createdPeriod != null\">" +
            "AND create_time BETWEEN #{query.createdPeriod.begin} AND #{query.createdPeriod.end}" +
            "</if>",
            "</if>",
            "</where>",
            "GROUP BY lw.status",
            "</script>"
    }
    )
    List<GroupCountVO> countStatus(@Param("query") BaseStatisticsQuery query);

    List<LooseWorksMarketTenantVO> getLooseWorksByStatus(@Param("query") LooseWorksStatusQuery query);

    List<LooseWorksMarketTenantVO> getLooseWorksStatus(@Param("query") LooseWorksStatusQuery query);

    List<LooseWorksMarketStatusVO> getLooseWorksMarketStatus(@Param("query") LooseWorksStatusQuery query);

    Integer selectProjectReformCount(@Param("query") BaseQuery query);

    Page<ProjectRecordVO> selectProjectReformNumStatistics(Page<?> page, @Param("query") BaseQuery query);

    Page<ProjectReformOrderVO> selectProjectReformPage(Page<?> page, @Param("query") ProjectReformQuery query);
}
