package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.ExamTaskPageVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education.SafetyEducationMarketTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.management.po.ExamTaskPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository(value = "safetyExamTaskMapper")
public interface ExamTaskMapper extends BaseMapper<ExamTaskPO> {

    /**
     * 考试任务主题统计
     *
     * @param query
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<SafetyEducationThemeRatioVO> selectExamThemeRatio(@Param("query") SafetyEducationStatisticsQuery query);

    /**
     * 考试任务数量
     *
     * @param query
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Integer selectExamTaskCount(@Param("query") SafetyEducationStatisticsQuery query);

    /**
     * 市场考试任务统计列表
     *
     * @param query
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Page<SafetyEducationMarketTaskStatisticsVO> selectMarketExamStatistics(Page page, @Param("query") SafetyEducationStatisticsQuery query);

    /**
     * 考试任务分页列表
     *
     * @param page
     * @param query
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Page<ExamTaskPageVO> selectExamTaskPage(Page page, @Param("query") SafetyEducationStatisticsQuery query);
}
