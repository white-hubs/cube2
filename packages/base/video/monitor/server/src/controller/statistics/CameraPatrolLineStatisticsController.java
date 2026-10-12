package com.guhecloud.cube.biz.vmonitor.server.controller.statistics;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.client.AlarmStatisticsClient;
import com.guhecloud.cube.base.alarm.client.dto.AlarmBasicAnalysisStatisticsDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsPageQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.param.CameraPatrolLineStatisticsQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.service.CameraPatrolLineStatisticsService;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraDeviceAlarmStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolCurrencyStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolGrowthRateStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineAbnormalStatisticsVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineStatisticsExportVO;
import com.guhecloud.cube.biz.vmonitor.server.vo.CameraPatrolLineStatisticsPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 视频巡逻线路统计接口管理
 *
 * @author lanchun
 * @date 2022/11/9 13:50
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CameraPatrolLineStatisticsController {

    private final CameraPatrolLineStatisticsService cameraPatrolLineStatisticsService;

    private final AlarmStatisticsClient alarmStatisticsClient;

    @GetMapping("/camera-patrol/alarm-statistics")
    public CameraDeviceAlarmStatisticsVO getCameraDeviceAlarmStatistics(@Valid AlarmStatisticsQueryParam param) {
        AlarmBasicAnalysisStatisticsDTO alarmBasicAnalysisStatisticsDTO = this.alarmStatisticsClient.getAlarmBasicAnalysisRingStatistics(param);
        CameraDeviceAlarmStatisticsVO cameraDeviceAlarmStatisticsVO = CameraDeviceAlarmStatisticsVO
                .builder()
                .deviceNum(0L)
                .alarmNum(alarmBasicAnalysisStatisticsDTO.getAlarmNum())
                .ringRatio(alarmBasicAnalysisStatisticsDTO.getRingRatio())
                .build();
        return cameraDeviceAlarmStatisticsVO;
    }

    /**
     * 视频线路统计分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-line-statistics/page")
    public Pagination<CameraPatrolLineStatisticsPageVO> getPatrolLineStatisticsPage(@Valid CameraPatrolLineStatisticsPageQueryParam param) {
        Page<CameraPatrolLineStatisticsPageVO> cameraPatrolLineStatisticsPageVOPage = this.cameraPatrolLineStatisticsService.getPatrolLineStatisticsPage(param);
        if (CollectionUtils.isEmpty(cameraPatrolLineStatisticsPageVOPage.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(cameraPatrolLineStatisticsPageVOPage);
    }

    /**
     * 视频线路统计列表导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/camera-patrol-line-statistics/list/export")
    public void exportPatrolLineStatistics(@Valid @RequestBody CameraPatrolLineStatisticsPageQueryParam param) throws IOException {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = URLEncoder.encode("巡检路线统计", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        List<CameraPatrolLineStatisticsPageVO> cameraPatrolLineStatisticsPageVOS = this.cameraPatrolLineStatisticsService.getPatrolLineStatisticsList(param);
        List<CameraPatrolLineStatisticsExportVO> cameraPatrolLineStatisticsExportVOS = ObjectUtils.copying(cameraPatrolLineStatisticsPageVOS, CameraPatrolLineStatisticsExportVO.class);
        EasyExcel.write(response.getOutputStream(), CameraPatrolLineStatisticsExportVO.class).sheet("巡检路线统计")
                .doWrite(cameraPatrolLineStatisticsExportVOS);
    }

    /**
     * 巡检线路异常统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-line-statistics/group-abnormal")
    public List<CameraPatrolCurrencyStatisticsVO> getPatrolLineAbnormalGroupStatistics(@Valid CameraPatrolLineStatisticsQueryParam param) {
        return this.cameraPatrolLineStatisticsService.getPatrolLineAbnormalInnerGroupStatistics(param);
    }

    /**
     * 巡逻异常数增长率统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-line-statistics/abnormal-growth-rate")
    public CameraPatrolGrowthRateStatisticsVO getPatrolAbnormalRatioStatistics(@Valid CameraPatrolLineStatisticsQueryParam param) {
        return this.cameraPatrolLineStatisticsService.getPatrolAbnormalRatioStatistics(param);
    }

    /**
     * 巡逻次数增长率统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-line-statistics/record-rate")
    public CameraPatrolGrowthRateStatisticsVO getPatrolRecordRatioStatistics(@Valid CameraPatrolLineStatisticsQueryParam param) {
        return this.cameraPatrolLineStatisticsService.getPatrolRecordRatioStatistics(param);
    }

    /**
     * 巡逻异常情况统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/camera-patrol-line-statistics/abnormal")
    public CameraPatrolLineAbnormalStatisticsVO getCameraPatrolLineAbnormalStatistics(@Valid CameraPatrolLineStatisticsQueryParam param) {
        return this.cameraPatrolLineStatisticsService.getCameraPatrolLineAbnormalStatistics(param);
    }
}
