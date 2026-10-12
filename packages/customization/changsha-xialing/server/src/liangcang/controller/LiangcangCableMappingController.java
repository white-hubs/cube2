package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableAvailableDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableCreateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableDeleteParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCablePointMappingCreateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCablePointQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCablePointUnbindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseCableQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseCableService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCableAvailableDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCableListItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseCablePointMappingVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 粮仓电缆设备映射。
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangCableMappingController {

    private final WarehouseCableService warehouseCableService;

    /**
     * 仓房下电缆列表。
     */
    @GetMapping("/liangcang/warehouse-management/cable-mapping/cable/list")
    public List<WarehouseCableListItemVO> cableList(@ModelAttribute WarehouseCableQueryParam param) {
        Long warehouseId = param == null ? null : param.getWarehouseId();
        return warehouseCableService.listByWarehouseId(warehouseId);
    }

    /**
     * 电缆点位映射详情。
     */
    @GetMapping("/liangcang/warehouse-management/cable-mapping/point/list")
    public List<WarehouseCablePointMappingVO> pointList(@ModelAttribute WarehouseCablePointQueryParam param) {
        Long cableId = param == null ? null : param.getCableId();
        return warehouseCableService.listPoints(cableId);
    }

    /**
     * 新增电缆并生成全量点位。
     */
    @PostMapping("/liangcang/warehouse-management/cable-mapping/cable/create")
    public Boolean createCable(@RequestBody WarehouseCableCreateParam param) {
        return warehouseCableService.createCable(param);
    }

    /**
     * 删除电缆及其点位映射。
     */
    @PostMapping("/liangcang/warehouse-management/cable-mapping/cable/delete")
    public Boolean deleteCable(@RequestBody WarehouseCableDeleteParam param) {
        return warehouseCableService.deleteCable(param);
    }

    /**
     * 批量新增点位设备映射。
     */
    @PostMapping("/liangcang/warehouse-management/cable-mapping/point/mapping/create")
    public Boolean createPointMappings(@RequestBody WarehouseCablePointMappingCreateParam param) {
        return warehouseCableService.createPointMappings(param);
    }

    /**
     * 批量取消点位设备映射。
     */
    @PostMapping("/liangcang/warehouse-management/cable-mapping/point/unbind")
    public Boolean unbindPoints(@RequestBody WarehouseCablePointUnbindParam param) {
        return warehouseCableService.unbindPoints(param);
    }

    /**
     * 粮仓温度产品设备分页，并标记是否已被电缆点位映射。
     */
    @GetMapping("/liangcang/warehouse-management/cable-mapping/available-devices/page")
    public Pagination<WarehouseCableAvailableDeviceVO> availableDevicePage(
            @Valid @ModelAttribute WarehouseCableAvailableDevicePageParam param
                                                                          ) {
        return warehouseCableService.availableDevicePage(param);
    }
}
