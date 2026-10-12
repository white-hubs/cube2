package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.CultivateTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education.SafetyEducationMarketTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.po.CultivateTaskPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository(value = "safetyCultivateTaskMapper")
public interface CultivateTaskMapper extends BaseMapper<CultivateTaskPO> {

    /**
     * 培训任务主题统计
     *
     * @param query
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<SafetyEducationThemeRatioVO> selectCultivateThemeRatio(@Param("query") SafetyEducationStatisticsQuery query);

    /**
     * 培训任务数量
     *
     * @param query
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Integer selectCultivateCount(@Param("query") SafetyEducationStatisticsQuery query);

    /**
     * 市场培训任务数量统计
     *
     * @param query
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Page<SafetyEducationMarketTaskStatisticsVO> selectMarketCultivateStatistics(Page page,
                                                                                @Param("query") SafetyEducationStatisticsQuery query
                                                                               );

    /**
     * 培训任务分页列表
     *
     * @param page
     * @param query
     *
     * @return
     */
    Page<CultivateTaskPageVO> selectCultivateTaskPage(Page page,
                                                      @Param("query") SafetyEducationStatisticsQuery query
                                                     );
}
