package com.guhecloud.cube.biz.emergency.client;

import com.guhecloud.cube.biz.emergency.client.vo.EmergencyFileSafetyReportVO;
import com.guhecloud.platform.base.model.Period;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("emergencyManagement")
public interface EmergencyFileClient {
    /**
     * 安全报告预案文件数据采集
     *
     * @param period
     */
    @GetMapping("/emergency-file/safety-report")
    EmergencyFileSafetyReportVO getEmergencyFileForSafetyReport(@RequestParam(value = "period", required = false) Period period);
}
