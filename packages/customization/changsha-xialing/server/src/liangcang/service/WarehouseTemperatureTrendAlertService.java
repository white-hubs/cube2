package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertGlobalConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertRecordPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertSampleParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertTrendParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureAlertWarehouseConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertGlobalConfigVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertNoticeVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertRecordVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertSampleDayVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertTrendVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureAlertWarehouseConfigVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/** 粮仓温度趋势预警总服务。 */
public interface WarehouseTemperatureTrendAlertService {

    /** 保存全局默认配置。 */
    Boolean saveGlobalConfig(WarehouseTemperatureAlertGlobalConfigSaveParam param);

    /** 查询全局默认配置。 */
    WarehouseTemperatureAlertGlobalConfigVO globalConfigDetail();

    /** 查询单仓配置详情。 */
    WarehouseTemperatureAlertWarehouseConfigVO warehouseConfigDetail(Long warehouseId);

    /** 保存单仓配置。 */
    Boolean saveWarehouseConfig(WarehouseTemperatureAlertWarehouseConfigSaveParam param);

    /** 重置单仓配置。 */
    Boolean resetWarehouseConfig(Long warehouseId);

    /** 预警记录分页。 */
    Pagination<WarehouseTemperatureAlertRecordVO> recordPage(WarehouseTemperatureAlertRecordPageParam param);

    /** 预警记录导出数据，供 Excel/下载使用。 */
    List<WarehouseTemperatureAlertRecordVO> recordExport(WarehouseTemperatureAlertRecordPageParam param);

    /** 预警记录导出，直接写入响应流。 */
    void exportRecord(jakarta.servlet.http.HttpServletResponse response, WarehouseTemperatureAlertRecordPageParam param) throws java.io.IOException;

    /** 温度走势。 */
    WarehouseTemperatureAlertTrendVO trend(WarehouseTemperatureAlertTrendParam param);

    /** 采样明细。 */
    List<WarehouseTemperatureAlertSampleDayVO> samples(WarehouseTemperatureAlertSampleParam param);

    /** 采样导出数据，按天展开。 */
    List<WarehouseTemperatureAlertSampleDayVO> sampleExport(WarehouseTemperatureAlertSampleParam param);

    /** 采样导出，直接写入响应流。 */
    void exportSample(jakarta.servlet.http.HttpServletResponse response, WarehouseTemperatureAlertSampleParam param) throws java.io.IOException;

    /** 货位卡提示，返回当前仓是否处于预警状态。 */
    WarehouseTemperatureAlertNoticeVO activeNotice(Long warehouseId);

    /** 30 分钟采样并检测，同一任务内完成采样、日均和预警状态更新。 */
    void calculateAndDetect();

    /** 每日完整性标记，把前一天的日均数据标为完整。 */
    void markPreviousDayComplete();
}
