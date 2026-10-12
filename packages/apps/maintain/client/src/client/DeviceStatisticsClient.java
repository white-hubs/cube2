package com.guhecloud.cube.srv.gen.maintain.client;

import com.guhecloud.cube.srv.gen.maintain.dto.DeviceInspectStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.dto.DeviceStatisticParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wql
 * @describe 设备统计接口
 * @date 2023/12/19 10:49
 */
@FeignClient("maintain")
public interface DeviceStatisticsClient {
    /**
     * 设备隐患报修统计
     *
     * @param param 统计参数
     *
     * @return 隐患报修数量
     */
    @PostMapping("/hidden/deviceHiddenNum")
    List<DeviceOperationStatisticsDTO> deviceHiddenNumStatistics(@RequestBody DeviceStatisticParam param);

    /**
     * 设备故障报修次数统计
     *
     * @param param 查询参数
     *
     * @return 报修次数
     */
    @PostMapping("/fault/deviceFaultNum")
    List<DeviceOperationStatisticsDTO> deviceFaultNumStatistics(@RequestBody DeviceStatisticParam param);

    /**
     * 设备巡检次数统计
     *
     * @param param 统计参数
     *
     * @return 巡检次数
     */
    @PostMapping("/inspect-record/deviceInspectNum")
    List<DeviceInspectStatisticsDTO> deviceInspectNumStatistics(@RequestBody DeviceStatisticParam param);

    /**
     * 设备委外维修次数
     *
     * @param param 统计参数
     *
     * @return 维修次数
     */
    @PostMapping("/outsource/device/repair/number")
    List<DeviceOperationStatisticsDTO> getRepairNumberByDeviceId(@RequestBody DeviceStatisticParam param);

    /**
     * 设备维保次数统计
     */
    @PostMapping("/upkeep/deviceMaintenanceNum")
    List<DeviceOperationStatisticsDTO> deviceMaintenanceNumStatistics(@RequestBody DeviceStatisticParam param);

}
