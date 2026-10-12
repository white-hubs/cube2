package com.guhecloud.cube.srv.gen.appraisals.server.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.appraisals.server.process.entity.FormAppraisalProcess;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;

public interface FormAppraisalProcessService extends IService<FormAppraisalProcess> {
    Pagination<ProcessInstanceDTO> allPageBatch(ProcessInstancePageQuery processInstancePageQuery);

    void export(ProcessInstancePageQuery processInstancePageQuery, HttpServletResponse response);
}
