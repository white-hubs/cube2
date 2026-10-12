package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.SecurityCommonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.PowerConsumptionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.RecordTimeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.PowerDistributionBoxService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.DistributionsExcelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.DistributionsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricityExcelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricityVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.RecordTimeVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PowerDistributionBoxController {

    private final PowerDistributionBoxService powerDistributionBoxService;

    private final SecurityCommonService securityCommonService;

    /**
     * @Description 用电时间记录列表
     * @Author lrj
     */
    @GetMapping("/getElectricityTimeRecord")
    public Pagination<RecordTimeVo> getElectricityTimeRecord(RecordTimeParam param) {
        return powerDistributionBoxService.getElectricityTimeRecord(param);
    }

    /**
     * @Description 配电时间记录列表分页
     * @Author lrj
     */
    @GetMapping("/getDistributionTimeRecord")
    public Pagination<RecordTimeVo> getDistributionTimeRecord(RecordTimeParam param) {
        return powerDistributionBoxService.getDistributionTimeRecord(param);
    }

    /**
     * @Description 用电详情
     * @Author lrj
     */
    @GetMapping("/getElectricityTimeRecordDetails")
    public Map<String, Object> getElectricityTimeRecordDetails(RecordTimeParam recordTimeParam) {
        HashMap<String, Object> remap = new HashMap<>();
        List<ElectricityVo> electricityTimeRecordDetails = powerDistributionBoxService.getElectricityTimeRecordDetails(recordTimeParam);
        BigDecimal total = new BigDecimal("0.00");
        for (ElectricityVo s : electricityTimeRecordDetails) {
            SecurityDevice transformerMeter = securityCommonService.getOne(Wrappers.<SecurityDevice>lambdaQuery()
                                                                                   .eq(SecurityDevice::getDeviceId, s.getTransformerMeterCode()));
            if (Objects.nonNull(transformerMeter)) {
                String codeByDeviceId = transformerMeter.getCode();
                s.setTransformerMeterCode(codeByDeviceId);
            }
            SecurityDevice powerMeter = securityCommonService.getOne(Wrappers.<SecurityDevice>lambdaQuery()
                                                                             .eq(SecurityDevice::getDeviceId, s.getPowerMeterCode()));
            if (Objects.nonNull(powerMeter)) {
                s.setPowerMeterCode(powerMeter.getCode());
            }
            if (Objects.nonNull(s.getTransformerCurrentQuantity())) {
                total = total.add(new BigDecimal(s.getTransformerCurrentQuantity()));
            }
        }
        remap.put("total", total);
        remap.put("electricity", electricityTimeRecordDetails);
        String period = recordTimeParam.getPeriod();
        String[] split = period.split(",");
        var parse = DateUtils.parse(split[0], "yyyy-MM-dd");
        remap.put("collectTime", parse);
        return remap;
    }

    /**
     * @Description 配电详情
     * @Author lrj
     */
    @GetMapping("/getDistributionsTimeRecordDetails")
    public List<DistributionsVo> getDistributionsTimeRecordDetails(RecordTimeParam recordTimeParam) {
        return powerDistributionBoxService.getDistributionsTimeRecordDetails(recordTimeParam);
    }

    /**
     * @Description 用电导表
     * @Author lrj
     */
    @GetMapping("/electricityDownloadExcel")
    public void downLoadFile(HttpServletResponse response, RecordTimeParam param) throws IOException {
        List<ElectricityVo> electricityTimeRecordDetails = powerDistributionBoxService.getElectricityTimeRecordDetails(param);
        List<ElectricityExcelVo> copying = ObjectUtils.copying(electricityTimeRecordDetails, ElectricityExcelVo.class);
        String period = param.getPeriod();
        String[] split = period.split(",");
        var parse = DateUtils.parse(split[0]);
        BigDecimal total = new BigDecimal("0.00");
        for (ElectricityExcelVo s : copying) {

            SecurityDevice transformerMeter = securityCommonService.getOne(Wrappers.<SecurityDevice>lambdaQuery()
                                                                                   .eq(SecurityDevice::getDeviceId, s.getTransformerMeterCode()));
            if (Objects.nonNull(transformerMeter)) {
                String codeByDeviceId = transformerMeter.getCode();
                s.setTransformerMeterCode(codeByDeviceId);
            }
            SecurityDevice powerMeter = securityCommonService.getOne(Wrappers.<SecurityDevice>lambdaQuery()
                                                                             .eq(SecurityDevice::getDeviceId, s.getPowerMeterCode()));
            if (Objects.nonNull(powerMeter)) {
                s.setPowerMeterCode(powerMeter.getCode());
            }
            if (Objects.nonNull(s.getTransformerCurrentQuantity())) {
                total = total.add(new BigDecimal(s.getTransformerCurrentQuantity()));
            }
            s.setCollectTime(DateUtils.format(parse, "yyyy-MM-dd"));
        }

        BigDecimal finalTotal = total;
        copying.forEach(a -> {
            a.setTodayTotal(finalTotal.toString());
        });

        //1.获取excel模板
        TemplateExportParams params = new TemplateExportParams(
                "static/yongdian.xlsx");
        //2.获取所有数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alllist", copying);
        //3.执行excel导出
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), 1, 0, 1, 2, 3, 4, 9);
        String fileName = "用电导表记录.xlsx";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);

        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        response.setHeader("Content-Length", String.valueOf(is.available()));
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream sout = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sout);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导表", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }

    }

    /**
     * @Description 配电导表
     * @Author lrj
     */
    @GetMapping("/distributionsDownloadExcel")
    public void distributionsDownloadExcel(HttpServletResponse response, RecordTimeParam param) throws IOException {
        List<DistributionsExcelVo> copying = powerDistributionBoxService.getDistributionsTimeRecordExcel(param);

        //1.获取excel模板
        TemplateExportParams params = new TemplateExportParams(
                "static/peidian.xlsx");
        //2.获取所有数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alllist", copying);
        //3.执行excel导出
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        String fileName = "配电导表记录.xlsx";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);

        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        response.setHeader("Content-Length", String.valueOf(is.available()));
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream sout = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sout);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("导表", e);
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }

    }

    /**
     * @Description 耗电量
     * @Author lrj
     */
    @GetMapping("/power-consumption")
    public BigDecimal getPowerConsumption(PowerConsumptionParam param) {
        return powerDistributionBoxService.getPowerConsumption(param);
    }

}
