package com.guhecloud.cube.srv.gen.appraisals.server.process.controller;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.appraisals.server.process.service.FormAppraisalProcessService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalProcessController {
    private final FormAppraisalProcessService formAppraisalProcessService;

    /**
     * 表单考核的流程分页列表
     *
     * @param processInstancePageQuery param
     *
     * @return 分页列表
     */
    @GetMapping("/appraisal/process/allPage")
    public Pagination<ProcessInstanceDTO> allPageBatch(ProcessInstancePageQuery processInstancePageQuery) {
        return formAppraisalProcessService.allPageBatch(processInstancePageQuery);
    }

    /**
     * 表单考核的流程分页列表导出
     *
     * @param processInstancePageQuery param
     * @param response                 response
     */
    @GetMapping("/appraisal/process/allPage/export")
    public void export(ProcessInstancePageQuery processInstancePageQuery, HttpServletResponse response) {
        formAppraisalProcessService.export(processInstancePageQuery, response);
    }
}
