package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.FilingDeviceQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.PropertyFilingNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.risksource.RiskSourceMetricQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.MarketTenantBarVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.MarketNumStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.property.RiskSourceCountStatusVO;
import com.guhecloud.cube.srv.gen.safety.management.po.PropertyFilingPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wql
 * @describe 资产建档数据统计
 * @date 2023/9/13 16:47
 */
@Mapper
public interface PropertyStatisticsMapper extends BaseMapper<PropertyFilingPO> {
    List<MarketNumStatisticsVO> getDeviceExpireStatistics(@Param("query") BaseQuery query,
                                                          @Param("type") String type, @Param("filterTime") String filterTime
                                                         );

    Page<MarketNumStatisticsVO> getDeviceExpireStatisticsPage(Page page, @Param("query") BaseQuery query,
                                                              @Param("type") String type, @Param("filterTime") String filterTime
                                                             );

    Integer selectTenantCount();

    List<RiskSourceCountStatusVO> countStatusByType(@Param("query") BaseStatisticsQuery query, @Param("types") List<String> types);

    Page<PropertyFilingPO> getPropertyFilingPage(Page page, @Param("query") FilingDeviceQuery query);

    List<PropertyFilingPO> getPropertyFilingList(@Param("query") BaseQuery query);

    List<PropertyFilingPO> getPropertyFilingNumList(@Param("query") PropertyFilingNumQuery query);

    List<String> getPropertyTypeList();

    List<GroupCountVO> countByType(@Param("query") RiskSourceMetricQuery query);

    @Select({
            "<script>",
            "SELECT",
            "t.market_tenant AS groupKey,",
            "COUNT(1) AS count",
            "FROM property_filing t",
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
            "GROUP BY t.market_tenant",
            "</script>"
    }
    )
    List<GroupCountVO> countByMarket(@Param("query") BaseStatisticsQuery query);

    List<MarketTenantBarVO> countTypeTenant(@Param("query") RiskSourceMetricQuery query);
}
