package com.guhecloud.cube.srv.gen.reportmanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.ReportType;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportTypeAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportTypeQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportTypeVo;
import com.guhecloud.platform.base.model.Pagination;

public interface ReportTypeService extends IService<ReportType> {
    void addReportType(ReportTypeAddOrUpdateParam param);

    void updateReportType(ReportTypeAddOrUpdateParam param);

    Pagination<ReportTypeVo> getReportTypeInfo(ReportTypeQueryParam param);

    void updateReportTypeStatus(Long id, Integer status);

    void deleteReportType(Long id);
}
