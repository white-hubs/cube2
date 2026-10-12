package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.CountTaskNumQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.HiddenDangerListQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.GroupCountVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerCountStatusOfMarketVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.hidden.HiddenDangerOverviewPageVO;
import com.guhecloud.cube.srv.gen.safety.management.po.HiddenDangerPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author wql
 * @describe 隐患单统计
 * @date 2023/9/11 16:46
 */
@Mapper
public interface HiddenDangerStatisticMapper extends BaseMapper<HiddenDangerPO> {

    Page<HiddenDangerOverviewPageVO> countGroupByMarket(Page page, @Param("query") BaseQuery query);

    List<HiddenDangerCountStatusOfMarketVO> countStatusGroupByMarket(@Param("marketTenantIDs") List<String> marketTenantIDs,
                                                                     @Param("createdPeriod") Period createdPeriod
                                                                    );

    List<GroupCountVO> countProcessStatus(@Param("query") BaseStatisticsQuery query);

    List<HiddenDangerPO> getHiddenDangerList(@Param("query") BaseQuery query);

    Page<HiddenDangerPO> getHiddenDangerPage(Page page, @Param("query") HiddenDangerListQuery listQuery);

    Integer countByStatus(@Param("query") BaseQuery query, @Param("status") Set<String> status);

    List<String> getHiddenDangerCategoryList();

    Integer countTaskNum(@Param("query") CountTaskNumQuery query);
}
