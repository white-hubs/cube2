package com.guhecloud.cube.customization.xialing.server.liangcang.event;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseCable;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseCablePoint;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehousePersonPermission;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureTrendConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureTrendLayerCable;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseTemperatureTrendLayerConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseCablePointService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseCableService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehousePersonPermissionService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendLayerCableService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseTemperatureTrendLayerConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/** 清理粮仓解绑后的附属配置。 */
@Component
@RequiredArgsConstructor
public class WarehouseUnboundCleanupListener {

    private final WarehousePersonPermissionService warehousePersonPermissionService;
    private final WarehouseCableService warehouseCableService;
    private final WarehouseCablePointService warehouseCablePointService;
    private final WarehouseTemperatureTrendConfigService warehouseTemperatureTrendConfigService;
    private final WarehouseTemperatureTrendLayerConfigService warehouseTemperatureTrendLayerConfigService;
    private final WarehouseTemperatureTrendLayerCableService warehouseTemperatureTrendLayerCableService;

    @EventListener
    public void onWarehouseUnbound(WarehouseUnboundEvent event) {
        Long warehouseId = event == null ? null : event.warehouseId();
        if (warehouseId == null) {
            return;
        }
        warehousePersonPermissionService.remove(Wrappers.<WarehousePersonPermission>lambdaQuery()
                .eq(WarehousePersonPermission::getWarehouseId, warehouseId));
        warehouseTemperatureTrendLayerCableService.remove(Wrappers.<WarehouseTemperatureTrendLayerCable>lambdaQuery()
                .eq(WarehouseTemperatureTrendLayerCable::getWarehouseId, warehouseId));
        warehouseTemperatureTrendLayerConfigService.remove(Wrappers.<WarehouseTemperatureTrendLayerConfig>lambdaQuery()
                .eq(WarehouseTemperatureTrendLayerConfig::getWarehouseId, warehouseId));
        warehouseTemperatureTrendConfigService.remove(Wrappers.<WarehouseTemperatureTrendConfig>lambdaQuery()
                .eq(WarehouseTemperatureTrendConfig::getWarehouseId, warehouseId));
        warehouseCablePointService.remove(Wrappers.<WarehouseCablePoint>lambdaQuery()
                .eq(WarehouseCablePoint::getWarehouseId, warehouseId));
        warehouseCableService.remove(Wrappers.<WarehouseCable>lambdaQuery()
                .eq(WarehouseCable::getWarehouseId, warehouseId));
    }
}
