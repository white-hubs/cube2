package com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.srv.gen.maintain.server.common.param.StatisticsQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.TrendDecimalStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.param.MaintainTaskTotalParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.param.MaintenanceDeviceQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.service.MaintenanceStatisticsService;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo.MaintainTaskNumVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo.MaintainTaskPersonNumVO;
import com.guhecloud.cube.srv.gen.maintain.server.upkeep.statistics.vo.MaintenanceDeviceStatisticsPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 维保统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MaintenanceStatisticsController {

    private final MaintenanceStatisticsService maintenanceStatisticsService;

    /**
     * 设备统计分页列表
     *
     * @param param 请求参数
     *
     * @return Pagination<MaintenanceDeviceStatisticsPageVO>
     */
    @GetMapping("/maintenance/device/statistics/page")
    public Pagination<MaintenanceDeviceStatisticsPageVO> getDeviceStatisticsPage(@Valid MaintenanceDeviceQueryParam param) {
        return maintenanceStatisticsService.getDeviceStatisticsPage(param);
    }

    /**
     * 设备统计导出
     *
     * @param param 导出参数
     */
    @GetMapping("/maintenance/device/statistics/export")
    public void deviceStatisticsExport(MaintenanceDeviceQueryParam param) throws UnsupportedEncodingException {
        List<MaintenanceDeviceStatisticsPageVO> deviceStatistics = this.maintenanceStatisticsService.getDeviceStatistics(param);
        Assert.parameterIsNull(deviceStatistics, "暂无数据，无法导出");
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        try {
            response.setCharacterEncoding("utf-8");
            //这里URLEncoder.encode可以防止中文乱码
            String excelName = URLEncoder.encode("维保统计", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), MaintenanceDeviceStatisticsPageVO.class)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .sheet("维保统计")
                    .doWrite(deviceStatistics);
        } catch (Exception e) {
            log.debug("maintain:维保统计导出异常：", e);
            throw new ServiceException("下载异常");
        }
    }

    /**
     * 维护统计分析-维护计划执行率统计
     */
    @GetMapping("/maintenance/task/statistics")
    public MaintainTaskNumVO getMaintainTaskNumRatio() {
        return maintenanceStatisticsService.getMaintainTaskNumRatio();
    }

    /**
     * 维护统计分析-人员执行率统计
     */
    @GetMapping("/maintenance/person/statistics")
    public List<MaintainTaskPersonNumVO> getPersonStatisticsRatio() {
        return maintenanceStatisticsService.getPersonStatisticsRatio();
    }

    /**
     * 维护统计分析-不同设备执行率统计
     */
    @GetMapping("/maintenance/device/statistics")
    public List<MaintainTaskPersonNumVO> getDeviceStatistics() {
        return maintenanceStatisticsService.getDeviceExecuteStatistics();
    }

    /**
     * 维护统计分析-执行率趋势分析
     */
    @GetMapping("/maintenance/trend/statistics")
    public List<TrendDecimalStatisticsVO> getTrendStatistics(StatisticsQueryParam param) {
        return maintenanceStatisticsService.getTrendStatistics(param);
    }

    /**
     * 维护统计分析-执行时间分析
     */
    @GetMapping("/maintenance/time/statistics")
    public List<TrendDecimalStatisticsVO> getTimeStatistics() {
        return maintenanceStatisticsService.getTimeStatistics();
    }

    /**
     * 获取任务总数
     *
     * @param param 条件
     *
     * @return 总数
     */
    @PostMapping("/maintain/task-total/post")
    public Long maintainTaskTotalStat(@RequestBody MaintainTaskTotalParam param) {
        return maintenanceStatisticsService.maintainTaskTotalStat(param);
    }
}
