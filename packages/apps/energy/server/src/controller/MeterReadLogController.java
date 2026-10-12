package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogDeleteParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogTemplateParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.MeterReadLogService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadLogErrorVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadMeterLogVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lrj
 * @Description 抄表周期
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MeterReadLogController {
    private final MeterReadLogService meterReadLogService;

    /**
     * 抄表记录
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/meter-read-log/page")
    public Pagination<ReadMeterLogVO> getPage(MeterReadLogParam param) {
        return meterReadLogService.getPage(param);
    }

    /**
     * 修改数据，通过覆盖的方式修改数据
     * 注意：只有influxdb中的time 和 tag 一致 才能覆盖
     *
     * @param param
     */
    @PostMapping(value = "/meter-read-log")
    public void updateReadMeterLog(@RequestBody ReadMeterLogVO param) {
        //处理时间
        param.setCurTime(param.getCurDateTime() != null ? DateUtils.toTimestamp(param.getCurDateTime()) : null);
        param.setPreTime(param.getPreDateTime() != null ? DateUtils.toTimestamp(param.getPreDateTime()) : null);
        meterReadLogService.updateReadMeterLog(param);
    }

    /**
     * 设备读数记录导出
     *
     * @param param    条件
     * @param request  请求
     * @param response 相应
     */
    @GetMapping("/meter-read-log/export")
    public void readMeterLogExport(MeterReadLogParam param, HttpServletRequest request, HttpServletResponse response) {
        meterReadLogService.readMeterLogExport(param, request, response);
    }

    /**
     * 批量删除读数记录
     *
     * @return 是否成功
     */
    @PostMapping("/meter-read-log/remove-batch")
    public Boolean batchReadLogRemoveBatch(@RequestBody MeterReadLogDeleteParam param) {
        return meterReadLogService.batchReadLogRemoveBatch(param.getDateRangeList());
    }

    /**
     * 下载模板(设备-读数记录)
     *
     * @param param    参数
     * @param request  请求
     * @param response 响应
     */
    @PostMapping("/meter-read-log/download-template")
    public void downloadReadLogTemplate(@RequestBody MeterReadLogTemplateParam param, HttpServletRequest request,
                                        HttpServletResponse response
                                       ) {
        meterReadLogService.downloadReadLogTemplate(param, request, response);
    }

    /**
     * 导入读数记录
     */
    @PostMapping("/meter-read-log/import-excel")
    public MeterReadLogErrorVO importReadLogExcel(@RequestParam MultipartFile file) {
        return meterReadLogService.importReadLogExcel(file);
    }
}