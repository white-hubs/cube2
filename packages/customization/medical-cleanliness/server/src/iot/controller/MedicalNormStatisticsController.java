package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalIotRealTimeDataService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalOverrunRecordService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalRecordStatisticsVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.StatusStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备监测记录统计
 *
 * @Author: lijie
 * @CreateTime: 2024-09-19  13:57
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalNormStatisticsController {
    private final MedicalOverrunRecordService medicalOverrunRecordService;
    private final MedicalIotRealTimeDataService medicalIotRealTimeDataService;

    /**
     * 设备历史监测记录统计
     */
    @GetMapping("/medical/device/Statistics")
    public Pagination<MedicalRecordStatisticsVO> medicalDeviceStatistics() {
        return medicalOverrunRecordService.medicalDeviceStatistics();
    }
    /**
     * 实时数据状态统计
     */
    @GetMapping("/status/Statistics")
    public StatusStatisticsVO statusStatistics() {
        return medicalIotRealTimeDataService.statusStatistics();
    }
}
