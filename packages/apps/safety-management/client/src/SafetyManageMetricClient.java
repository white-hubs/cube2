package com.guhecloud.cube.base.dataintegrate.client;

import com.guhecloud.cube.base.dataintegrate.client.param.AlarmMetricQuery;
import com.guhecloud.cube.base.dataintegrate.client.param.BaseStatisticsQuery;
import com.guhecloud.cube.base.dataintegrate.client.param.LooseWorkMetricQuery;
import com.guhecloud.cube.base.dataintegrate.client.param.RiskSourceMetricQuery;
import com.guhecloud.cube.base.dataintegrate.client.param.SafetyEducationStatisticsQuery;
import com.guhecloud.cube.base.dataintegrate.client.vo.AlarmMarketTimeoutCountVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.AlarmTimeoutCountVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.DeviceApplicationStatisticsQuery;
import com.guhecloud.cube.base.dataintegrate.client.vo.DeviceCountStatusVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.GroupCountVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.SafetyEducationTaskRatioVO;
import com.guhecloud.cube.base.dataintegrate.client.vo.SubGroupCountVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/9 16:06
 * @Description:
 */
@FeignClient(name = "com.guhecloud.cube.srv.gen.safety.management")
public interface SafetyManageMetricClient {
    /**
     * 统计各状态计数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/count/status")
    List<GroupCountVO> countStatus(@SpringQueryMap AlarmMetricQuery query);

    /**
     * 统计各状态计数（按市场汇总）
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/count/status/group-by-market")
    List<SubGroupCountVO> countStatusGroupByMarket(@SpringQueryMap AlarmMetricQuery query);

    /**
     * 统计超时记录数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/count/timeout")
    AlarmTimeoutCountVO countTimeout(@SpringQueryMap AlarmMetricQuery query);

    /**
     * 统计各市场超时记录数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-alarm-statistics/count/timeout/group-by-market")
    List<AlarmMarketTimeoutCountVO> countTimeoutByMarket(@SpringQueryMap AlarmMetricQuery query);

    /**
     * 根据应用种类统计设备状态数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-device-statistics/count/status/application")
    List<DeviceCountStatusVO> countStatusByApplication(@SpringQueryMap DeviceApplicationStatisticsQuery query);

    /**
     * 预案文件数量
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-emergency/count")
    Integer emergencyFileCount(@SpringQueryMap BaseStatisticsQuery query);

    /**
     * 隐患状态分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/hidden-danger/count-process-status")
    List<GroupCountVO> hiddenDangerCountProcessStatus(@SpringQueryMap BaseStatisticsQuery query);

    /**
     * 巡检任务状态分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/inspect/task/count/status")
    List<GroupCountVO> inspectTaskCountStatus(@SpringQueryMap BaseStatisticsQuery query);

    /**
     * 小散工程状态分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/loose/works/statistics/count/status")
    List<GroupCountVO> looseWorksCountStatus(@SpringQueryMap BaseStatisticsQuery query);

    /**
     * 小散工程数量
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/loose/works/statistics/count")
    Integer looseWorkCount(@SpringQueryMap LooseWorkMetricQuery query);

    /**
     * 巡更任务状态分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/patrol-task/count/status")
    List<GroupCountVO> patrolTaskCountStatus(@SpringQueryMap BaseStatisticsQuery query);

    /**
     * 风险源建档数量分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/risk/source/count")
    List<GroupCountVO> riskSourceCountByMarket(@SpringQueryMap BaseStatisticsQuery query);

    /**
     * 风险源类型数量分布
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety/statistics/risk/source/count/type")
    List<GroupCountVO> riskSourceCountByType(@SpringQueryMap RiskSourceMetricQuery query);

    /**
     * 安全教育完成数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/count/completed")
    Integer safetyEducationCountCompleted(@SpringQueryMap BaseStatisticsQuery query);

    /**
     * 安全教育任务频率
     *
     * @param safetyEducationStatisticsQuery
     *
     * @return
     */
    @GetMapping("/safety-education-statistics/count/task-ratio")
    SafetyEducationTaskRatioVO getSafetyEducationTaskRatio(@SpringQueryMap SafetyEducationStatisticsQuery safetyEducationStatisticsQuery);
}
