package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.statistics;

import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerEfficiencyMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.HandlerRateMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TaskHandlerTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.ElectronQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.StatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.task.LocationQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PatrolRouteStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PersonnelStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 巡更统计
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatisticsController {
    private final PatrolTaskLocationService patrolTaskLocationService;
    private final PatrolTaskService patrolTaskService;

    /**
     * 巡更点统计
     *
     * @param param 统计查询参数
     *
     * @return 巡更点统计结果
     */
    @GetMapping("/electron/statistics/location")
    public Pagination<LocationStatisticsVO> locationStatistics(StatisticParam param) {
        return patrolTaskLocationService.locationStatistics(param);
    }

    /**
     * 巡更点统计导出
     *
     * @param param    导出参数
     * @param response 响应文件
     */
    @GetMapping("/electron/statistics/locationExport")
    public void locationExport(StatisticParam param, HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelWriter writer = patrolTaskLocationService.locationExport(param);
        setResponseInfo(response, "巡更点统计");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * 巡更人员统计
     *
     * @param param 统计参数
     *
     * @return 统计信息
     */
    @GetMapping("/electron/statistics/personnel")
    @DataPermissionSwitch(dataModel = "patrol:patrolTask")
    public Pagination<PersonnelStatisticsVO> personnelStatistics(StatisticParam param) {
        return patrolTaskLocationService.personnelStatistics(param);
    }

    /**
     * 巡更人员统计导出
     *
     * @param param    统计参数
     * @param response 导出文件
     */
    @GetMapping("/electron/statistics/personnelExport")
    @DataPermissionSwitch(dataModel = "patrol:patrolTask")
    public void personnelExport(StatisticParam param, HttpServletResponse response) throws UnsupportedEncodingException {
        ExcelWriter writer = patrolTaskLocationService.personnelExport(param);
        setResponseInfo(response, "巡更人员统计");
        try (var outputStream = response.getOutputStream()) {
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * 巡更路线统计
     */
    @GetMapping("/electron/statistics/patrolRoute")
    public Pagination<PatrolRouteStatisticsVO> patrolRouteStatistics(StatisticParam param) {
        return patrolTaskLocationService.patrolRouteStatistics(param);
    }

    /**
     * 巡更路线统计导出
     */
    @GetMapping("/electron/statistics/patrolRouteExport")
    public void patrolRouteStatisticsExport(StatisticParam param, HttpServletResponse response) {
        patrolTaskLocationService.patrolRouteStatisticsExport(param, response);
    }

    /**
     * 巡更异常率
     *
     * @return 率
     */
    @GetMapping("/electron/statistics/errRate")
    public TotalNumberRateDTO getElectronErrRate(ElectronQueryParam param) {
        return patrolTaskService.getElectronErrRate(param);
    }

    /**
     * 巡更完成率
     *
     * @return 率
     */
    @GetMapping("/electron/statistics/completeRate")
    public TotalNumberRateDTO getElectronCompleteRate(ElectronQueryParam param) {
        return patrolTaskService.getElectronCompleteRate(param);
    }

    /**
     * 巡更处理率指标
     */
    @GetMapping("/electron/statistics/complete/rate/metric")
    public HandlerRateMetricDTO getElectronCompleteRateMetric(@RequestParam("period") Period period) {
        return patrolTaskService.getElectronCompleteRateMetric(period);
    }

    /**
     * 巡更处理效率指标
     */
    @GetMapping("/electron/statistics/complete/efficiency/metric")
    public HandlerEfficiencyMetricDTO getElectronCompleteEfficiencyMetric(@RequestParam("period") Period period) {
        return patrolTaskService.getElectronCompleteEfficiencyMetric(period);
    }

    /**
     * 巡更处理总时长和处理数
     */
    @GetMapping("/electron/statistics/complete/total/time")
    public TaskHandlerTimeVO getElectronCompleteTotalTime(@RequestParam("period") Period period) {
        return patrolTaskService.getElectronCompleteTotalTime(period);
    }

    /**
     * 获取任务总数
     *
     * @param period 时间
     *
     * @return 总数
     */
    @GetMapping("/electron/statistics/taskTotal")
    public TotalNumberRateDTO getTaskTotal(@RequestParam("period") Period period) {
        Long taskTotal = patrolTaskService.getTaskTotal(period, null);

        return TotalNumberRateDTO.builder()
                .total(taskTotal)
                .number(taskTotal)
                .build();
    }

    /**
     * @Author wql
     * @Description 设置响应头
     * @Date 15:12 2023/5/30
     **/
    private void setResponseInfo(HttpServletResponse response, String name) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String fileName = URLEncoder.encode(name + DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss"), "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
    }

    /**
     * 巡更点数量统计
     */
    @PostMapping("/electron/task/locations/statistics")
    public List<LocationStatisticsVO> getLocationsStatistics(@RequestBody LocationQueryParam param) {
        return patrolTaskService.getLocationsStatistics(param);
    }

}
