package com.guhecloud.cube.srv.gen.loose.works.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.loose.works.dto.LooseWorksDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleIdTypeDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleMetricDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.CheckAcceptParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.FeedbackParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectScheduleQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectScheduleQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectStatisticsQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.param.ProjectScheduleParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.FeedbackDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForAnalysisVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForAnalysisVO2;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDataForReportVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectNumStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectScheduleVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectTrendStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.TrackRecordVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.TrendStatisticsQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.MetricType;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectSchedulePO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;
import java.util.Set;

/**
 * @author wql
 * @describe 工程进度
 * @date 2023/9/14 18:43
 */
public interface ProjectScheduleService extends IService<ProjectSchedulePO> {
    /**
     * 根据id获取工程进度
     *
     * @param id
     *
     * @return
     */
    ProjectScheduleVO projectScheduleGetById(Long id);

    /**
     * 根据条件批量获取工程进度
     *
     * @param param
     *
     * @return
     */
    List<ProjectScheduleVO> projectScheduleBatchGet(ProjectScheduleQueryParam param);

    Pagination<ProjectScheduleVO> getProjectScheduleList(ProjectScheduleQuery query);

    ProjectScheduleDetailVO getProjectScheduleDetail(Long id);

    List<TrackRecordVO> getTrackDownList(Long id);

    FeedbackDetailVO getTrackDownDetail(Long id);

    /**
     * @Author wql
     * @Description 统计工程各进度项
     * @Date 10:00 2023/9/15
     **/
    ProjectScheduleStatisticsVO scheduleStatistics(ProjectStatisticsQuery query);

    /**
     * @Author wql
     * @Description 获取反馈列表
     * @Date 10:49 2023/9/15
     **/
    List<ProjectDetailVO.FeedbackDetail> getFeedbackDetailList(Long id, String feedbackType);

    /**
     * @Author wql
     * @Description 工程进度反馈
     * @Date 14:18 2023/9/15
     **/
    void projectFeedback(FeedbackParam feedbackParam);

    /**
     * @Author wql
     * @Description 工程验收
     * @Date 14:27 2023/9/15
     **/
    void projectCheckAccept(CheckAcceptParam checkAcceptParam);

    /**
     * @Author wql
     * @Description 新增工程进度跟踪信息
     * @Date 17:29 2023/9/15
     **/
    void saveScheduleInfo(ProjectSchedulePO build);

    /**
     * @Author wql
     * @Description 安全管理小散工程数据采集
     * @Date 14:46 2023/9/19
     **/
    LooseWorksDTO looseWorksDataCollect(Period period, Period reformPeriod);

    /**
     * @Author wql
     * @Description 根据请示申请单id查询工程进度跟踪记录
     * @Date 15:49 2023/9/22
     **/
    ProjectSchedulePO getProjectScheduleByApplyId(Long id);

    ProjectNumStatisticsVO projectNumStatistics(Period period);

    List<ProjectTrendStatisticsVO> projectTrendStatistics();

    Float projectAbnormalRatio(Period period);

    ProjectDataForReportVO projectDataForReport(Period period);

    ProjectDataForAnalysisVO projectDataForAbnormalSafetyAnalysis(Period period, String type);

    ProjectDataForAnalysisVO2 projectDataForSafetyAnalysis(Period period, String type);

    List<ProjectTrendStatisticsVO> projectTrendForSafetyAnalysis(TrendStatisticsQueryParam param);

    /**
     * 小散工程-首页统计
     *
     * @param param
     *
     * @return
     */
    ProjectStatisticsVO homepageStatistics(TrendStatisticsQueryParam param);

    Long saveProjectSchedule(ProjectScheduleParam param);

    Boolean editProjectSchedule(ProjectScheduleParam param);

    /**
     * 根据id批量删除工程进度
     *
     * @param idList
     *
     * @return
     */
    Boolean deleteProjectSchedule(Set<Long> idList);

    /**
     * 获取工程指标
     *
     * @param period
     * @param metricType
     *
     * @return
     */
    ProjectScheduleMetricDTO projectScheduleMetric(Period period, MetricType metricType);

    /**
     * 获取工程监管ID列表
     *
     * @param query 查询参数
     *
     * @return 工程监管ID列表
     */
    List<ProjectScheduleIdTypeDTO> getProjectScheduleIds(ProjectScheduleQuery query);
}
