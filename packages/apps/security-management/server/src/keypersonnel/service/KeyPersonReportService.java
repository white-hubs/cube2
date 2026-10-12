package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.entity.KeyPersonSupervisionReport;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonReportPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonReportSaveParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonReportVO;
import com.guhecloud.platform.base.model.Pagination;

public interface KeyPersonReportService extends IService<KeyPersonSupervisionReport> {
    Long create(Long personId, KeyPersonReportSaveParam param);

    Pagination<KeyPersonReportVO> getReportPage(Long personId, KeyPersonReportPageParam param);

    KeyPersonReportVO getReport(Long id);
}
