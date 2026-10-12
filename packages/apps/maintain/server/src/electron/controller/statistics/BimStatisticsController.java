package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.statistics;

import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration.ExceptionTypeEnum;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.AbnormalTaskRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolRoutePageParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.StatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolDeviceService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolRouteService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.statistics.PatrolStatisticsService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.DeviceStatusStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolRouteVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationDistributeStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationRouteStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationTopStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskAbnormalListInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskAbnormalNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskCompletionRateStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskLocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BimStatisticsController {

    private final PatrolRouteService patrolRouteService;

    private final PatrolTaskLocationService patrolTaskLocationService;

    private final PatrolDeviceService patrolDeviceService;

    private final PatrolStatisticsService patrolStatisticsService;

    private final PatrolTaskService patrolTaskService;

    private final PatrolLocationService patrolLocationService;

    /**
     * 巡更路线
     */
    @GetMapping("/patrol/route/list")
    public List<PatrolRouteVO> queryRouteList(PatrolRoutePageParam param) {
        return patrolRouteService.queryRouteList(param);
    }

    /**
     * 巡更点异常top5统计
     */
    @GetMapping("/electron/statistics/location/list")
    public List<LocationStatisticsVO> locationStatisticsList(StatisticParam param) {
        return patrolTaskLocationService.locationStatisticsTop5(param);
    }

    /**
     * 巡更点漏巡统计
     * top5
     */
    @GetMapping("/electron/location/leak/stat")
    public List<LocationStatisticsVO> getLocationLeakStat(StatisticParam param) {
        param.setExceptionType(ExceptionTypeEnum.LEAK_DETECTION.name());
        param.setResultSize(5);
        return patrolTaskLocationService.getPatrolExpStat(param);
    }

    /**
     * 设备状态统计
     */
    @GetMapping("/electron/device/status/stat")
    public List<DeviceStatusStatVO> statPatrolStatus() {
        return patrolDeviceService.getDeviceStatusStat();
    }

    /**
     * 巡更任务完成率统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/patrol/task/completion-rate")
    public TaskCompletionRateStatisticsVO getPatrolTaskCompletionRate(@Valid BaseDateStatisticsParam param) {
        return this.patrolStatisticsService.getPatrolTaskCompletionRate(param);
    }

    /**
     * 任务异常记录数统计(物联2.1)
     *
     * @return 统计结果
     */
    @GetMapping("/patrol/task/abnormal/num")
    public TaskAbnormalNumStatisticsVO taskAbnormalNumStatistics(@Valid BaseDateStatisticsParam param) {
        return patrolTaskService.taskAbnormalNumStatistics(param);
    }

    /**
     * 任务异常记录
     *
     * @param param 任务异常记录查询条件
     *
     * @return 任务异常记录列表
     */
    @GetMapping("/patrol/task/abnormal/list")
    public Pagination<TaskAbnormalListInfoVO> taskAbnormalListStatistics(AbnormalTaskRecordParam param) {
        return patrolTaskService.taskAbnormalListStatistics(param);
    }

    /**
     * 巡更任务汇总统计
     *
     * @param param
     *
     * @return 任务总数环比, 任务状态数量统计, 线路总数, 巡更点, 巡更覆盖率
     */
    @GetMapping("/patrol/task/summary-statistics")
    public TaskStatisticsVO getPatrolTaskSummaryStatistics(@Valid BaseDateStatisticsParam param) {
        return TaskStatisticsVO.builder()
                .taskNumberStatistics(this.patrolStatisticsService.getTaskNumberChainRatioStatistics(param))
                .taskStatusStatistics(this.patrolStatisticsService.getTaskStatusStatistics(param))
                .taskLineStatistics(this.patrolStatisticsService.getTaskLineStatistics(param))
                .build();
    }

    /**
     * 巡更点异常top5统计(物联2.1)
     *
     * @param param 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/electron/abnormal/top")
    public List<LocationTopStatisticsVO> locationAbnormalTopStatisticsList(StatisticsQueryParam param) {
        return patrolTaskLocationService.locationAbnormalTopStatisticsList(param);
    }

    /**
     * 巡更点漏巡top5统计(物联2.1)
     *
     * @param param 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/electron/forgetPatrol/top")
    public List<LocationTopStatisticsVO> locationForgetTopStatisticsList(StatisticsQueryParam param) {
        return patrolTaskLocationService.locationForgetTopStatisticsList(param);
    }

    /**
     * 巡更点分布情况(物联2.1)
     *
     * @param param 统计时间范围
     *
     * @return 统计结果
     */
    @GetMapping("/electron/location/area")
    public LocationDistributeStatisticsVO locationDistributeStatistics(StatisticsQueryParam param) {
        return patrolLocationService.locationDistributeStatistics(param);
    }

    /**
     * 巡更任务巡更点异常占比(物联2.1)
     *
     * @param param 统计时间范围
     *
     * @return 统计结果
     */
    @GetMapping("/electron/task/location/abnormal")
    public TaskLocationStatisticsVO taskLocationAbnormalStatistics(StatisticsQueryParam param) {
        return patrolTaskLocationService.taskLocationAbnormalStatistics(param);
    }

    /**
     * 巡更任务巡更点漏巡占比(物联2.1)
     *
     * @param param 统计时间范围
     *
     * @return 统计结果
     */
    @GetMapping("/electron/task/location/missing")
    public TaskLocationStatisticsVO taskLocationMissingStatistics(StatisticsQueryParam param) {
        return patrolTaskLocationService.taskLocationMissingStatistics(param);
    }

    /**
     * 巡更路线统计(物联2.1)
     *
     * @return 统计结果
     */
    @GetMapping("/electron/location/route")
    public List<LocationRouteStatisticsVO> locationRouteStatistics(StatisticsQueryParam param) {
        return patrolTaskService.locationRouteStatistics(param);
    }
}
