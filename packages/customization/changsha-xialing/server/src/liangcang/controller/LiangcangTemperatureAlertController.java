package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertGlobalConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertSampleParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertWarehouseConfigDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertWarehouseConfigResetParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertWarehouseConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendAlertService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertGlobalConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertNoticeVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertSampleDayVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertTrendVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertWarehouseConfigVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/** 粮仓温度趋势预警。 */
@RestController
@RequiredArgsConstructor
public class LiangcangTemperatureAlertController {

    private final WarehouseTemperatureTrendAlertService temperatureAlertService;

    /** 查询全局默认配置。 */
    @GetMapping("/liangcang/warehouse-management/temperature-alert/config/global")
    public WarehouseTemperatureAlertGlobalConfigVO globalConfigDetail() {
        return temperatureAlertService.globalConfigDetail();
    }

    /** 保存全局默认配置。 */
    @PostMapping("/liangcang/warehouse-management/temperature-alert/config/global/save")
    public Boolean saveGlobalConfig(@RequestBody WarehouseTemperatureAlertGlobalConfigSaveParam param) {
        return temperatureAlertService.saveGlobalConfig(param);
    }

    /** 查询单仓配置详情。 */
    @GetMapping("/liangcang/warehouse-management/temperature-alert/config/warehouse/detail")
    public WarehouseTemperatureAlertWarehouseConfigVO warehouseConfigDetail(
            @ModelAttribute WarehouseTemperatureAlertWarehouseConfigDetailParam param) {
        return temperatureAlertService.warehouseConfigDetail(param == null ? null : param.getWarehouseId());
    }

    /** 保存单仓配置。 */
    @PostMapping("/liangcang/warehouse-management/temperature-alert/config/warehouse/save")
    public Boolean saveWarehouseConfig(@RequestBody WarehouseTemperatureAlertWarehouseConfigSaveParam param) {
        return temperatureAlertService.saveWarehouseConfig(param);
    }

    /** 重置单仓配置为全局默认。 */
    @PostMapping("/liangcang/warehouse-management/temperature-alert/config/warehouse/reset")
    public Boolean resetWarehouseConfig(@RequestBody WarehouseTemperatureAlertWarehouseConfigResetParam param) {
        return temperatureAlertService.resetWarehouseConfig(param == null ? null : param.getWarehouseId());
    }

    /** 预警记录分页。 */
    @GetMapping("/liangcang/warehouse-management/temperature-alert/records/page")
    public Pagination<WarehouseTemperatureAlertRecordVO> recordPage(@ModelAttribute WarehouseTemperatureAlertRecordPageParam param) {
        return temperatureAlertService.recordPage(param);
    }

    /** 预警记录导出。 */
    @GetMapping("/liangcang/warehouse-management/temperature-alert/records/export")
    public void exportRecords(HttpServletResponse response, @ModelAttribute WarehouseTemperatureAlertRecordPageParam param)
            throws IOException {
        temperatureAlertService.exportRecord(response, param);
    }

    /** 温度走势查询。 */
    @GetMapping("/liangcang/warehouse-management/temperature-alert/trend")
    public WarehouseTemperatureAlertTrendVO trend(@ModelAttribute WarehouseTemperatureAlertTrendParam param) {
        return temperatureAlertService.trend(param);
    }

    /** 采样明细查询。 */
    @GetMapping("/liangcang/warehouse-management/temperature-alert/samples")
    public List<WarehouseTemperatureAlertSampleDayVO> samples(@ModelAttribute WarehouseTemperatureAlertSampleParam param) {
        return temperatureAlertService.samples(param);
    }

    /** 采样明细导出。 */
    @GetMapping("/liangcang/warehouse-management/temperature-alert/samples/export")
    public void exportSamples(HttpServletResponse response, @ModelAttribute WarehouseTemperatureAlertSampleParam param)
            throws IOException {
        temperatureAlertService.exportSample(response, param);
    }

    /** 货位卡红色提示条数据。 */
    @GetMapping("/liangcang/warehouse-management/temperature-alert/notice")
    public WarehouseTemperatureAlertNoticeVO notice(@ModelAttribute WarehouseTemperatureAlertWarehouseConfigDetailParam param) {
        return temperatureAlertService.activeNotice(param == null ? null : param.getWarehouseId());
    }
}
