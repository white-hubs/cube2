package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 故障单2.0公共接口
 *
 * @author lanchun
 * @date 2025/1/3 15:01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicFaultOrderController {

    private final FaultOrderController faultOrderController;

    /**
     * 对象故障单
     *
     * @param param 统计范围
     *
     * @return 隐患单id列表
     */
    @GetMapping("/public/fault/order/statistic")
    public Set<String> objFaultOrderStatistics(DeviceStatisticParam param) {
        return faultOrderController.objFaultOrderStatistics(param);
    }

}
