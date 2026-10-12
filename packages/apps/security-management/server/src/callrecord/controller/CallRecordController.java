package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.param.CallRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.service.CallRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.BedFileVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.CallRecordVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.ProcessingRecordsVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CallRecordController {

    private final CallRecordService callRecordService;

    /**
     * @Description 呼叫统计
     * @Author lrj
     */
    @GetMapping("/call-statistics")
    public Map<String, Object> getCallStatistics() {
        return callRecordService.getCallStatistics();
    }

    /**
     * @Description 呼叫趋势
     * @Author lrj
     */
    @GetMapping("/call-trends")
    public Map<String, Object> getCallTrends(CallRecordParam param) {
        return callRecordService.getCallTrends(param);
    }

    /**
     * @Description 呼叫记录
     * @Author lrj
     */
    @GetMapping("/call-records-page")
    public Pagination<CallRecordVo> getCallRecordsPage(CallRecordParam param) {
        return callRecordService.getCallRecordsPage(param);
    }

    /**
     * @Description 处理记录
     * @Author lrj
     */
    @GetMapping("/processing-records-page")
    public Pagination<ProcessingRecordsVo> getProcessingRecordsPage(CallRecordParam param) {
        return callRecordService.getProcessingRecordsPage(param);
    }

    /**
     * @Description 床位档案
     * @Author lrj
     */
    @GetMapping("/bed-file-page")
    public Pagination<BedFileVo> getBedFilePage(CallRecordParam param) {
        return callRecordService.getBedFilePage(param);
    }

}
