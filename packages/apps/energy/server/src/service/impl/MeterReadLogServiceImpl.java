package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dto.ReadLogExcelDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogDeleteParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogTemplateParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterReadLogService;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.InfluxdbUtil;
import com.guhecloud.cube.srv.gen.energymanagement.server.util.PageResponse;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadLogErrorVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadMeterLogExportVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadMeterLogVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RedLogErrorVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.DeletePredicateRequest;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_WITH_COLON_FORMAT;

/**
 * 描述：能耗采集时段定义服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterReadLogServiceImpl implements MeterReadLogService {
    @Value("${influx.dbNameEnergy}")
    private String dbName;
    @Value("${influx.dborg}")
    private String dborg;

    private final ExcelUtils excelUtils;
    private final InfluxDBClient influxdbClient;
    private final MeterDeviceMapper meterDeviceMapper;

    @Override
    public Pagination<ReadMeterLogVO> getPage(MeterReadLogParam param) {
        List<ReadMeterLogVO> readMeterLogVOS = this.meterLogList(param);
        // 内存分页
        return PageResponse.doPage(readMeterLogVOS, param.getPageNumber(), param.getPageSize());
    }

    @Override
    public void updateReadMeterLog(ReadMeterLogVO param) {
        Point pointForMeterLog = param.createPointForMeterLog(param);
        //通过时间覆盖数据 注意time字段 时间一致即会覆盖
        WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
        writeApiBlocking.writePoint(dbName, dborg, pointForMeterLog);
    }

    @Override
    public void readMeterLogExport(MeterReadLogParam param, HttpServletRequest request, HttpServletResponse response) {
        List<ReadMeterLogVO> dataList = this.meterLogList(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(dataList), "未查询到数据，无法导出");
        List<ReadMeterLogExportVO> finalDataList = dataList.stream().map(log -> {
            ReadMeterLogExportVO exportVO = new ReadMeterLogExportVO();
            BeanUtils.copyProperties(log, exportVO);
            exportVO.setPreDateTimeStr(DateUtils.formatDate(log.getPreDateTime()));
            exportVO.setCurDateTimeStr(DateUtils.formatDate(log.getCurDateTime()));
            return exportVO;
        }).collect(Collectors.toList());
        // 导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("deviceId", "序号");
        writer.addHeaderAlias("deviceCode", "设备编码");
        writer.addHeaderAlias("deviceName", "设备名称");
        writer.addHeaderAlias("preDateTimeStr", "上期抄表时间");
        writer.addHeaderAlias("preReadValue", "上次抄表数");
        writer.addHeaderAlias("curDateTimeStr", "本期抄表时间");
        writer.addHeaderAlias("readValue", "抄表值");
        writer.addHeaderAlias("usage", "用量");
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(finalDataList, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "采集读数记录" + DateUtils.format(LocalDateTime.now(), COMPACT_DATETIME_WITH_COLON_FORMAT);
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    private List<ReadMeterLogVO> meterLogList(MeterReadLogParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 设备条件查询
        boolean deviceWhereBool = StringUtils.isNotBlank(param.getAreaIds()) || StringUtils.isNotBlank(param.getDeviceKeyword()) ||
                                  StringUtils.isNotBlank(param.getDeviceIds()) || StringUtils.isNotBlank(param.getInstrumentType());
        if (deviceWhereBool) {
            LambdaQueryWrapper<MeterDevice> wrapper = Wrappers.lambdaQuery();
            if (StringUtils.isNotBlank(param.getInstrumentType())) {
                wrapper.eq(MeterDevice::getInstrumentType, param.getInstrumentType());
            }
            if (StringUtils.isNotBlank(param.getAreaIds())) {
                List<Long> areaIdList = Arrays.stream(param.getAreaIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
                wrapper.in(MeterDevice::getLastAreaId, areaIdList);
            }
            if (StringUtils.isNotBlank(param.getDeviceKeyword())) {
                wrapper.and(i -> i.like(MeterDevice::getCode, param.getDeviceKeyword()).or()
                        .like(MeterDevice::getDeviceName, param.getDeviceKeyword()));
            }
            if (StringUtils.isNotBlank(param.getDeviceIds())) {
                List<String> deviceIdList = Arrays.stream(param.getDeviceIds().split(",")).collect(Collectors.toList());
                wrapper.in(MeterDevice::getDeviceId, deviceIdList);
            }
            List<MeterDevice> deviceList = meterDeviceMapper.selectList(wrapper);
            if (deviceList == null || deviceList.isEmpty()) {
                param.setDeviceIds("0");
            } else {
                param.setDeviceIds(deviceList.stream().map(MeterDevice::getDeviceId).distinct().collect(Collectors.joining(",")));
            }
        }
        int limitNumber = 10;
        if (StringUtils.isBlank(param.getDeviceIds()) || param.getDeviceIds().length() > 1000) {
            limitNumber = 1;
        } else if (param.getDeviceIds().length() > 1 && param.getDeviceIds().length() < 1000) {
            limitNumber = 500 / (param.getDeviceIds().length() / 19);
        }
        //初始化influxdb需要的时间格式
        param.initInfluxdbPeriod();
        //定义查询语句
        String influxdbQuerySql = "from(bucket: \"energy\")\n" +
                                  "  |> range(start: " + param.getStartTime() + ", stop: " + param.getEndTime() + ")\n" +
                                  "  |> filter(fn: (r) => r[\"_measurement\"] == \"readMeterLog\")\n" +
                                  "  |> filter(fn: (r) => r.tenant==\"" + tenant + "\" " +
                                  InfluxdbUtil.getInfluxdbDeviceIdQuery(param.getDeviceIds()) + ")\n" +
                                  "  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n" +
                                  "  |> sort(columns: [\"_time\"],desc: true)\n" +
                                  // pivot后再limit，避免截断同一条读数记录的部分字段
                                  "  |> limit(n: " + limitNumber + ")";
        return InfluxdbUtil.execQueryForList(influxdbQuerySql, influxdbClient, ReadMeterLogVO.class);
    }

    /**
     * 读数记录批量删除
     *
     * @param dateRangeList 删除对象集合
     *
     * @return 是否删除成功
     */
    @Override
    public Boolean batchReadLogRemoveBatch(List<MeterReadLogDeleteParam.DateRange> dateRangeList) {
        if (CollectionUtils.isEmpty(dateRangeList)) {
            return true;
        }
        ReadMeterLogVO meterLogVO = new ReadMeterLogVO();
        meterLogVO.setCurDateTime(LocalDateTime.now());
        var deleteApi = influxdbClient.getDeleteApi();
        dateRangeList.forEach(dateRange -> {
            var predicate = "_measurement = \"readMeterLog\" AND deviceId = \"" + dateRange.getDeviceId() + "\"";
            var startTime = dateRange.getTime().atOffset(ZoneOffset.UTC).plusHours(-8);
            var endTime = startTime.plusHours(38);
            var deletePredicateRequest = new DeletePredicateRequest();
            deletePredicateRequest.setStart(startTime);
            deletePredicateRequest.setStop(endTime);
            deletePredicateRequest.setPredicate(predicate);
            try {
                deleteApi.delete(deletePredicateRequest, dbName, dborg);
            } catch (Exception e) {
                log.error("删除数据失败{}:{}！", "time", predicate, e);
            }

        });
        return true;
    }

    /**
     * 读数记录模板下载
     *
     * @param param    模板参数
     * @param request  请求
     * @param response 响应
     */
    @Override
    public void downloadReadLogTemplate(MeterReadLogTemplateParam param, HttpServletRequest request, HttpServletResponse response) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getDeviceIdList()), "设备不能为空！");
        var deviceList = meterDeviceMapper.selectList(Wrappers.<MeterDevice>lambdaQuery()
                                                              .in(MeterDevice::getDeviceId, param.getDeviceIdList()));
        Assert.isNotFound(CollectionUtils.isEmpty(deviceList), "未查询到要导出的设备！");
        List<Map<String, String>> excelDataList = deviceList.stream().map(this::getTemplateDataMap)
                .collect(Collectors.toList());
        Map<String, String> headers = this.getHeaderList();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setHeaderAlias(headers);
        writer.setOnlyAlias(true);
        // 设置表头背景色
        CellStyle headCellStyle = writer.getHeadCellStyle();
        StyleUtil.setColor(headCellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
        //默认配置
        writer.write(excelDataList, true);
        // 设置列宽
        writer.setColumnWidth(-1, 20);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        String fileName = "读数记录";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 读数记录导入
     *
     * @param file 记录文件
     *
     * @return 是否成功
     */
    @Override
    public MeterReadLogErrorVO importReadLogExcel(MultipartFile file) {
        MeterReadLogErrorVO result = new MeterReadLogErrorVO();
        Assert.isParameterError(file == null, "请选择导入文件！");
        List<ReadLogExcelDTO> excelDataList;
        try {
            excelDataList = excelUtils.readExcelFile(file, ReadLogExcelDTO.class);
        } catch (IOException e) {
            log.error("excel读取失败:", e);
            throw new ServiceException("导入数据读取错误！");
        }
        if (CollectionUtils.isEmpty(excelDataList)) {
            throw new ServiceException("导入数据为空！");
        }
        var resultList = new ArrayList<RedLogErrorVO>();
        /* 获取设备信息Map以便获取使用 */
        var deviceIdSet = excelDataList.stream().map(ReadLogExcelDTO::getDeviceId).collect(Collectors.toSet());
        var deviceMap = meterDeviceMapper.selectList(Wrappers.<MeterDevice>lambdaQuery()
                                                             .in(MeterDevice::getDeviceId, deviceIdSet)
                                                             .groupBy(MeterDevice::getDeviceId)).stream()
                .collect(Collectors.toMap(MeterDevice::getDeviceId, v -> v));
        // 获取当前租户
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        WriteApiBlocking writeApiBlocking = influxdbClient.getWriteApiBlocking();
        // 遍历
        Integer okCount = 0;
        int size = excelDataList.size();
        for (int i = 0; i < size; i++) {
            var excelData = excelDataList.get(i);
            if (!this.checkReadLogDOT(excelData, i, deviceMap, resultList)) {
                continue;
            }

            MeterDevice meterDevice = deviceMap.get(String.valueOf(excelData.getDeviceId()));
            if (excelData.getPreTime() == null) {
                excelData.setPreTime(excelData.getCurTime());
            }
            if (excelData.getPreReadValue() == null) {
                BigDecimal calculatedPreReadValue;
                if (excelData.getUsage() != null) {
                    // 如果已知用量，通过当前读数减去用量计算上次读数
                    calculatedPreReadValue = excelData.getReadValue().subtract(excelData.getUsage());
                } else {
                    // 如果没有用量信息，使用当前读数作为上次读数（或设置为0）
                    calculatedPreReadValue = excelData.getReadValue();
                }
                excelData.setPreReadValue(calculatedPreReadValue.setScale(2, RoundingMode.HALF_UP));
            }
            if (excelData.getUsage() == null) {
                excelData.setUsage(excelData.getReadValue().subtract(excelData.getPreReadValue()).setScale(2, RoundingMode.HALF_UP));
            }
            var point = Point.measurement("readMeterLog")
                    .addTag("deviceId", String.valueOf(excelData.getDeviceId()))//设备id
                    .addTag("tenant", String.valueOf(tenant))
                    .addTag("instrumentType", meterDevice.getInstrumentType());
            if (StringUtils.isNotBlank(meterDevice.getDeviceAreaId())) {
                point.addTag("areaId", meterDevice.getDeviceAreaId());
            }
            point.addField("deviceCode", meterDevice.getCode())
                    .addField("deviceName", meterDevice.getDeviceName())
                    .addField("curTime", DateUtils.toTimestamp(excelData.getCurTime()))
                    .addField("readValue", excelData.getReadValue().setScale(2, RoundingMode.HALF_UP))
                    .addField("preTime", excelData.getPreTime() != null ? DateUtils.toTimestamp(excelData.getPreTime()) : null)
                    .addField("preReadValue", excelData.getPreReadValue())
                    .addField("usage", excelData.getUsage())//用量
                    .addField("collectType", "hand")
                    .time(excelData.getCurTime().atZone(ZoneId.systemDefault()).toInstant(), WritePrecision.S);
            try {
                writeApiBlocking.writePoint(dbName, dborg, point);
                okCount++;
            } catch (InfluxException e) {
                log.error("写入数据错误{}:{}:", excelData.getCurTime(), excelData.getDeviceId(), e);
            }
        }
        result.setSuccessCount(okCount);
        result.setErrorList(resultList);
        return result;
    }

    private boolean checkReadLogDOT(ReadLogExcelDTO excelData, Integer i, Map<String, MeterDevice> deviceMap,
                                    List<RedLogErrorVO> resultList
                                   ) {
        var errorMsg = new StringBuilder();
        if (excelData.getDeviceId() == null) {
            errorMsg.append("，").append("设备id不能为空！");
        }
        if (excelData.getCurTime() == null) {
            errorMsg.append("，").append("采集时间不能为空！");
        }
        if (excelData.getReadValue() == null) {
            errorMsg.append("，").append("读数不能为空！");
        }
        if (!deviceMap.containsKey(String.valueOf(excelData.getDeviceId()))) {
            errorMsg.append("，").append("未找到该设备！");
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            resultList.add(RedLogErrorVO.builder().errorRow(i + 1).errorMsg(errorMsg.substring(1)).build());
            return false;
        }
        return true;
    }

    private Map<String, String> getHeaderList() {
        Map<String, String> headers = new LinkedHashMap<>(8);
        headers.put("deviceId", "设备id");
        headers.put("deviceCode", "设备编码");
        headers.put("deviceName", "设备名称");
        headers.put("preTime", "上期抄表时间");
        headers.put("preReadValue", "上次抄表数");
        headers.put("curTime", "*本期抄表时间");
        headers.put("readValue", "*抄表值");
        headers.put("usage", "用量");
        return headers;
    }

    private Map<String, String> getTemplateDataMap(MeterDevice meterDevice) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("deviceId", meterDevice.getDeviceId());
        map.put("deviceCode", meterDevice.getCode());
        map.put("deviceName", meterDevice.getDeviceName());
        map.put("preTime", "");
        map.put("preReadValue", "");
        map.put("curTime", "");
        map.put("readValue", "");
        map.put("usage", "");
        return map;
    }

}
