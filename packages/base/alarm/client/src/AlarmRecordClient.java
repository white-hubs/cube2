package com.guhecloud.cube.base.alarm.client;

import com.guhecloud.cube.base.alarm.client.dto.AlarmDeviceStatusStatisticsPageDTO;
import com.guhecloud.cube.base.alarm.client.dto.AlarmRecordDTO;
import com.guhecloud.cube.base.alarm.client.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.client.param.AlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.client.param.SettingAlarmBusinessSnapshotParam;
import com.guhecloud.cube.base.alarm.client.vo.AlarmRecordPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "alarm")
public interface AlarmRecordClient {

    /**
     * 设置报警业务快照信息
     *
     * @param snapshot
     */
    @PutMapping("/alarm-record/setting/business-snapshot")
    void setAlarmBusinessSnapshot(@RequestBody SettingAlarmBusinessSnapshotParam snapshot);

    /**
     * 报警记录详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/alarm-record/{id}")
    AlarmRecordDTO getAlarmRecord(@PathVariable("id") Long id);

    /**
     * 报警记录详情
     *
     * @param businessKey
     *
     * @return
     */
    @GetMapping("/alarm-record/by-business/{businessKey}")
    AlarmRecordDTO getAlarmRecordByBusinessKey(@PathVariable("businessKey") @NotNull String businessKey);

    /**
     * 报警设备状态分组统计分页列表
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-statistics/device-status-page")
    Pagination<AlarmDeviceStatusStatisticsPageDTO> getDeviceAlarmStatusTotalPage(
            @RequestBody AlarmDeviceStatusTotalQueryParam param
                                                                                );

    /**
     * 报警设备状态分组统计列表
     *
     * @param param 查询条件
     *
     * @return 设备报警状态统计
     */
    @PostMapping("/alarm-statistics/device-status/list")
    List<AlarmDeviceStatusStatisticsPageDTO> getDeviceAlarmStatusTotalList(
            @RequestBody AlarmDeviceStatusTotalQueryParam param
                                                                           );

    /**
     * 报警设备状态分组列表导出
     *
     * @param param
     */
    @PostMapping("/alarm-statistics/device-status/list/export")
    List<AlarmDeviceStatusStatisticsPageDTO> exportDeviceAlarmStatusTotalList(@RequestBody AlarmDeviceStatusTotalQueryParam param
                                                                             );

    @PostMapping("/alarm-records/page")
    Pagination<AlarmRecordPageVO> page(@RequestBody @Valid AlarmRecordQueryParam param);
}
