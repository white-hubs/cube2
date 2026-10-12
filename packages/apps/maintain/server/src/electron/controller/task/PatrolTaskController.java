package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.task;

import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.ElectronicPatrolTaskDataModel;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.AbnormalTaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.LocationTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolLocationDispatchingParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolRecordExportParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolRecordPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskOperateParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskResultParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.TaskLocationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.CalcTaskPeriodVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.HomePagePatrolTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.AbnormalTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.ElectronTaskSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolOverviewVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolRecordPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskDataVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskPageVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 巡更任务
 *
 * @author : lijie
 * @date : 2023-5-25
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolTaskController {

    private final PatrolTaskService patrolTaskService;
    private final PatrolTaskLocationService patrolTaskLocationService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    @GetMapping("/electron/task/{id}")
    public PatrolTaskVO detail(@PathVariable("id") Long id, @RequestParam(required = false) Boolean executePeriodAll) {
        return patrolTaskService.detail(id, executePeriodAll);
    }

    /**
     * 巡更任务分页查询
     *
     * @param param 筛选条件
     *
     * @return 分页数据
     */
    @GetMapping("/electron/task/queryTaskPage")
    @DataPermissionSwitch(dataModel = "patrol:patrolTask")
    public Pagination<PatrolTaskPageVO> queryTaskPage(PatrolTaskPageParam param) {
        return patrolTaskService.queryTaskPage(param);
    }

    /**
     * 巡更记录分页查询
     *
     * @param param 巡更记录分页参数
     *
     * @return 巡更记录
     */
    @GetMapping("/electron/task/queryRecordPage")
    @DataPermissionSwitch(dataModel = "patrol:patrolTask")
    public Pagination<PatrolRecordPageVO> queryRecordPage(PatrolRecordPageParam param) {
        return patrolTaskService.queryRecordPage(param);
    }

    /**
     * 巡更记录导出
     *
     * @param param 巡更记录导出参数
     *
     * @return 巡更记录
     */
    @GetMapping("/electron/task/queryRecordExport")
    @DataPermissionSwitch(dataModel = "patrol:patrolTask")
    public void queryRecordExport(PatrolRecordExportParam param, HttpServletResponse response) {
        patrolTaskService.queryRecordExport(param, response);
    }

    /**
     * 新增任务
     *
     * @param param 任务信息
     */
    @PostMapping("/electron/task/insert")
    public void insert(@RequestBody PatrolTaskParam param) {
        patrolTaskService.addTask(param);
    }

    /**
     * 接收任务
     *
     * @param id 任务id
     */
    @GetMapping("/electron/accept/task/{id}")
    public void acceptTask(@PathVariable("id") Long id) {
        patrolTaskService.acceptTask(id);
    }

    /**
     * 添加协同人
     *
     * @param taskId 巡更任务主键
     * @param userId 协作人主键
     */
    @PutMapping("/electron/{id}/addCollaborator")
    public void addSynergyUser(@PathVariable("id") Long taskId, String userId) {
        patrolTaskService.addSynergyUser(taskId, userId);
    }

    /**
     * 移交任务
     *
     * @param id     任务id
     * @param userId 用户id
     */
    @GetMapping("/electron/turnOver/task/{id}/{userId}")
    public void turnOverTask(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        patrolTaskService.turnOverTask(id, userId);
    }

    /**
     * 提交巡更结果
     *
     * @param param 结果信息
     */
    @PostMapping("/electron/task/submitResult")
    public void submitResult(@RequestBody PatrolTaskResultParam param) {
        patrolTaskService.submitResult(param);
    }

    /**
     * 巡更任务概览
     *
     * @param param 查询参数
     *
     * @return 巡更任务概览
     */
    @GetMapping("/electron/task/overview")
    @DataPermissionSwitch(dataModel = "patrol:patrolTask")
    public PatrolOverviewVO overview(PatrolTaskPageParam param) {
        return patrolTaskService.overview(param);
    }

    /**
     * 根据nfc芯片获取最新任务的巡更点信息
     *
     * @param nfcCode nfc芯片编码
     *
     * @return 巡更点信息
     */
    @GetMapping("/electron/task/getLocationRecordBy/{nfcCode}")
    public PatrolTaskDataVO getLocationRecordByNFC(@PathVariable("nfcCode") String nfcCode) {
        return patrolTaskService.getLocationRecordByNFC(nfcCode);
    }

    /**
     * 获取巡更点最新的巡更任务
     *
     * @Author wql
     * @Description 获取巡更点最新的巡更任务
     * @Date 14:08 2023/10/25
     **/
    @GetMapping("/electron/task/getLocationRecord/{locationId}")
    public PatrolTaskDataVO getLocationRecordByLocationId(@PathVariable("locationId") Long locationId) {
        return patrolTaskService.getLocationRecordByLocationId(locationId);
    }

    /**
     * @Author lijie
     * @Description 判断任务是否可以巡检
     * @Date 2023-06-14
     **/
    @GetMapping("/electron/task/taskOverdue/{taskId}")
    public Boolean taskOverdue(@PathVariable("taskId") Long taskId) {
        return patrolTaskService.taskOverdue(taskId);
    }

    /**
     * @Author wql
     * @Description 安全管理电子巡更数据采集
     * @Date 9:02 2023/9/11
     **/
    @PreAuthorize("hasScope('electronlc_patrol')")
    @GetMapping("/electron/task/getInspectDataModel")
    public List<ElectronicPatrolTaskDataModel> getSafetyManagementDataModel(Period period) {
        return patrolTaskService.getSafetyManagementDataModel(period);
    }

    /**
     * 指挥调度异常任务列表
     *
     * @Author wql
     * @Description 指挥调度异常任务列表
     * @Date 15:04 2023/10/26
     **/
    @GetMapping("/electron/task/anomalyTaskPage")
    public Pagination<AbnormalTaskVO> anomalyTaskPage(AbnormalTaskPageParam param) {
        return patrolTaskService.anomalyTaskPage(param);
    }

    /**
     * 异常任务确认知晓
     *
     * @Author wql
     * @Description 异常任务确认知晓
     * @Date 16:50 2023/10/26
     **/
    @GetMapping("/electron/task/affirm/{id}")
    public void taskAffirm(@PathVariable("id") Long id) {
        patrolTaskService.taskAffirm(id);
    }

    /**
     * 查询任务当前所有任务下的所有巡更点
     */
    @PostMapping("/electron/task/getAllLocation")
    public List<PatrolTaskDataVO> getAllLocation(@RequestBody LocationTaskParam param) {
        return patrolTaskService.getAllLocation(param);
    }

    /**
     * 获取任务的巡更时间段详情
     *
     * @param param 查询参数
     *
     * @return 集合
     */
    @GetMapping("/electron/task/location")
    public List<PatrolTaskDataVO> getTaskLocation(TaskLocationQueryParam param) {
        return patrolTaskLocationService.getTaskLocation(param.getTaskId(), param.getExecutePeriod(), param.getAreaSequence(), param.getResultNull());
    }

    /**
     * 计算任务时间段
     *
     * @param startStr     开始时间
     * @param endStr       结束时间
     * @param intervalHour 间隔时间(小时)
     *
     * @return 时间段
     */
    @GetMapping("/electron/task/calcPeriod")
    public CalcTaskPeriodVO calcPeriod(@RequestParam("startStr") String startStr, @RequestParam("endStr") String endStr,
                                       @RequestParam("intervalHour") Integer intervalHour
                                      ) {
        if (StringUtils.isBlank(startStr) || StringUtils.isBlank(endStr) || intervalHour == null) {
            throw new ServiceException("请填写完整参数！");
        }
        final String formatString = "HH:mm";
        LocalDateTime startTime;
        LocalDateTime endTime;
        try {
            startTime = DateUtils.parse(startStr, formatString);
            endTime = DateUtils.parse(endStr, formatString);
        } catch (Exception e) {
            throw new ServiceException("日期格式错误！");
        }
        if (endTime.isBefore(startTime)) {
            endTime = DateUtils.offsetDay(endTime, 1);
        }
        if (DateUtils.compare(startTime, endTime) == 0) {
            throw new ServiceException("开始-结束时间不能相同");
        }
        CalcTaskPeriodVO result = new CalcTaskPeriodVO();
        String tps = null;
        List<String> periodList = new ArrayList<>();

        var printTime = startTime;
        int i = 0;
        while (printTime.isBefore(endTime)) {
            i++;
            var end = DateUtils.offset(printTime, ChronoUnit.HOURS, intervalHour);
            if (end.isAfter(endTime)) {
                end = endTime;
                tps = "时间不足，请根据实际情况调整";
            }
            String period = DateUtils.format(printTime, formatString) + "-" + DateUtils.format(end, formatString);
            periodList.add(period);
            printTime = end;
            if (i > 10000) {
                throw new ServiceException("网络错误！");
            }
        }
        result.setTip(tps);
        result.setPeriodList(periodList);
        return result;
    }

    @GetMapping("/electron/task/safety-report")
    public ElectronTaskSafetyReportVO getElectronTaskForSafetyReport(@RequestParam("period") Period period) {
        return patrolTaskLocationService.getElectronTaskForSafetyReport(period);
    }

    /**
     * 电子巡更-任务统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/electron/task/home-page/stat")
    @DataPermissionSwitch(dataModel = "patrol:patrolTask")
    public HomePagePatrolTaskVO getElectronTaskHomePageStatistics(BaseDateStatisticsParam param) {
        return patrolTaskService.getElectronTaskHomePageStatistics(param);
    }

    /**
     * 删除巡更任务(批量)
     *
     * @param param taskIdList 任务id集合
     *
     * @return 是否成功
     */
    @PutMapping("/electron/task/remove-batch")
    public Boolean removeElectronTaskBatch(@RequestBody PatrolTaskOperateParam param) {
        return patrolTaskService.removeElectronTaskBatch(param);
    }

    /**
     * 任务分发（任务的巡更点分配）
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/electron/task/location-distribution")
    public Boolean taskLocationDistribution(@RequestBody PatrolLocationDispatchingParam param) {
        return patrolTaskService.taskLocationDistribution(param);
    }

    /**
     * 获取任务下巡更点的区域
     *
     * @param taskId 任务id
     *
     * @return 区域列表
     */
    @GetMapping("/electron/task/location-area")
    public List<AreaDTO> getTaskLocationAreaList(Long taskId) {
        return patrolTaskService.getTaskLocationAreaList(taskId);
    }
}
