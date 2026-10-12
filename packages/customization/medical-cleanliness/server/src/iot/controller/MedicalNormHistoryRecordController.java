package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalDeviceHistoryStatisticsParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalOverrunAlarmRecordParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalOverrunRecordService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalDeviceHistoryStatisticsVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalOverrunAlarmRecordVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.param.MedicalDeleteRecordParam;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备监测记录
 *
 * @Author: lijie
 * @CreateTime: 2024-09-10  09:43
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalNormHistoryRecordController {
    private final MedicalOverrunRecordService medicalOverrunRecordService;

    /**
     * 设备历史监测记录分页
     */
    @PostMapping("/medical/overrun/alarm/page")
    public Pagination<MedicalOverrunAlarmRecordVO> medicalOverrunAlarmPage(@RequestBody MedicalOverrunAlarmRecordParam param) {
        return medicalOverrunRecordService.medicalOverrunAlarmPage(param);
    }

    /**
     * 批量删除设备历史监测记录
     */
    @PostMapping("/medical/deleteRecord")
    public Boolean deleteRecord(@RequestBody MedicalDeleteRecordParam param) {
        return medicalOverrunRecordService.deleteRecord(param.getIds());
    }

    /**
     * 设备历史监测记录状态统计
     */
    @GetMapping("/medical/device/history/statistics")
    public Pagination<MedicalDeviceHistoryStatisticsVO> deviceHistoryStatistics(MedicalDeviceHistoryStatisticsParam param) {
        return medicalOverrunRecordService.deviceHistoryStatistics(param);
    }
}
