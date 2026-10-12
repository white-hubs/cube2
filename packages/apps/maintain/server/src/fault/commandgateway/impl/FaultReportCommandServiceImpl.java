package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultReportCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.ReportCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultReportMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.ReportEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultReport;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultReportCommandServiceImpl extends ServiceImpl<FaultReportMapper, FaultReport> implements FaultReportCommandService {

    @Override
    public void add(ReportCommand reportCommand) {
        String startTime = "";
        String endTime = "";
        String name = "";
        if (ReportEnum.REPORT_DAY.name().equals(reportCommand.getType())) {
            startTime = DateUtils.format(reportCommand.getReportTime(), "yyyy-MM-dd 00:00:00");
            endTime = DateUtils.format(reportCommand.getReportTime(), "yyyy-MM-dd 23:59:00");
            name = "分析日报（" + DateUtils.format(reportCommand.getReportTime(), "yyyy-MM-dd") + ")";
        } else if (ReportEnum.REPORT_WEEK.name().equals(reportCommand.getType())) {
            startTime = DateUtils.format(reportCommand.getReportTime(), "yyyy-MM-dd 00:00:00");
            var offset = DateUtils.offset(reportCommand.getReportTime(), ChronoUnit.DAYS, 7);
            endTime = DateUtils.format(offset, "yyyy-MM-dd 23:59:00");
            name = "分析周报（" + DateUtils.format(reportCommand.getReportTime(), "yyyy-MM-dd") + "~" +
                   DateUtils.format(offset, "yyyy-MM-dd") + ")";
        } else if (ReportEnum.REPORT_MONTH.name().equals(reportCommand.getType())) {
            var beginOfMonth = DateUtils.beginOfMonth(reportCommand.getReportTime());
            var endOfMonth = DateUtils.endOfMonth(reportCommand.getReportTime());
            startTime = DateUtils.format(beginOfMonth, "yyyy-MM-dd 00:00:00");
            endTime = DateUtils.format(endOfMonth, "yyyy-MM-dd 23:59:00");
            name = "分析月报（" + DateUtils.format(beginOfMonth, "yyyy-MM-dd") + "~" + DateUtils.format(endOfMonth, "yyyy-MM-dd") + ")";
        }

        FaultReport faultReport = new FaultReport();
        faultReport.setName(name);
        faultReport.setStartTime(DateUtils.parse(startTime));
        faultReport.setEndTime(DateUtils.parse(endTime));
        faultReport.setType(reportCommand.getType());
        this.save(faultReport);

    }

    @Override
    public void deleteReport(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
