package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureTrendConfigDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseTemperatureTrendConfigSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseTemperatureTrendConfigVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 仓库粮温趋势配置。 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangTemperatureTrendConfigController {

    private final WarehouseTemperatureTrendConfigService warehouseTemperatureTrendConfigService;

    /** 查询仓库粮温趋势配置详情。 */
    @GetMapping("/liangcang/warehouse-management/temperature-trend-config/detail")
    public WarehouseTemperatureTrendConfigVO detail(@Valid @ModelAttribute WarehouseTemperatureTrendConfigDetailParam param) {
        Long warehouseId = param == null ? null : param.getWarehouseId();
        return warehouseTemperatureTrendConfigService.detail(warehouseId);
    }

    /** 保存仓库粮温趋势配置。 */
    @PostMapping("/liangcang/warehouse-management/temperature-trend-config/save")
    public Boolean save(@RequestBody WarehouseTemperatureTrendConfigSaveParam param) {
        return warehouseTemperatureTrendConfigService.saveConfig(param);
    }
}
