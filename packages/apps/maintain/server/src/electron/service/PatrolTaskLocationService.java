package com.guhecloud.cube.srv.gen.maintain.server.electron.service;

import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTaskLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.StatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.PatrolTaskResultParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationTopStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PatrolRouteStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PersonnelStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.TaskLocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.ElectronTaskSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskDataVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.TaskTimeQuantumVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 巡更记录
 * @date 2023/5/29 14:46
 */
public interface PatrolTaskLocationService extends IService<PatrolTaskLocation> {
    /**
     * @Author wql
     * @Description 生成任务对应的巡更记录
     * @Date 10:52 2023/5/31
     **/
    void saveTaskLocation(Long id, Long planId, List<Long> locationIds, TaskTimeQuantumVO taskTimeQuantumVO);

    void saveTaskLocation(Long id, String belonger, List<String> executePeriod, Long planId, List<Long> locationIds,
                          LocalDateTime dateTime
                         );

    /**
     * @Author wql
     * @Description 获取巡更任务的巡更记录
     * @Date 10:52 2023/5/31
     **/
    List<PatrolTaskDataVO> getTaskLocation(Long id, String executePeriod, String areaSequence, Boolean resultNull);

    /**
     * @Author wql
     * @Description 提交巡更结果
     * @Date 10:52 2023/5/31
     **/
    Long submitResult(PatrolTaskResultParam param);

    /**
     * @Author wql
     * @Description 统计该任务还未巡更记录数
     * @Date 10:52 2023/5/31
     **/
    Long getNotInspectedNum(Long taskId);

    /**
     * @param taskId 任务id
     * @param hand   true表示只查手动填写的异常，过期自动异常不会查询。(有巡更时间的是手动填写
     *
     * @Author wql
     * @Description 查询结果异常的巡巡更录
     * @Date 10:52 2023/5/31
     **/
    Long countAbnormalResultRecord(Long taskId, Boolean hand);

    /**
     * @Author wql
     * @Description 将剩余未巡更的记录改为超时异常
     * @Date 10:53 2023/5/31
     **/
    void overtimeRecord(Long userId, Long taskId);

    /**
     * @Author wql
     * @Description 更新未巡更记录的巡更人员
     * @Date 10:53 2023/5/31
     **/
    void updateUserId(Long userId, Long id);

    /**
     * @Author wql
     * @Description 统计查询参数
     * @Date 15:14 2023/5/30
     **/
    Pagination<LocationStatisticsVO> locationStatistics(StatisticParam param);

    /**
     * @Description 巡更点异常top5统计
     * @Date 15:14 2023/5/30
     **/
    List<LocationStatisticsVO> locationStatisticsTop5(StatisticParam param);

    /**
     * 统计时间段内的巡更点，巡检次数
     * 可筛选异常次数；按照降序进行排列
     *
     * @param param
     *
     * @return
     */
    List<LocationStatisticsVO> getPatrolExpStat(StatisticParam param);

    /**
     * @Author wql
     * @Description 巡更点统计导出
     * @Date 15:15 2023/5/30
     **/
    ExcelWriter locationExport(StatisticParam param);

    /**
     * @Author wql
     * @Description 巡更人员统计
     * @Date 15:20 2023/5/30
     **/
    Pagination<PersonnelStatisticsVO> personnelStatistics(StatisticParam param);

    /**
     * @Author wql
     * @Description 巡更人员统计导出
     * @Date 15:20 2023/5/30
     **/
    ExcelWriter personnelExport(StatisticParam param);

    /**
     * @Author lijie
     * @Description 更新隐患工单状态
     * @Date 18:20 2023/5/31
     **/
    Boolean updateHidden(List<String> hiddenDangerIds);

    /**
     * @Author wql
     * @Description 获取巡更点最新的巡更任务
     * @Date 8:59 2023/6/5
     **/
    PatrolTaskDataVO getNewestTask(Long locationId);

    List<LocationTopStatisticsVO> locationAbnormalTopStatisticsList(StatisticsQueryParam param);

    List<LocationTopStatisticsVO> locationForgetTopStatisticsList(StatisticsQueryParam param);

    TaskLocationStatisticsVO taskLocationAbnormalStatistics(StatisticsQueryParam param);

    TaskLocationStatisticsVO taskLocationMissingStatistics(StatisticsQueryParam param);

    ElectronTaskSafetyReportVO getElectronTaskForSafetyReport(Period period);

    List<Long> getTaskIdByLocationId(Long locationId);

    Pagination<PatrolRouteStatisticsVO> patrolRouteStatistics(StatisticParam param);

    void patrolRouteStatisticsExport(StatisticParam param, HttpServletResponse response);
}
