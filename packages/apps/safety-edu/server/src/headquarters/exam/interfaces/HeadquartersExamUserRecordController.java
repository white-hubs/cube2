package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.querygateway.query.ExamTaskUserRecordStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.repository.mapper.HeadquartersExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo.ExamTaskUserRecordExportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo.ExamTaskUserRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author lanchun
 * @date 2024/4/11 18:10
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamUserRecordController {

    private final HeadquartersExamTaskMapper headquartersExamTaskMapper;

    /**
     * 用户考试记录分页列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/headquarters/exam-user/records")
    public Pagination<ExamTaskUserRecordVO> getExamTaskUserRecordPage(ExamTaskUserRecordStatisticsQuery query) {
        Page<ExamTaskUserRecordVO> examTaskUserRecordPage = this.headquartersExamTaskMapper.
                selectExamTaskUserRecordPage(PagingUtils.convert(query),
                                             SessionContextHolder.getRequester().getTenant(),
                                             query);
        return PagingUtils.convert(examTaskUserRecordPage);
    }

    /**
     * 考试记录导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/headquarters/exam-user/records/export")
    public void exportExamUserList(@Valid @RequestBody ExamTaskUserRecordStatisticsQuery param) throws IOException {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = URLEncoder.encode("考试记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        List<ExamTaskUserRecordExportVO> examTaskUserRecordExportVOS = this.headquartersExamTaskMapper.selectExamUserRecordExport(SessionContextHolder
                                                                                                                                          .getRequester()
                                                                                                                                          .getTenant(), param);
        EasyExcel.write(response.getOutputStream(), ExamTaskUserRecordExportVO.class).sheet("考试记录")
                .doWrite(examTaskUserRecordExportVOS);
    }
}
