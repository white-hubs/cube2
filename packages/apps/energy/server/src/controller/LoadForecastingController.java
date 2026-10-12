package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.LoadForecastingParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.LoadForecastingService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.LoadForecastingVO;
import com.guhecloud.platform.base.util.DateUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;

/**
 * @Author wql
 * @Description 负荷预测
 * @Date 17:51 2023/4/10
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoadForecastingController {

    private final LoadForecastingService loadForecastingService;

    @PostMapping(value = "/load-forecasting/trend")
    public List<LoadForecastingVO> getLoadForecasting(@RequestBody LoadForecastingParam param) {
        return loadForecastingService.getLoadForecasting(param);
    }

    @PostMapping(value = "/load-forecasting/export")
    public void reportExport(@RequestBody LoadForecastingParam param, HttpServletResponse response) throws UnsupportedEncodingException {
        List<LoadForecastingVO> voList = loadForecastingService.getLoadForecasting(param);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("timeQuantum", "时间段");
        writer.addHeaderAlias("predictedValue", "预测值");
        writer.addHeaderAlias("historicalValue", "历史值");
        writer.addHeaderAlias("deviationValue", "偏差值");
        writer.addHeaderAlias("deviationPercent", "偏差百分比");
        List<Map<String, String>> list = new ArrayList<>();
        if (Objects.nonNull(voList)) {
            voList.forEach(o -> {
                Map<String, String> map = new HashMap<>();
                map.put("timeQuantum", o.getTimeQuantum());
                map.put("predictedValue", o.getPredictedValue());
                map.put("historicalValue", o.getHistoricalValue());
                map.put("deviationValue", o.getDeviationValue());
                map.put("deviationPercent", o.getDeviationPercent());
                list.add(map);
            });
        }
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String fileName = URLEncoder.encode(
                "负荷预测" + DateUtils.formatDate(LocalDateTime.now(), COMPACT_DATETIME_FORMAT), "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }

}
