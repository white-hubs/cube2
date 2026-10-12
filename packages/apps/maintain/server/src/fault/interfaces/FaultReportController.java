package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultReportCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.ReportCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.ReportEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.DevcieCountVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultReportVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.ReportEnumVo;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultReportQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.ReportQuery;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultReportController {
    private final FaultReportCommandService faultReportCommandService;
    private final FaultReportQueryService faultReportQueryService;

    /**
     * @Description 报告类型获取
     * @Author lrj
     */
    @GetMapping("/getReportType")
    public List<ReportEnumVo> getReportType() {
        return ReportEnum.getEnumTypeVo();
    }

    /**
     * @Description 报告添加
     * @Author lrj
     */
    @PostMapping("/addReport")
    public void add(@RequestBody ReportCommand reportCommand) {
        faultReportCommandService.add(reportCommand);
    }

    /**
     * @Description 报告删除
     * @Author lrj
     */
    @DeleteMapping("/deleteReport/{id}")
    public void deleteReport(@PathVariable Long id) {
        faultReportCommandService.deleteReport(id);
    }

    /**
     * @Description 报告分页
     * @Author lrj
     */
    @GetMapping("/getReport")
    public Pagination<FaultReportVo> getReport(ReportQuery reportQuery) {
        return faultReportQueryService.getReport(reportQuery);
    }

    /**
     * @Author wql
     * @Description 故障报告信息详情
     * @Date 11:04 2023/11/9
     **/
    @GetMapping("/getReportOverview/{id}")
    public Map<String, Object> getReportOverview(@PathVariable Long id) {
        return faultReportQueryService.getReportOverview(id);
    }

    /**
     * @Author wql
     * @Description 设备台账统计
     * @Date 11:12 2023/11/9
     **/
    @GetMapping("/getDeviceCount")
    public List<DevcieCountVo> getDeviceCount(@RequestParam("ids") List<Long> ids) {
        return faultReportQueryService.getDeviceCount(ids);
    }

}
