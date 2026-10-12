package com.guhecloud.cube.srv.gen.maintain.server.electron.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.ElectronicPatrolTaskDataModel;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.AbnormalTaskRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.ElectronQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.AbnormalTaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.LocationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.LocationTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolLocationDispatchingParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolRecordExportParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolRecordPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskOperateParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskResultParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.HomePagePatrolTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationRouteStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskAbnormalListInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskAbnormalNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.AbnormalTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolOverviewVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolRecordPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskDataVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author wql
 * @describe 巡更任务
 * @date 2023/5/29 11:26
 */
public interface PatrolTaskService extends IService<PatrolTask> {
    void addTask(PatrolTaskParam param);

    void submitResult(PatrolTaskResultParam param);

    Pagination<PatrolTaskPageVO> queryTaskPage(PatrolTaskPageParam param);

    PatrolTaskVO detail(Long id, Boolean executePeriodAll);

    void acceptTask(Long id);

    void addSynergyUser(Long taskId, String userId);

    void turnOverTask(Long id, Long userId);

    PatrolOverviewVO overview(PatrolTaskPageParam param);

    List<AreaDTO> getTaskLocationAreaList(Long taskId);

    Pagination<PatrolRecordPageVO> queryRecordPage(PatrolRecordPageParam param);

    void queryRecordExport(PatrolRecordExportParam param, HttpServletResponse response);

    /**
     * 任务即将开始发送短信
     */
    void taskStartMessage(Long conditionId, LocalDateTime time, Integer value);

    void changeToWaitAccept(LocalDateTime time);

    /**
     * 任务即将结束发送短信
     *
     * @param conditionId 短信模板id
     * @param time        时间
     * @param value       多少分钟发送
     */
    void taskEndMessage(Long conditionId, LocalDateTime time, Integer value);

    /**
     * @Author wql
     * @Description 根据nfc芯片获取最新任务的巡更点信息
     * @Date 18:31 2023/6/2
     **/
    PatrolTaskDataVO getLocationRecordByNFC(String nfcCode);

    /**
     * @Author wql
     * @Description 检查巡更任务是否过期
     * @Date 10:01 2023/6/6
     **/
    boolean checkTaskIsExpire();

    /**
     * 检查巡更任务是否过期
     *
     * @param taskId
     *
     * @return
     */
    boolean checkTaskIsExpire(Long taskId);

    /**
     * @Author lijie
     * @Description 判断任务是否可以巡检
     * @Date 2023-06-14
     **/
    Boolean taskOverdue(Long taskId);

    /**
     * @Author wql
     * @Description 查询前先更新下任务状态
     * @Date 10:00 2023/6/21
     **/
    void updateTaskStatus(LocalDateTime date);

    /**
     * @Author wql
     * @Description 安全管理电子巡更数据采集
     * @Date 9:03 2023/9/11
     **/
    List<ElectronicPatrolTaskDataModel> getSafetyManagementDataModel(Period period);

    /**
     * 查询所有有巡更任务的租户
     *
     * @return
     */
    Set<Integer> getAllTenant();

    /**
     * @Author wql
     * @Description 获取巡更点最新的巡更任务
     * @Date 14:11 2023/10/25
     **/
    PatrolTaskDataVO getLocationRecordByLocationId(Long locationId);

    /**
     * @Author wql
     * @Description 指挥调度异常任务列表
     * @Date 15:09 2023/10/26
     **/
    Pagination<AbnormalTaskVO> anomalyTaskPage(AbnormalTaskPageParam param);

    /**
     * @Author wql
     * @Description 异常任务确认知晓
     * @Date 16:51 2023/10/26
     **/
    void taskAffirm(Long id);

    List<PatrolTaskDataVO> getAllLocation(LocationTaskParam param);

    List<LocationRouteStatisticsVO> locationRouteStatistics(StatisticsQueryParam param);

    TaskAbnormalNumStatisticsVO taskAbnormalNumStatistics(BaseDateStatisticsParam param);

    Pagination<TaskAbnormalListInfoVO> taskAbnormalListStatistics(AbnormalTaskRecordParam param);

    HomePagePatrolTaskVO getElectronTaskHomePageStatistics(BaseDateStatisticsParam pram);

    /**
     * 删除巡更任务(批量)
     *
     * @param param taskIdList 任务id集合
     *
     * @return 是否成功
     */
    Boolean removeElectronTaskBatch(PatrolTaskOperateParam param);

    TotalNumberRateDTO getElectronErrRate(ElectronQueryParam param);

    TotalNumberRateDTO getElectronCompleteRate(ElectronQueryParam param);

    Long getTaskTotal(Period period, Set<Long> taskIds);

    List<LocationStatisticsVO> getLocationsStatistics(LocationQueryParam param);

    /**
     * 巡更完成率指标
     *
     * @param period
     *
     * @return
     */
    HandlerRateMetricDTO getElectronCompleteRateMetric(Period period);

    /**
     * 巡更效率指标
     *
     * @param period
     *
     * @return
     */
    HandlerEfficiencyMetricDTO getElectronCompleteEfficiencyMetric(Period period);

    /**
     * 处理数和处理总时长
     *
     * @param period
     *
     * @return
     */
    TaskHandlerTimeVO getElectronCompleteTotalTime(Period period);

    Boolean taskLocationDistribution(PatrolLocationDispatchingParam param);
}
