package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamTaskQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.open.ExamTaskOpenQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamTaskStandardVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.UserExamTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskAttendanceStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskStatisticsPageExportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskStatisticsPageVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskThemeRatioStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskTrendStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskTypeRatioStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query.ExamTaskStatisticsPageQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query.ExamTaskStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.interfaces.vo.SafetyEducationThemeRatioVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.query.SafetyEducationStatisticsQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ExamTaskMapper extends BaseMapper<ExamTaskPO> {

    Page<ExamTaskVO> selectExamTaskPage(Page page, @Param("tenant") Integer tenant, @Param("query") ExamTaskQuery query);

    Page<UserExamTaskVO> selectUserExamTaskPage(Page page, @Param("tenant") Integer tenant, @Param("query") ExamTaskQuery query);

    Page<UserExamTaskVO> selectPublicUserExamTaskPage(Page page, @Param("query") ExamTaskOpenQuery query);

    List<ExamTaskStandardVO> selectExamTaskStandardList(@Param("tenant") Integer tenant, @Param("query") ExamTaskQuery examTaskQuery);

    Page<ExamTaskStatisticsPageVO> selectExamTaskStatisticsPage(Page page, @Param("tenant") Integer tenant,
                                                                @Param("query") ExamTaskStatisticsPageQuery query
                                                               );

    List<ExamTaskStatisticsPageVO> selectExamTaskStatisticsList(@Param("tenant") Integer tenant,
                                                                @Param("query") ExamTaskStatisticsPageQuery query
                                                               );

    List<ExamTaskStatisticsPageExportVO> selectExamTaskStatisticsExportList(@Param("tenant") Integer tenant,
                                                                            @Param("query") ExamTaskStatisticsPageQuery query
                                                                           );

    List<ExamTaskThemeRatioStatisticsVO> selectExamTaskThemeRatioStatistics(@Param("tenant") Integer tenant,
                                                                            @Param("query") ExamTaskStatisticsQuery query
                                                                           );

    List<ExamTaskTypeRatioStatisticsVO> selectExamTaskTypeRatioStatistics(@Param("tenant") Integer tenant,
                                                                          @Param("query") ExamTaskStatisticsQuery query
                                                                         );

    List<ExamTaskAttendanceStatisticsVO> selectExamTaskAttendanceStatistics(@Param("tenant") Integer tenant,
                                                                            @Param("query") ExamTaskStatisticsQuery query
                                                                           );

    List<ExamTaskTrendStatisticsVO> selectExamTaskMonthTrendStatistics(@Param("tenant") Integer tenant,
                                                                       @Param("query") ExamTaskStatisticsQuery query
                                                                      );

    Integer selectExamTaskCount(@Param("tenant") Integer tenant, @Param("query") ExamTaskStatisticsQuery query);

    Integer selectExamTaskNoPassedUserCount(@Param("tenant") Integer tenant, @Param("query") ExamTaskStatisticsQuery query);

    Integer selectExamTaskMissUserCount(@Param("tenant") Integer tenant, @Param("query") ExamTaskStatisticsQuery query);

    List<SafetyEducationThemeRatioVO> selectExamThemeRatio(@Param("tenant") Integer tenant,
                                                           @Param("query") SafetyEducationStatisticsQuery query
                                                          );

    Integer selectVisualExamTaskCount(@Param("tenant") Integer tenant, @Param("query") SafetyEducationStatisticsQuery query);

    Long selectExamUserCount(@Param("query") SafetyEducationStatisticsQuery query);

    Integer selectUsedExamTaskCount(@Param("paperIds") Set<Long> paperIds);
}
