package com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.DevcieCountVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultReportVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultReport;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.ReportQuery;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

public interface FaultReportQueryService extends IService<FaultReport> {
    Pagination<FaultReportVo> getReport(ReportQuery reportQuery);

    /**
     * @Author wql
     * @Description 故障报告信息详情
     * @Date 11:12 2023/11/9
     **/
    Map<String, Object> getReportOverview(Long id);

    /**
     * @Author wql
     * @Description 设备台账统计
     * @Date 11:12 2023/11/9
     **/
    List<DevcieCountVo> getDeviceCount(List<Long> ids);
}
