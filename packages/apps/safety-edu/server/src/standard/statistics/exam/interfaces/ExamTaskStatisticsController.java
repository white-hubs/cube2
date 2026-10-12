package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamTaskUserService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.ExamTaskPaperQueryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskPaperPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamTaskUserPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.repository.mapper.ExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskAttendanceStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskStatisticsPageExportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskStatisticsPageVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskThemeRatioStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskTrendStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskTypeRatioStatisticsVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskUserStatisticsPageExportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo.ExamTaskUserStatisticsPageVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query.ExamTaskStatisticsPageQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query.ExamTaskStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.query.ExamTaskStatisticsUserQuery;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 考试统计接口管理
 *
 * @author lanchun
 * @date 2023/4/20 17:34
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamTaskStatisticsController {

    private final ExamTaskMapper examTaskMapper;

    private final ExamTaskUserService examTaskUserService;

    private final ExamTaskPaperQueryService examTaskPaperQueryService;

    /**
     * 考试分页列表统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-task-statistics/page")
    public Pagination<ExamTaskStatisticsPageVO> getExamTaskStatisticsPage(@Valid ExamTaskStatisticsPageQuery query) {
        Page<ExamTaskStatisticsPageVO> page = this.examTaskMapper.selectExamTaskStatisticsPage(PagingUtils.convert(query), SessionContextHolder
                .getRequester().getTenant(), query);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        Set<Long> taskIds = page.getRecords().stream().map(ExamTaskStatisticsPageVO::getId).collect(Collectors.toSet());
        Map<Long, List<ExamTaskPaperPO>> examTaskPaperPOMap = this.examTaskPaperQueryService.getPublishedExamPapers(taskIds)
                .stream()
                .collect(Collectors.groupingBy(ExamTaskPaperPO::getExamTaskId));
        page.getRecords().forEach(examTaskStatisticsPageVO -> {
            if (examTaskPaperPOMap.containsKey(examTaskStatisticsPageVO.getId())) {
                List<ExamTaskPaperPO> examTaskPaperPOS = examTaskPaperPOMap.get(examTaskStatisticsPageVO.getId());
                examTaskStatisticsPageVO.setExamPaperScore(examTaskPaperPOS.get(0).getExamPaperScore());
                examTaskStatisticsPageVO.setExamPaperName(getExamTaskPaperName(examTaskPaperPOS));
            }
        });
        return PagingUtils.convert(page);
    }

    /**
     * 考试分页列表统计导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/exam-task-statistics/page/export")
    public void exportPatrolLineStatistics(@Valid @RequestBody ExamTaskStatisticsPageQuery param) {
        ServletRequestAttributes attr = null;
        HttpServletResponse response = null;
        try {
            attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attr != null) {
                response = attr.getResponse();
                if (response != null) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setCharacterEncoding("utf-8");
                    // 这里URLEncoder.encode可以防止中文乱码
                    String excelName = URLEncoder.encode("考试统计", "UTF-8");
                    response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
                    List<ExamTaskStatisticsPageExportVO> examTaskStatisticsPageExportVOS = this.examTaskMapper
                            .selectExamTaskStatisticsExportList(SessionContextHolder
                                                                        .getRequester()
                                                                        .getTenant(), param);
                    extend(examTaskStatisticsPageExportVOS);
                    EasyExcelFactory.write(response.getOutputStream(), ExamTaskStatisticsPageExportVO.class).sheet("考试统计")
                            .doWrite(examTaskStatisticsPageExportVOS);
                }
            }
        } catch (IOException e) {
            log.error("考试分页列表统计导出失败,失败原因:{}", e);
            throw new ServiceException("导出失败,请联系管理员");
        }
    }

    private void extend(List<ExamTaskStatisticsPageExportVO> examTaskStatisticsPageExportVOS) {
        if (CollectionUtils.isEmpty(examTaskStatisticsPageExportVOS)) {
            return;
        }
        Set<Long> taskIds = examTaskStatisticsPageExportVOS.stream().map(ExamTaskStatisticsPageExportVO::getId)
                .collect(Collectors.toSet());
        Map<Long, List<ExamTaskPaperPO>> examTaskPaperPOMap = this.examTaskPaperQueryService.getPublishedExamPapers(taskIds)
                .stream()
                .collect(Collectors.groupingBy(ExamTaskPaperPO::getExamTaskId));
        examTaskStatisticsPageExportVOS.forEach(examTaskStatisticsPageExportVO -> {
            if (examTaskPaperPOMap.containsKey(examTaskStatisticsPageExportVO.getId())) {
                List<ExamTaskPaperPO> examTaskPaperPOS = examTaskPaperPOMap.get(examTaskStatisticsPageExportVO.getId());
                examTaskStatisticsPageExportVO.setExamPaperScore(examTaskPaperPOS.get(0).getExamPaperScore());
                examTaskStatisticsPageExportVO.setExamPaperName(getExamTaskPaperName(examTaskPaperPOS));
            }
        });
    }

    private String getExamTaskPaperName(List<ExamTaskPaperPO> examTaskPaperPOS) {
        return examTaskPaperPOS.stream().map(ExamTaskPaperPO::getExamPaperName)
                .collect(Collectors.joining(","));
    }

    /**
     * 考试任务下的用户分页列表统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-task-statistics/user-page")
    public Pagination<ExamTaskUserStatisticsPageVO> getExamTaskStatisticsUserPage(@Valid ExamTaskStatisticsUserQuery query) {
        Page<ExamTaskUserPO> examTaskUserPOPage = this.examTaskUserService.page(PagingUtils.convert(query), Wrappers
                .<ExamTaskUserPO>lambdaQuery()
                .eq(ExamTaskUserPO::getExamTaskId, query.getExamTaskId())
                .eq(Objects.nonNull(query.getPassed()), ExamTaskUserPO::getPassed, query.getPassed())
                .eq(Objects.nonNull(query.getExamStatus()), ExamTaskUserPO::getExamStatus, query.getExamStatus()));
        return PagingUtils.convert(examTaskUserPOPage, ExamTaskUserStatisticsPageVO.class);
    }

    /**
     * 考试任务下的用户分页列表导出
     *
     * @param query
     *
     * @return
     */
    @PostMapping("/exam-task-statistics/user-page/export")
    public void getExamTaskStatisticsUserPageExport(@Valid @RequestBody ExamTaskStatisticsUserQuery query) {
        List<ExamTaskUserPO> examTaskUserPOS = this.examTaskUserService.list(Wrappers.<ExamTaskUserPO>lambdaQuery()
                                                                                     .eq(ExamTaskUserPO::getExamTaskId, query.getExamTaskId())
                                                                                     .eq(Objects.nonNull(query.getPassed()), ExamTaskUserPO::getPassed, query.getPassed())
                                                                                     .eq(Objects.nonNull(query.getExamStatus()), ExamTaskUserPO::getExamStatus, query.getExamStatus()));
        String fileName = "预计参加人员";
        if (Objects.nonNull(query.getPassed()) && Objects.equals(Boolean.TRUE, query.getPassed())) {
            fileName = "及格人员";
        } else if (Objects.nonNull(query.getPassed()) && Objects.equals(Boolean.FALSE, query.getPassed())) {
            fileName = "不及格人员";
        } else if (Objects.nonNull(query.getExamStatus()) && UserExamStatusEnum.EXAMINED == query.getExamStatus()) {
            fileName = "实际参加人员";
        } else if (Objects.nonNull(query.getExamStatus()) && UserExamStatusEnum.MISS_EXAM == query.getExamStatus()) {
            fileName = "缺考人员";
        }
        ServletRequestAttributes attr = null;
        HttpServletResponse response = null;
        try {
            attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attr != null) {
                response = attr.getResponse();
                if (response != null) {
                    response.setContentType("application/vnd.ms-excel");
                    response.setCharacterEncoding("utf-8");
                    // 这里URLEncoder.encode可以防止中文乱码
                    String excelName = URLEncoder.encode(fileName, "UTF-8");
                    response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
                    List<ExamTaskUserStatisticsPageExportVO> examTaskStatisticsPageExportVOS = ObjectUtils.copying(examTaskUserPOS, ExamTaskUserStatisticsPageExportVO.class);
                    EasyExcelFactory.write(response.getOutputStream(), ExamTaskUserStatisticsPageExportVO.class).sheet("考试统计")
                            .doWrite(examTaskStatisticsPageExportVOS);
                }
            }
        } catch (IOException e) {
            log.error("试任务下的用户分页列表导出失败,错误原因:{}", e);
            throw new ServiceException("导出失败,请联系管理员");
        }
    }

    /**
     * 考试任务主题占比统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-task-statistics/theme-ratio")
    public List<ExamTaskThemeRatioStatisticsVO> getExamTaskThemeRatioStatistics(@Valid ExamTaskStatisticsQuery query) {
        List<ExamTaskThemeRatioStatisticsVO> examTaskThemeRatioStatisticsVOS = this.examTaskMapper.selectExamTaskThemeRatioStatistics(SessionContextHolder
                                                                                                                                              .getRequester()
                                                                                                                                              .getTenant(), query);
        Integer totalNum = examTaskThemeRatioStatisticsVOS.stream().mapToInt(ExamTaskThemeRatioStatisticsVO::getTotalNum).sum();
        examTaskThemeRatioStatisticsVOS
                .forEach(examTaskThemeRatioStatisticsVO ->
                                 examTaskThemeRatioStatisticsVO.setRatio(new BigDecimal(examTaskThemeRatioStatisticsVO.getTotalNum())
                                                                                 .divide(new BigDecimal(totalNum), 2, BigDecimal.ROUND_HALF_UP)
                                                                                 .multiply(new BigDecimal(100))
                                                                                 .floatValue())
                        );
        return examTaskThemeRatioStatisticsVOS;
    }

    /**
     * 考试任务类型占比统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-task-statistics/type-ratio")
    public List<ExamTaskTypeRatioStatisticsVO> getExamTaskTypeRatioStatistics(@Valid ExamTaskStatisticsQuery query) {
        List<ExamTaskTypeRatioStatisticsVO> examTaskTypeRatioStatisticsVOS = this.examTaskMapper.selectExamTaskTypeRatioStatistics(SessionContextHolder
                                                                                                                                           .getRequester()
                                                                                                                                           .getTenant(), query);
        Integer totalNum = examTaskTypeRatioStatisticsVOS.stream().mapToInt(ExamTaskTypeRatioStatisticsVO::getTotalNum).sum();
        examTaskTypeRatioStatisticsVOS.stream().forEach(examTaskTypeRatioStatisticsVO -> {
            examTaskTypeRatioStatisticsVO.setName(examTaskTypeRatioStatisticsVO.getExamTaskType().getDescription());
            examTaskTypeRatioStatisticsVO.setRatio(new BigDecimal(examTaskTypeRatioStatisticsVO.getTotalNum())
                                                           .divide(new BigDecimal(totalNum), 2, BigDecimal.ROUND_HALF_UP)
                                                           .multiply(new BigDecimal(100)).floatValue());
        });
        return examTaskTypeRatioStatisticsVOS;
    }

    /**
     * 考试出勤情况统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-task-statistics/attendance")
    public List<ExamTaskAttendanceStatisticsVO> getExamTaskAttendanceStatistics(@Valid ExamTaskStatisticsQuery query) {
        return this.examTaskMapper.selectExamTaskAttendanceStatistics(SessionContextHolder.getRequester().getTenant(), query);
    }

    /**
     * 考试任务统计(数量,环比 不及格,缺考)
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-task-statistics")
    public ExamTaskStatisticsVO getExamTaskStatistics(@Valid ExamTaskStatisticsQuery query) {
        Integer examTaskCount = this.examTaskMapper.selectExamTaskCount(SessionContextHolder.getRequester().getTenant(), query);
        Integer missExamUserNumber = this.examTaskMapper.selectExamTaskMissUserCount(SessionContextHolder.getRequester()
                                                                                             .getTenant(), query);
        Integer noPassedUserNumber = this.examTaskMapper.selectExamTaskNoPassedUserCount(SessionContextHolder.getRequester()
                                                                                                 .getTenant(), query);
        Period qoqPeriod = calQOQPeriod(query.getPeriod());
        query.setPeriod(qoqPeriod);
        Integer previousExamTaskCount = this.examTaskMapper.selectExamTaskCount(SessionContextHolder.getRequester().getTenant(), query);
        Float chainRatio = 0f;
        if (0 < previousExamTaskCount) {
            //计算环比增长率((本期-上期)/上期)
            chainRatio = new BigDecimal(examTaskCount - previousExamTaskCount)
                    .divide(new BigDecimal(previousExamTaskCount), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).floatValue();
        } else if (0 >= previousExamTaskCount && examTaskCount > 0) {
            chainRatio = 100f;
        }
        return ExamTaskStatisticsVO.builder()
                .totalNum(examTaskCount)
                .chainRatio(chainRatio)
                .missExamUserNumber(missExamUserNumber)
                .noPassedUserNumber(noPassedUserNumber)
                .build();
    }

    /**
     * 考试任务统计(数量,环比 不及格,缺考)
     *
     * @param query
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "safetyEducation:examTaskUser")
    @GetMapping("/exam-task-statistics/permission")
    public ExamTaskStatisticsVO getExamTaskPermissionStatistics(@Valid ExamTaskStatisticsQuery query) {
        return this.getExamTaskStatistics(query);
    }

    /**
     * 考试任务数趋势统计
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/exam-task-statistics/trend")
    public List<ExamTaskTrendStatisticsVO> getExamTaskTrendStatistics(@Valid ExamTaskStatisticsQuery query) {
        List<ExamTaskTrendStatisticsVO> examTaskTrendStatisticsVOS = this.examTaskMapper.selectExamTaskMonthTrendStatistics(SessionContextHolder
                                                                                                                                    .getRequester()
                                                                                                                                    .getTenant(), query);
        if (!CollectionUtils.isEmpty(examTaskTrendStatisticsVOS)) {
            YearMonth startMonth = YearMonth.parse(DateUtils.format(query.getPeriod().getBegin(), "YYYY-MM"));
            YearMonth endMonth = YearMonth.parse(DateUtils.format(query.getPeriod().getEnd(), "YYYY-MM"));
            return IntStream.rangeClosed(0, endMonth.getMonthValue() - startMonth.getMonthValue() +
                                            (12 * (endMonth.getYear() - startMonth.getYear()))).mapToObj(startMonth::plusMonths).
                    map(m -> {
                        ExamTaskTrendStatisticsVO stat = examTaskTrendStatisticsVOS.stream().filter(t -> t.getName().equals(m.toString()))
                                .findAny().orElse(null);
                        if (stat == null) {
                            return new ExamTaskTrendStatisticsVO(m.toString(), 0);
                        } else {
                            return stat;
                        }
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Period calQOQPeriod(Period period) {
        var between = DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.DAYS);
        var start = DateUtils.offsetDay(period.getBegin(), -(between + 1));
        var end = DateUtils.offsetDay(period.getEnd(), -(between + 1));
        return Period.of(start, end);
    }
}
