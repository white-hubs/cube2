package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.BaseInspectStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionAnalysisParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionItemDetailsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.AnalysisOfAuditResultsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepTaskExecutionAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.HomepageStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionFrequencyAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionIObjDetailsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionItemDetailsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjAbnormalAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjTaskAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionPlanAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionRecordAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskUserAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ObjInspectionInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.SafetyReportResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TargetCategoryVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskExecutionAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskTimeAnalysisVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * @author wql
 * @describe 巡检分析
 * @date 2023/4/25 19:10
 */
public interface InspectionAnalysisService {
    /**
     * @Author wql
     * @Description 巡检计划分析
     * @Date 19:08 2023/4/25
     **/
    InspectionPlanAnalysisVO inspectionPlanAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 巡检任务分析
     * @Date 19:35 2023/4/25
     **/
    InspectionTaskAnalysisVO inspectionTaskAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 巡检次数分析
     * @Date 19:45 2023/4/25
     **/
    InspectionFrequencyAnalysisVO inspectionFrequencyAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 任务执行分析
     * @Date 19:53 2023/4/25
     **/
    TaskExecutionAnalysisVO taskExecutionAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 部门巡检任务分析
     * @Date 19:59 2023/4/25
     **/
    DepTaskExecutionAnalysisVO depTaskExecutionAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 任务耗时分析
     * @Date 20:10 2023/4/25
     **/
    TaskTimeAnalysisVO taskTimeAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 稽查结果分析
     * @Date 20:18 2023/4/25
     **/
    AnalysisOfAuditResultsVO analysisOfAuditResults(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 巡检对象分析
     * @Date 20:25 2023/4/25
     **/
    InspectionObjAnalysisVO inspectionObjAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 巡检对象异常项分析
     * @Date 20:31 2023/4/25
     **/
    InspectionObjAbnormalAnalysisVO inspectionObjAbnormalAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 对象任务统计分析
     * @Date 20:35 2023/4/25
     **/
    InspectionObjTaskAnalysisVO inspectionObjTaskAnalysis(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 对象巡检次数top10
     * @Date 20:26 2023/4/18
     **/
    List<ObjInspectionInfoVO> objInspectionNumTop(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 对象异常项数排名top10
     * @Date 8:48 2023/4/19
     **/
    List<ObjInspectionInfoVO> objAbnormalNumTop(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 对象巡检任务数top10
     * @Date 8:55 2023/4/19
     **/
    List<ObjInspectionInfoVO> objInspectionTaskTop(InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 对象巡检任务数last10
     * @Date 8:55 2023/4/19
     **/
    List<ObjInspectionInfoVO> objInspectionTaskLast(InspectionAnalysisParam analysisParam);

    /**
     * @description: 巡检对象分析
     * @author: lijie
     * @date: 2023/11/14 10:46
     * @param:
     * @return:
     **/
    List<TargetCategoryVO> getTargetCategory(InspectionAnalysisParam analysisParam);

    SafetyReportResultVO inspectAnalysisSafetyReport(Period period);

    Pagination<InspectionItemDetailsVO> getInspectionStandardTtem(InspectionItemDetailsParam query);

    HomepageStatisticsVO inspectAnalysisHomepageStatistics(BaseDateStatisticsParam param);

    InspectionIObjDetailsVO inspectAnalysisObjectDetailsStatistics(InspectionItemDetailsParam param);

    /**
     * 巡检任务数和异常常数统计
     *
     * @param param
     *
     * @return
     */
    InspectionTaskAnalysisVO inspectAnalysisTaskStatistics(BaseDateStatisticsParam param);

    /**
     * 巡检任务列表
     *
     * @param param
     *
     * @return
     */
    Pagination<InspectionTaskUserAnalysisVO> inspectAnalysisTaskExecuteUserStatistics(BaseInspectStatisticsParam param);

    /**
     * 巡检记录列表
     *
     * @param query
     *
     * @return
     */
    Pagination<InspectionRecordAnalysisVO> inspectAnalysisTaskRecordStatistics(BaseInspectStatisticsParam query);
}
