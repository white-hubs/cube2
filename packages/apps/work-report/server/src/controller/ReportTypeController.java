package com.guhecloud.cube.srv.gen.reportmanagement.server.controller;

import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportTypeAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportTypeQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.ReportTypeService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportTypeVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 工作汇报类型
 * @Date 2023/3/22 15:30
 * @Author tangzhiqiang
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportTypeController {

    private final ReportTypeService reportTypeService;

    /**
     * 新增汇报类型
     *
     * @param param
     */
    @PostMapping("/report-type-add")
    public void insertReportType(@RequestBody ReportTypeAddOrUpdateParam param) {

        reportTypeService.addReportType(param);
    }

    /**
     * 修改汇报类型
     *
     * @param param
     */
    @PutMapping("/report-type-update")
    public void updateReportType(@RequestBody ReportTypeAddOrUpdateParam param) {
        reportTypeService.updateReportType(param);
    }

    /**
     * 汇报类型页查询
     *
     * @param param
     */
    @GetMapping("/report-type-page")
    public Pagination<ReportTypeVo> getReportTypeInfo(ReportTypeQueryParam param) {
        return reportTypeService.getReportTypeInfo(param);
    }

    /**
     * 修改汇报类型状态
     */
    @PutMapping("/report-type/{id}")
    public void updateReportTypeStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        reportTypeService.updateReportTypeStatus(id, status);
    }

    /**
     * 删除汇报类型
     */
    @DeleteMapping("/report-type/{id}")
    public void deleteReportType(@PathVariable("id") Long id) {
        reportTypeService.deleteReportType(id);
    }
}
