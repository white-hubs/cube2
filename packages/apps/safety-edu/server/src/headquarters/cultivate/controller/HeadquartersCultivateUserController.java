package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.dao.HeadquartersCultivateTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.CultivateUserSignRecordExportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.CultivateUserSignRecordVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserRecordQueryParam;
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

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersCultivateUserController {

    private final HeadquartersCultivateTaskMapper headquartersCultivateTaskMapper;

    /**
     * 用户培训记录分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/headquarters/cultivate-user/records")
    public Pagination<CultivateUserSignRecordVO> getUserSignRecordPage(CultivateTaskUserRecordQueryParam param) {
        Page<CultivateUserSignRecordVO> userSignRecordPage = this.headquartersCultivateTaskMapper.getUserSignRecordPage(PagingUtils.convert(param), param);
        return PagingUtils.convert(userSignRecordPage);
    }

    /**
     * 培训记录导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/headquarters/cultivate-user/records/export")
    public void exportCultivateUserList(@Valid @RequestBody CultivateTaskUserRecordQueryParam param) throws IOException {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = URLEncoder.encode("培训记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        List<CultivateUserSignRecordExportVO> cultivateUserSignRecordExportVOS = this.headquartersCultivateTaskMapper.getUserSignRecordExport(param);
        EasyExcel.write(response.getOutputStream(), CultivateUserSignRecordExportVO.class).sheet("培训记录")
                .doWrite(cultivateUserSignRecordExportVOS);
    }
}
