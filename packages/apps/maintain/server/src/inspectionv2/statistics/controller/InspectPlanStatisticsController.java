package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.convert.InspectPlanStatisticsExportConvert;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.dto.InspectPlanStatisticsExportDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectPlanStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.vo.InspectPlanStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.service.InspectPlanStatisticsService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectPlanStatisticsController {

    private static final int EXPORT_BATCH_SIZE = 2000;

    private final InspectPlanStatisticsService inspectPlanStatisticsService;

    @GetMapping("/statistic-analysis/planStatistics")
    public Pagination<InspectPlanStatisticsVO> getPage(@Valid InspectPlanStatisticsParam param) {
        return inspectPlanStatisticsService.getPage(param);
    }

    @PostMapping("/statistic-analysis/planStatisticsExport")
    public void export(@RequestBody(required = false) InspectPlanStatisticsParam param, HttpServletResponse response) throws IOException {
        InspectPlanStatisticsParam queryParam = param == null ? new InspectPlanStatisticsParam() : param;
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("巡检计划统计", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
        WriteSheet sheet = EasyExcel.writerSheet("巡检计划统计")
                .head(InspectPlanStatisticsExportDTO.class)
                .build();

        boolean wroteData = false;
        int pageNumber = 1;
        try (var outputStream = response.getOutputStream()) {
            ExcelWriter writer = EasyExcel.write(outputStream).build();
            try {
                while (true) {
                    queryParam.setPageNumber(pageNumber);
                    queryParam.setPageSize(EXPORT_BATCH_SIZE);
                    List<InspectPlanStatisticsVO> records = inspectPlanStatisticsService.getExportPage(queryParam);
                    if (CollectionUtils.isEmpty(records)) {
                        break;
                    }
                    writer.write(InspectPlanStatisticsExportConvert.convert(records), sheet);
                    wroteData = true;
                    if (records.size() < EXPORT_BATCH_SIZE) {
                        break;
                    }
                    pageNumber++;
                }
                if (!wroteData) {
                    writer.write(Collections.emptyList(), sheet);
                }
            } finally {
                writer.finish();
            }
        }
    }
}
