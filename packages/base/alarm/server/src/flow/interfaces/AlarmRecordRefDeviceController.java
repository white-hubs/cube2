package com.guhecloud.cube.base.alarm.server.flow.interfaces;

import com.guhecloud.cube.base.alarm.server.flow.application.AlarmRecordRefDeviceService;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmRecordRefDeviceQueryParam;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 报警记录关联设备接口
 *
 * @author lanchun
 * @date 2025/6/26 11:31
 */
@Slf4j
@RestController
public class AlarmRecordRefDeviceController {

    private final AlarmRecordRefDeviceService alarmRecordRefDeviceService;

    public AlarmRecordRefDeviceController(AlarmRecordRefDeviceService alarmRecordRefDeviceService
                                         ) {this.alarmRecordRefDeviceService = alarmRecordRefDeviceService;}

    /**
     * 获取报警记录关联的设备分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-records/ref/devices")
    public Pagination<DevicePageInfoVO> getAlarmRefDevices(AlarmRecordRefDeviceQueryParam param) {
        return alarmRecordRefDeviceService.getAlarmRefDevices(param);
    }
}
