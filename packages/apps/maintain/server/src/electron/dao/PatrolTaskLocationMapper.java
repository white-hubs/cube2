package com.guhecloud.cube.srv.gen.maintain.server.electron.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTask;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolTaskLocation;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.StatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.HomePageStatisticsVO1;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationTopStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PatrolRouteStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PersonnelStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.ElectronLocationSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.PatrolTaskDataVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task.TaskLocationVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wql
 * @describe 巡更记录
 * @date 2023/5/29 14:45
 */
@Mapper
public interface PatrolTaskLocationMapper extends BaseMapper<PatrolTaskLocation> {
    /**
     * @Author wql
     * @Description 巡更点统计
     * @Date 15:58 2023/5/30
     **/
    Page<LocationStatisticsVO> locationStatistics(@Param("page") Page page, @Param("param") StatisticParam param);

    /**
     * @Author wql
     * @Description 巡更点统计导出
     * @Date 15:58 2023/5/30
     **/
    List<LocationStatisticsVO> locationExport(@Param("param") StatisticParam param);

    /**
     * @Author wql
     * @Description 巡更人员统计
     * @Date 17:22 2023/5/31
     **/
    Page<PersonnelStatisticsVO> personnelStatistics(@Param("page") Page<PatrolTaskLocation> page, @Param("param") StatisticParam param);

    /**
     * @Author wql
     * @Description 巡更人统计导出
     * @Date 18:10 2023/5/31
     **/
    List<PersonnelStatisticsVO> personnelExport(@Param("param") StatisticParam param);

    /**
     * @Author wql
     * @Description 根据巡更点id查询所有执行中的巡更任务
     * @Date 9:06 2023/6/5
     **/
    List<PatrolTask> getNewestTask(@Param("locationId") Long id, @Param("status") String status);

    /**
     * 批量插入更新
     *
     * @param list
     */
    void insertOrUpdateBatch(List<PatrolTaskLocation> list);

    //巡更点异常top5统计
    List<LocationStatisticsVO> getLocationStatistics(@Param("param") StatisticParam param);

    List<LocationStatisticsVO> getPatrolExpStat(@Param("param") StatisticParam param);

    /**
     * @Author wql
     * @Description 按巡更点顺序查询任务巡更点信息
     * @Date 16:11 2023/10/26
     **/
    List<TaskLocationVO> getTaskLocation(@Param("id") Long id);

    List<PatrolTaskDataVO> getTaskLocationByBluetooth(@Param("bluetoothCodes") List<String> bluetoothCodes, @Param("userId") Long userId);

    List<LocationTopStatisticsVO> locationAbnormalTopStatisticsList(@Param("period") Period period);

    List<LocationTopStatisticsVO> locationForgetTopStatisticsList(@Param("period") Period period);

    List<ElectronLocationSafetyReportVO> selectPatrolDataForSafetyReport(@Param("period") Period period);

    Long countNum(@Param("period") Period month);

    Integer patrolRouteStatisticsTotal(@Param("param") StatisticParam param);

    List<PatrolRouteStatisticsVO> patrolRouteStatistics(@Param("param") StatisticParam param);

    List<HomePageStatisticsVO1> patrolTaskHomePageStatistics(@Param("period") Period period);

    /**
     * 获取用户任务的巡更点数量
     *
     * @param userId     用户id 必填
     * @param resultNull 是否返回结果为空的数据
     * @param taskIds    任务ids 可选
     *
     * @return
     */
    List<Map<String, Object>> userTaskLocationCount(@Param("userId") Long userId, @Param("resultNull") Boolean resultNull,
                                                    @Param("taskIds") List<Long> taskIds
                                                   );
}
