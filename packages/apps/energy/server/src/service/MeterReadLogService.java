package com.guhecloud.cube.srv.gen.energymanagement.server.service;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogDeleteParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.MeterReadLogTemplateParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.MeterReadLogErrorVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.ReadMeterLogVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 描述：抄表记录分页
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
public interface MeterReadLogService {
    Pagination<ReadMeterLogVO> getPage(MeterReadLogParam param);

    void updateReadMeterLog(ReadMeterLogVO param);

    void readMeterLogExport(MeterReadLogParam param, HttpServletRequest request, HttpServletResponse response);

    Boolean batchReadLogRemoveBatch(List<MeterReadLogDeleteParam.DateRange> dateRangeList);

    void downloadReadLogTemplate(MeterReadLogTemplateParam param, HttpServletRequest request, HttpServletResponse response);

    MeterReadLogErrorVO importReadLogExcel(MultipartFile file);
}