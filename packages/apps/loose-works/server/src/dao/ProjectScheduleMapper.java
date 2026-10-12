package com.guhecloud.cube.srv.gen.loose.works.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectScheduleQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectTrendStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.TrendStatisticsQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSchedulePO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 工程进度
 * @date 2023/9/14 18:35
 */
@Mapper
public interface ProjectScheduleMapper extends BaseMapper<ProjectSchedulePO> {
    Page<ProjectSchedulePO> getProjectScheduleList(Page page, @Param("query") ProjectScheduleQuery query);

    /**
     * 小散趋势统计
     *
     * @param param
     *
     * @return
     */
    List<ProjectTrendStatisticsVO> selectProjectTrendStatistics(@Param("param") TrendStatisticsQueryParam param);

    @InterceptorIgnore(tenantLine = "on")
    List<ProjectSchedulePO> selectAll(@Param("period") Period period, @Param("tenant") Integer tenant);
}
