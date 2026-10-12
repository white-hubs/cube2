package com.guhecloud.cube.srv.prepaid.server.controller;

import com.guhecloud.cube.srv.prepaid.server.param.WaterMeterPageParam;
import com.guhecloud.cube.srv.prepaid.server.result.WaterMeterPageResult;
import com.guhecloud.cube.srv.prepaid.server.service.WaterMeterService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 水表
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WaterMeterController {

    private final WaterMeterService waterMeterService;

    /**
     * 水表分页列表
     *
     * @param param 条件入参对象
     *
     * @return 分页列表
     */
    @GetMapping("/waterMeter/page")
    public Pagination<WaterMeterPageResult> getWaterMeterPage(WaterMeterPageParam param) {
        return waterMeterService.getWaterMeterPage(param);
    }
}
