package com.guhecloud.cube.srv.gen.loose.works.controller;

import com.guhecloud.cube.srv.gen.loose.works.dto.LooseWorksDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleIdTypeDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleMetricDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.CheckAcceptParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.FeedbackParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectScheduleQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectScheduleQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.param.ProjectScheduleParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.FeedbackDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForAnalysisVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForAnalysisVO2;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForReportVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectNumStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectTrendStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.TrackRecordVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.TrendStatisticsQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.MetricType;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectScheduleService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 小散工程进度
 *
 * @author wql
 * @describe 工程进度
 * @date 2023/9/14 18:49
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectScheduleController {
    private final ProjectScheduleService projectScheduleService;

    /**
     * 根据ID获取工程监管信息
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/project/schedule/get/by/id")
    public ProjectScheduleVO projectScheduleGetById(@RequestParam("id") Long id) {
        return projectScheduleService.projectScheduleGetById(id);
    }

    /**
     * 批量获取获取工程监管信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/project/schedule/get/list")
    public List<ProjectScheduleVO> projectScheduleBatchGet(@SpringQueryMap ProjectScheduleQueryParam param) {
        return projectScheduleService.projectScheduleBatchGet(param);
    }

    /**
     * 批量获取获取工程监管信息
     *
     * @param param 条件
     *
     * @return 列表
     */
    @PostMapping("/project/schedule/post/list")
    public List<ProjectScheduleVO> projectScheduleBatchPost(@RequestBody ProjectScheduleQueryParam param) {
        return projectScheduleService.projectScheduleBatchGet(param);
    }

    /**
     * 获取小散工程总数
     */
    @GetMapping("/project/schedule/count")
    public Long projectScheduleCount() {
        return projectScheduleService.count();
    }

    /**
     * 工程进度跟踪列表
     *
     * @Author wql
     * @Description 工程进度跟踪列表
     * @Date 19:43 2023/9/14
     **/
    @GetMapping("/project/schedule/list")
    @DataPermissionSwitch(dataModel = "project:projectSchedule")
    public Pagination<ProjectScheduleVO> getProjectScheduleList(@Validated ProjectScheduleQuery query) {
        return projectScheduleService.getProjectScheduleList(query);
    }

    /**
     * 工程进度跟踪列表
     *
     * @Author wql
     * @Description 工程进度跟踪列表
     * @Date 19:43 2023/9/14
     **/
    @PostMapping("/project/schedule/page")
    @DataPermissionSwitch(dataModel = "project:projectSchedule")
    public Pagination<ProjectScheduleVO> getProjectSchedulePage(@RequestBody @Valid ProjectScheduleQuery query) {
        return projectScheduleService.getProjectScheduleList(query);
    }

    /**
     * 工程进度详情
     *
     * @Author wql
     * @Description 工程进度详情
     * @Date 20:04 2023/9/14
     **/
    @GetMapping("/project/schedule/detail/{id}")
    public ProjectScheduleDetailVO getProjectScheduleDetail(@NotNull @PathVariable("id") Long id) {
        return projectScheduleService.getProjectScheduleDetail(id);
    }

    /**
     * 跟踪记录列表
     *
     * @Author wql
     * @Description 跟踪记录列表
     * @Date 20:12 2023/9/14
     **/
    @GetMapping("/project/schedule/{id}/trackDownList")
    public List<TrackRecordVO> getTrackDownList(@NotNull @PathVariable("id") Long id) {
        return projectScheduleService.getTrackDownList(id);
    }

    /**
     * 跟踪记录详情
     *
     * @Author wql
     * @Description 跟踪记录详情
     * @Date 20:17 2023/9/14
     **/
    @GetMapping("/project/schedule/trackDownDetail/{id}")
    public FeedbackDetailVO getTrackDownDetail(@NotNull @PathVariable("id") Long id) {
        return projectScheduleService.getTrackDownDetail(id);
    }

    /**
     * 工程进度反馈
     *
     * @Author wql
     * @Description 工程进度反馈
     * @Date 14:14 2023/9/15
     **/
    @PostMapping("/project/schedule/feedback")
    public void projectFeedback(@Valid @RequestBody FeedbackParam feedbackParam) {
        projectScheduleService.projectFeedback(feedbackParam);
    }

    /**
     * 工程验收
     *
     * @Author wql
     * @Description 工程验收
     * @Date 14:24 2023/9/15
     **/
    @PostMapping("/project/schedule/checkAccept")
    public void projectCheckAccept(@Valid @RequestBody CheckAcceptParam checkAcceptParam) {
        projectScheduleService.projectCheckAccept(checkAcceptParam);
    }

    /**
     * 小散工程安全管理数据采集
     *
     * @Author wql
     * @Description 小散工程安全管理数据采集
     * @Date 14:45 2023/9/19
     **/
    @PreAuthorize("hasScope('loose_work')")
    @GetMapping("/project/schedule/dataCollect")
    public LooseWorksDTO looseWorksDataCollect(Period period, @RequestParam("reformPeriod") Period reformPeriod) {
        return projectScheduleService.looseWorksDataCollect(period, reformPeriod);
    }

    /**
     * 小散工程数量统计
     *
     * @param period 统计时间范围
     *
     * @return 统计结果
     */
    @GetMapping("/project/num/statistics")
    public ProjectNumStatisticsVO projectNumStatistics(Period period) {
        Assert.isNotFound(period, "统计时间范围不允许为空!");
        return projectScheduleService.projectNumStatistics(period);
    }

    /**
     * 小散工程趋势统计
     *
     * @return 统计结果
     */
    @GetMapping("/project/trend/statistics")
    public List<ProjectTrendStatisticsVO> projectTrendStatistics() {
        return projectScheduleService.projectTrendStatistics();
    }

    /**
     * 小散工程异常率统计
     *
     * @param period 时间范围
     *
     * @return 异常率
     */
    @GetMapping("/project/abnormal/ratio")
    public Float projectAbnormalRatio(Period period) {
        Assert.isNotFound(period, "统计时间范围不允许为空!");
        return projectScheduleService.projectAbnormalRatio(period);
    }

    /**
     * 小散工程统计数据获取
     *
     * @param period 时间范围
     *
     * @return 异常率
     */
    @GetMapping("/project/ratio/safety-report")
    public ProjectDataForReportVO projectDataForReport(@RequestParam("period") Period period) {
        Assert.isNotFound(period, "统计时间范围不允许为空!");
        return projectScheduleService.projectDataForReport(period);
    }

    /**
     * 小散工程异常率统计
     *
     * @param period 时间范围
     *
     * @return 异常率
     */
    @GetMapping("/abnormal/ratio/safety/analysis")
    public ProjectDataForAnalysisVO projectDataForAbnormalSafetyAnalysis(@RequestParam("period") Period period,
                                                                         @RequestParam("type") String type
                                                                        ) {
        Assert.isNotFound(period, "统计时间范围不允许为空!");
        return projectScheduleService.projectDataForAbnormalSafetyAnalysis(period, type);
    }

    /**
     * 安全分析-小散工程数量和环比统计
     *
     * @param period 时间范围
     *
     * @return 异常率
     */
    @GetMapping("/project/ratio/safety/analysis")
    public ProjectDataForAnalysisVO2 projectDataForSafetyAnalysis(@RequestParam("period") Period period,
                                                                  @RequestParam("type") String type
                                                                 ) {
        Assert.isNotFound(period, "统计时间范围不允许为空!");
        return projectScheduleService.projectDataForSafetyAnalysis(period, type);
    }

    /**
     * 安全分析-小散工程趋势
     *
     * @return 异常率
     */
    @GetMapping("/project/trend/safety/analysis")
    public List<ProjectTrendStatisticsVO> projectTrendForSafetyAnalysis(TrendStatisticsQueryParam param) {

        return projectScheduleService.projectTrendForSafetyAnalysis(param);
    }

    /**
     * 小散工程-工程数统计
     *
     * @param param 参数
     *
     * @return 数量
     */
    @GetMapping("/project/home/statistics")
    @DataPermissionSwitch(dataModel = "project:projectSchedule")
    public ProjectStatisticsVO homepageStatistics(TrendStatisticsQueryParam param) {

        return projectScheduleService.homepageStatistics(param);
    }

    /**
     * 新增工程监管
     */
    @PostMapping("/project/schedule/save")
    public Long saveProjectSchedule(@Valid @RequestBody ProjectScheduleParam param) {
        return projectScheduleService.saveProjectSchedule(param);
    }

    /**
     * 编辑工程监管基本信息
     */
    @PostMapping("/project/schedule/edit")
    public Boolean editProjectSchedule(@Valid @RequestBody ProjectScheduleParam param) {
        return projectScheduleService.editProjectSchedule(param);
    }

    /**
     * 工程监管数据删除
     *
     * @param idList id集合
     *
     * @return
     */
    @PostMapping("/project/schedule/delete")
    public Boolean deleteProjectSchedule(@RequestBody Set<Long> idList) {
        Assert.isParameterError(CollectionUtils.isEmpty(idList), "请选择要删除的数据");
        return projectScheduleService.deleteProjectSchedule(idList);
    }

    /**
     * 获取小散工程完成率，整改率，整改效率
     */
    @GetMapping("/project/schedule/metric")
    public ProjectScheduleMetricDTO projectScheduleMetric(@RequestParam("period") Period period,
                                                          @RequestParam("metricType") MetricType metricType
                                                         ) {
        Assert.isNotFound(period, "统计时间范围不允许为空!");
        return projectScheduleService.projectScheduleMetric(period, metricType);
    }

    /**
     * 获取工程监管ID列表
     *
     * @param query 查询参数
     *
     * @return 工程监管ID列表
     */
    @PostMapping("/project/schedule/idTypes")
    public List<ProjectScheduleIdTypeDTO> getProjectScheduleIdTypes(@RequestBody @Valid ProjectScheduleQuery query) {
        return projectScheduleService.getProjectScheduleIds(query);
    }

}
