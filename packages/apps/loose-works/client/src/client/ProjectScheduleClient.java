package com.guhecloud.cube.srv.gen.loose.works.client;

import com.guhecloud.cube.srv.gen.loose.works.MetricType;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectScheduleIdTypeDTO;
import com.guhecloud.cube.srv.gen.loose.works.param.ProjectScheduleQuery;
import com.guhecloud.cube.srv.gen.loose.works.param.ProjectScheduleQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectDataForAnalysisVO;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectDataForReportVO;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleMetricVO;
import com.guhecloud.cube.srv.gen.loose.works.vo.ProjectScheduleVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "looseWorks")
public interface ProjectScheduleClient {

    /**
     * 获取小散工程总数、同比环比、以及异常数据
     *
     * @param period
     *
     * @return
     */
    @GetMapping("/project/ratio/safety-report")
    ProjectDataForReportVO projectDataForReport(@RequestParam("period") Period period);

    @GetMapping("/abnormal/ratio/safety/analysis")
    ProjectDataForAnalysisVO projectDataForAbnormalSafetyAnalysis(@RequestParam("period") Period period, @RequestParam("type") String type);

    /**
     * 根据ID获取工程监管信息
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/project/schedule/get/by/id")
    ProjectScheduleVO projectScheduleGetById(@RequestParam("id") Long id);

    /**
     * 批量获取获取工程监管信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/project/schedule/get/list")
    List<ProjectScheduleVO> projectScheduleBatchGet(@SpringQueryMap ProjectScheduleQueryParam param);

    /**
     * 批量获取获取工程监管信息
     *
     * @param param 条件
     *
     * @return 列表
     */
    @PostMapping("/project/schedule/post/list")
    List<ProjectScheduleVO> projectScheduleBatchPost(@RequestBody ProjectScheduleQueryParam param);

    /**
     * 获取小散工程总数
     */
    @GetMapping("/project/schedule/count")
    Long projectScheduleCount();

    /**
     * 获取小散工程完成率，整改率，整改效率
     */
    @GetMapping("/project/schedule/metric")
    ProjectScheduleMetricVO projectScheduleMetric(@RequestParam("period") Period period, @RequestParam("metricType") MetricType metricType);

    @PostMapping("/project/schedule/page")
    Pagination<ProjectScheduleVO> getProjectScheduleList(@RequestBody ProjectScheduleQuery query);

    /**
     * 获取工程监管ID列表
     *
     * @param query 查询参数
     *
     * @return 工程监管ID列表
     */
    @PostMapping("/project/schedule/idTypes")
    List<ProjectScheduleIdTypeDTO> getProjectScheduleIdTypes(@RequestBody ProjectScheduleQuery query);
}
