package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.ReportCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultReport;

public interface FaultReportCommandService extends IService<FaultReport> {
    void add(ReportCommand reportCommand);

    void deleteReport(Long id);
}
