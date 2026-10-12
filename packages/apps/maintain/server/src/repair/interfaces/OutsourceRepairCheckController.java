package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.OutsourceWorkOrderService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dao.OutsourceWorkOrderMapper;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/1 15:17
 * @description 委外维修排查
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OutsourceRepairCheckController {

    private final OutsourceWorkOrderService outsourceWorkOrderService;
    private final OutsourceWorkOrderMapper outsourceWorkOrderMapper;

    /**
     * 设备健康度分析-委外维修
     *
     * @param deviceInstanceId 设备id
     * @param period           统计时间范围
     *
     * @return 工单数量
     */
    @GetMapping("/outsource/repair/number")
    public Long getRepairNumberByDeviceId(@RequestParam("deviceInstanceId") Long deviceInstanceId,
                                          @RequestParam("period") Period period
                                         ) {
        return outsourceWorkOrderService.getRepairNumberByDeviceId(deviceInstanceId, period);
    }

    /**
     * 设备委外维修次数统计
     *
     * @param param 查询参数
     *
     * @return 设备维修次数
     */
    @PostMapping("/outsource/device/repair/number")
    public List<DeviceOperationStatisticsDTO> getRepairNumberByDeviceId(@RequestBody DeviceStatisticParam param) {
        if (ObjectUtils.isNotEmpty(param.getDeviceIds())) {
            return outsourceWorkOrderMapper.selectRepairNumberByDeviceId(param);
        }
        return new ArrayList<>();
    }
}
