package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.job.WholesalersDataJob;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.AssociationParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.DataExcelTemplate;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.UpdateDataParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.WholesalerQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWholesalerService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.OperateLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataDayService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataMonthService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.WaterDataYearService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailDayVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailMonthVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailYearVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WholesalerVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ColdWholesalerController {
    private final ColdWholesalerService wholesalerService;

    private final WaterDataDayService waterDataDayService;

    private final WaterDataMonthService waterDataMonthService;

    private final WaterDataYearService waterDataYearService;

    private final OperateLogService operateLogService;

    private final WholesalersDataJob wholesalersDataJob;

    /**
     * @Description 批发商基础数据保存
     * @Author lrj
     */
    @GetMapping("/add-wholesalers")
    public void addWholesalers() {
        wholesalerService.addWholesalers();
    }

    /**
     * @Description 商户配置列表
     * @Author lrj
     */
    @GetMapping("/wholesalers")
    public Pagination<WholesalerVo> getWholesalers(WholesalerQueryParam param) {
        return wholesalerService.getWholesalers(param);
    }

    /**
     * @Description 商户关联档位
     * @Author lrj
     */
    @PostMapping("/association")
    public void association(@RequestBody AssociationParam associationParam) {
        wholesalerService.association(associationParam);
    }

    /**
     * @Description 商户取消关联档位
     * @Author lrj
     */
    @GetMapping("/association-cancellation/{id}")
    public void associationCancellation(@PathVariable Long id) {
        wholesalerService.associationCancellation(id);
    }

    /**
     * @Description 表报明细（日）
     * @Author lrj
     */
    @PostMapping("/report-detail-day")
    public Map<String, Object> reportDetailDay(@RequestBody ReportDetailParam reportDetailParam) {
        return waterDataDayService.reportDetailDay(reportDetailParam);
    }

    /**
     * @Description 表报明细（日）导表
     * @Author lrj
     */
    @PostMapping("/report-detail-day/excel")
    public void reportDetailDayExcel(HttpServletResponse response, @RequestBody ReportDetailParam param
                                    ) throws UnsupportedEncodingException {
        List<ReportDetailDayVo> reportDetailDayVos = waterDataDayService.reportDetailDayExcel(param);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("num", "序号");
        writer.addHeaderAlias("statisticalFlag", "统计日期");
        writer.addHeaderAlias("code", "档主编号");
        writer.addHeaderAlias("shiftMaster", "档主姓名");
        writer.addHeaderAlias("gearPositionCode", "档位号");
        writer.addHeaderAlias("departmentName", "管理部门");
        writer.addHeaderAlias("gearPositionArea", "所属区域");
        writer.addHeaderAlias("deviceCode", "钛泡编号");
        writer.addHeaderAlias("unitTraffic", "冷媒单位流量（m³/h)");
        writer.addHeaderAlias("duration", "使用时长（小时）");
        writer.addHeaderAlias("flow", "冷媒流量（m³）");
        writer.addHeaderAlias("unitPrice", "单价（元/m³）");
        writer.addHeaderAlias("total", "费用合计（元）");
        writer.setOnlyAlias(true);//排除字段操作
        writer.write(reportDetailDayVos, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");

        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date, COMPACT_DATETIME_FORMAT);
        String name = "报表明细（日）" + format1;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            ;
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("导出报表失败", e);
        } finally {
            writer.close();
        }
    }

    /**
     * @Description 电费表报
     * @Author lrj
     */
    @PostMapping("/report-detail-electricity")
    public Map<String, Object> reportDetailElectricity(@RequestBody ReportDetailParam reportDetailParam) {
        return waterDataDayService.reportDetailElectricity(reportDetailParam);
    }

    /**
     * @Description 电费表报明细导表
     * @Author lrj
     */
    @PostMapping("/report-detail-electricity/excel")
    public void reportDetailElectricityExcel(HttpServletResponse response, @RequestBody ReportDetailParam param
                                            ) throws UnsupportedEncodingException {
        List<ReportDetailDayVo> reportDetailDayVos = waterDataDayService.reportDetailDayExcelElectricCharge(param);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("num", "序号");
        writer.addHeaderAlias("code", "档主编号");
        writer.addHeaderAlias("shiftMaster", "档主姓名");
        writer.addHeaderAlias("gearPositionCode", "档位号");
        writer.addHeaderAlias("departmentName", "管理部门");
        writer.addHeaderAlias("gearPositionArea", "所属区域");
        writer.addHeaderAlias("deviceCode", "钛泡编号");
        writer.addHeaderAlias("unitTraffic", "冷媒单位流量（m³/h)");
        writer.addHeaderAlias("duration", "使用时长（小时）");
        writer.addHeaderAlias("flow", "冷媒流量（m³）");
        writer.addHeaderAlias("unitPrice", "单价（元/m³）");
        writer.addHeaderAlias("total", "费用合计（元）");
        writer.addHeaderAlias("totalQOQ", "环比金额合计（元）");
        writer.addHeaderAlias("flowQOQ", "环比冷媒流量");
        writer.addHeaderAlias("durationQOQ", "环比时长（h）");
        writer.addHeaderAlias("totalQOQRatio", "环比金额合计比例");
        writer.addHeaderAlias("flowQOQRatio", "环比冷媒流量比例");
        writer.addHeaderAlias("durationQOQRatio", "环比时长比例");
        writer.addHeaderAlias("totalYOY", "同比费用合计（元）");
        writer.addHeaderAlias("flowYOY", "同比冷媒流量");
        writer.addHeaderAlias("durationYOY", "同比时长（h）");
        writer.addHeaderAlias("totalYOYRatio", "同比费用合计比例");
        writer.addHeaderAlias("flowYOYRatio", "同比冷媒流量比例");
        writer.addHeaderAlias("durationYOYRatio", "同比时长比例");
        writer.setOnlyAlias(true);//排除字段操作
        writer.write(reportDetailDayVos, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");

        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date, COMPACT_DATETIME_FORMAT);
        String name = "电费报表" + format1;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Description 表报明细（月）
     * @Author lrj
     */
    @PostMapping("/report-detail-month")
    public Map<String, Object> reportDetailMonth(@RequestBody ReportDetailParam reportDetailParam) {
        Map<String, Object> map = waterDataMonthService.reportDetailMonth(reportDetailParam);
        return map;
    }

    /**
     * @Description 表报明细（月）导表
     * @Author lrj
     */
    @PostMapping("/report-detail-month/excel")
    public void reportDetailMonthExcel(HttpServletResponse response, @RequestBody ReportDetailParam param
                                      ) throws UnsupportedEncodingException {
        List<ReportDetailMonthVo> reportDetailMonthVos = waterDataMonthService.reportDetailMonthExcel(param);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("num", "序号");
        writer.addHeaderAlias("statisticalFlag", "统计日期");
        writer.addHeaderAlias("statisticalCycle", "统计周期");
        writer.addHeaderAlias("code", "档主编号");
        writer.addHeaderAlias("shiftMaster", "档主姓名");
        writer.addHeaderAlias("gearPositionCode", "档位号");
        writer.addHeaderAlias("departmentName", "管理部门");
        writer.addHeaderAlias("gearPositionArea", "所属区域");
        writer.addHeaderAlias("deviceCode", "钛泡编号");
        writer.addHeaderAlias("unitTraffic", "冷媒单位流量（m³/h)");
        writer.addHeaderAlias("duration", "使用时长（小时）");
        writer.addHeaderAlias("flow", "冷媒流量（m³）");
        writer.addHeaderAlias("unitPrice", "单价（元/m³）");
        writer.addHeaderAlias("total", "费用合计（元）");
        writer.setOnlyAlias(true);//排除字段操作
        writer.write(reportDetailMonthVos, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");

        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date, COMPACT_DATETIME_FORMAT);
        String name = "报表明细（月）" + format1;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

    /**
     * @Description 表报明细（年）
     * @Author lrj
     */
    @PostMapping("/report-detail-year")
    public Map<String, Object> reportDetailYear(@RequestBody ReportDetailParam reportDetailParam) {
        Map<String, Object> map = waterDataYearService.reportDetailYear(reportDetailParam);
        List<String> objects = new ArrayList<>();
        int month = LocalDateTime.now().getMonthValue();
        objects.add("code");
        objects.add("shiftMaster");
        objects.add("gearPositionCode");
        objects.add("departmentName");
        objects.add("gearPositionArea");
        objects.add("deviceCode");
        objects.add("unitTraffic");
        if (month == 1) {
            objects.add("one");
        } else if (month == 2) {
            objects.add("one");
            objects.add("two");
        } else if (month == 3) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
        } else if (month == 4) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
            objects.add("four");
        } else if (month == 5) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
            objects.add("four");
            objects.add("five");
        } else if (month == 6) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
            objects.add("four");
            objects.add("five");
            objects.add("six");
        } else if (month == 7) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
            objects.add("four");
            objects.add("five");
            objects.add("six");
            objects.add("seven");
        } else if (month == 8) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
            objects.add("four");
            objects.add("five");
            objects.add("six");
            objects.add("seven");
            objects.add("eight");
        } else if (month == 9) {
            objects.add("one");
            objects.add("three");
            objects.add("four");
            objects.add("five");
            objects.add("six");
            objects.add("seven");
            objects.add("eight");
            objects.add("nine");
        } else if (month == 10) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
            objects.add("four");
            objects.add("five");
            objects.add("six");
            objects.add("seven");
            objects.add("eight");
            objects.add("nine");
            objects.add("ten");
        } else if (month == 11) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
            objects.add("four");
            objects.add("five");
            objects.add("six");
            objects.add("seven");
            objects.add("eight");
            objects.add("nine");
            objects.add("ten");
            objects.add("eleven");
        } else if (month == 12) {
            objects.add("one");
            objects.add("two");
            objects.add("three");
            objects.add("four");
            objects.add("five");
            objects.add("six");
            objects.add("seven");
            objects.add("eight");
            objects.add("nine");
            objects.add("ten");
            objects.add("eleven");
            objects.add("twelve");
        }
        objects.add("total");
        map.put("title", objects);
        return map;
    }

    /**
     * @Description 表报明细（年）导表
     * @Author lrj
     */
    @PostMapping("/report-detail-year/excel")
    public void reportDetailYear(HttpServletResponse response, @RequestBody ReportDetailParam param) throws UnsupportedEncodingException {
        List<ReportDetailYearVo> reportDetailYearExcel = waterDataYearService.reportDetailYearExcel(param);
        int year = LocalDateTime.now().getYear();
        //当年
        Integer a = 12;
        if (year == Integer.parseInt(param.getTimeFlag())) {
            a = LocalDateTime.now().getMonthValue();
        }
        ExcelWriter writer = ExcelUtil.getWriter();

        writer.addHeaderAlias("code", "档主编号");
        writer.addHeaderAlias("shiftMaster", "档主姓名");
        writer.addHeaderAlias("gearPositionCode", "档位号");
        writer.addHeaderAlias("departmentName", "管理部门");
        writer.addHeaderAlias("gearPositionArea", "所属区域");
        writer.addHeaderAlias("deviceCode", "钛泡编号");
        writer.addHeaderAlias("unitTraffic", "冷媒单位流量（m³/h)");
        addWriter(a, writer);
        writer.addHeaderAlias("total", "费用合计（元）");
        writer.setOnlyAlias(true);//排除字段操作
        writer.write(reportDetailYearExcel, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        LocalDateTime date = LocalDateTime.now();
        String format1 = DateUtils.format(date, COMPACT_DATETIME_FORMAT);
        String name = "报表明细（年）" + format1;
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("导出报表失败", e);
        } finally {
            writer.close();
        }
    }

    private void addWriter(Integer month, ExcelWriter writer) {
        if (month == 1) {
            writer.addHeaderAlias("one", "1月");
        } else if (month == 2) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
        } else if (month == 3) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
        } else if (month == 4) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
        } else if (month == 5) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
            writer.addHeaderAlias("five", "5月");
        } else if (month == 6) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
            writer.addHeaderAlias("five", "5月");
            writer.addHeaderAlias("six", "6月");
        } else if (month == 7) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
            writer.addHeaderAlias("five", "5月");
            writer.addHeaderAlias("six", "6月");
            writer.addHeaderAlias("seven", "7月");
        } else if (month == 8) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
            writer.addHeaderAlias("five", "5月");
            writer.addHeaderAlias("six", "6月");
            writer.addHeaderAlias("seven", "7月");
            writer.addHeaderAlias("eight", "8月");
        } else if (month == 9) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
            writer.addHeaderAlias("five", "5月");
            writer.addHeaderAlias("six", "6月");
            writer.addHeaderAlias("seven", "7月");
            writer.addHeaderAlias("eight", "8月");
            writer.addHeaderAlias("nine", "9月");
        } else if (month == 10) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
            writer.addHeaderAlias("five", "5月");
            writer.addHeaderAlias("six", "6月");
            writer.addHeaderAlias("seven", "7月");
            writer.addHeaderAlias("eight", "8月");
            writer.addHeaderAlias("nine", "9月");
            writer.addHeaderAlias("ten", "10月");
        } else if (month == 11) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
            writer.addHeaderAlias("five", "5月");
            writer.addHeaderAlias("six", "6月");
            writer.addHeaderAlias("seven", "7月");
            writer.addHeaderAlias("eight", "8月");
            writer.addHeaderAlias("nine", "9月");
            writer.addHeaderAlias("ten", "10月");
            writer.addHeaderAlias("eleven", "11月");
        } else if (month == 12) {
            writer.addHeaderAlias("one", "1月");
            writer.addHeaderAlias("two", "2月");
            writer.addHeaderAlias("three", "3月");
            writer.addHeaderAlias("four", "4月");
            writer.addHeaderAlias("five", "5月");
            writer.addHeaderAlias("six", "6月");
            writer.addHeaderAlias("seven", "7月");
            writer.addHeaderAlias("eight", "8月");
            writer.addHeaderAlias("nine", "9月");
            writer.addHeaderAlias("ten", "10月");
            writer.addHeaderAlias("eleven", "11月");
            writer.addHeaderAlias("twelve", "12月");
        }

    }

    /**
     * @Description 数据补录
     * @Author lrj
     */
    @PostMapping("/add-data")
    public void addData(@RequestPart("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Map<String, Object>> readAll = reader.readAll();
        if (CollectionUtils.isNotEmpty(readAll)) {
            for (Map<String, Object> map : readAll) {
                try {
                    UpdateDataParam updateDataParam = new UpdateDataParam();
                    for (String s : map.keySet()) {
                        Object o = map.get(s);
                        if (s.equals("设备编号")) {
                            updateDataParam.setDeviceCode(o.toString());
                        } else if (s.equals("数据时间")) {
                            updateDataParam.setDate(o.toString());
                        } else if (s.equals("正确分钟值")) {
                            updateDataParam.setMinute(new BigDecimal(o.toString()));
                        }

                    }
                    waterDataDayService.addData(updateDataParam);
                    operateLogService.saveBusinessLog("数据补录:" + updateDataParam.toString());
                } catch (Exception e) {
                    log.error("补录失败================>", e);
                }
            }

        }

    }

    /**
     * @Description 修改数据
     * @Author lrj
     */
    @PostMapping("/update-data")
    public void updateData(@RequestPart("file") MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Map<String, Object>> readAll = reader.readAll();
        if (CollectionUtils.isNotEmpty(readAll)) {
            for (Map<String, Object> map : readAll) {
                try {
                    UpdateDataParam updateDataParam = new UpdateDataParam();
                    for (String s : map.keySet()) {
                        Object o = map.get(s);
                        if (s.equals("设备编号")) {
                            updateDataParam.setDeviceCode(o.toString());
                        } else if (s.equals("数据时间")) {
                            updateDataParam.setDate(o.toString());
                        } else if (s.equals("正确分钟值")) {
                            updateDataParam.setMinute(new BigDecimal(o.toString()));
                        } else if (s.equals("错误分钟值")) {
                            updateDataParam.setOldMinute(new BigDecimal(o.toString()));
                        }

                    }
                    waterDataDayService.updateData(updateDataParam);
                    operateLogService.saveBusinessLog("修改数据:" + updateDataParam.toString());
                } catch (Exception e) {
                    log.error("修改失败================>", e);
                }
            }

        }

    }

    /**
     * @Description 导表模板
     * @Author lrj
     */
    @PostMapping("/data-download-template")
    public void downloadTemplate(HttpServletResponse response) throws UnsupportedEncodingException {
        DataExcelTemplate excelTemplate = new DataExcelTemplate();
        List<DataExcelTemplate> excelList = new ArrayList<>();
        excelList.add(excelTemplate);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("date", "数据时间");
        writer.addHeaderAlias("deviceCode", "设备编号");
        writer.addHeaderAlias("minute", "正确分钟值");
        writer.addHeaderAlias("oldMinute", "错误分钟值");
        writer.setOnlyAlias(true);//排除字段操作
        writer.write(excelList, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String name = "数据修改导表模板";
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("导出模板失败", e);
        } finally {
            writer.close();
        }
    }

    @GetMapping("/aaaa")
    public void aaa(String param) {
        wholesalersDataJob.wholesalersData(param);
    }

}
