package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.ApplicationRelatedDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.LiangcangWarehousePageQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarBindingDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarBoundPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarDeviceBindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseBoundDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStatisticsParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseUnbindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ApplicationRelatedDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.BoundWarehouseSimpleVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangLocationCardInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehouseListItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarBindingDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarBoundPageVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseBoundDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStatisticsVO;
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

/** 粮仓管理。 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangWarehouseManagementController {

    private final WarehouseDeviceBindingService warehouseDeviceBindingService;

    /**
     * 绑定雷达与业务设备
     *
     * @param param 绑定参数
     */
    @PostMapping("/liangcang/warehouse-management/bind")
    public Boolean bind(@RequestBody RadarDeviceBindParam param) {
        return warehouseDeviceBindingService.bindDevices(param);
    }

    /**
     * 粮仓业务设备解绑
     *
     * @param param 设备解绑参数
     */
    @PostMapping("/liangcang/warehouse-management/unbind")
    public Boolean unbind(@RequestBody WarehouseUnbindParam param) {
        return warehouseDeviceBindingService.unbindDevices(param);
    }

    /**
     * 粮仓绑定状态分页
     *
     * @param param 分页和绑定状态，默认已绑定
     */
    @GetMapping("/liangcang/warehouse-management/warehouse-info/page")
    public Pagination<RadarBoundPageVO> boundWarehouseInfoPage(@Valid @ModelAttribute RadarBoundPageParam param) {
        return warehouseDeviceBindingService.boundWarehouseInfoPage(param);
    }

    /** 已绑定粮仓简要列表 */
    @GetMapping("/liangcang/warehouse-management/bound-warehouse/list")
    public List<BoundWarehouseSimpleVO> boundWarehouseSimpleList() {
        return warehouseDeviceBindingService.boundWarehouseSimpleList();
    }

    /**
     * 粮仓绑定详情
     *
     * @param param 粮仓 id
     */
    @GetMapping("/liangcang/warehouse-management/detail")
    public RadarBindingDetailVO bindingDetail(RadarBindingDetailParam param) {
        return warehouseDeviceBindingService.bindingDetail(param);
    }

    /** 仓库已绑定业务设备分页 */
    @GetMapping("/liangcang/warehouse-management/bound-devices/page")
    public Pagination<WarehouseBoundDeviceVO> boundDevicePage(@Valid @ModelAttribute WarehouseBoundDevicePageParam param) {
        return warehouseDeviceBindingService.boundDevicePage(param);
    }


    /** 仓与雷达列表 */
    @GetMapping("/liangcang/warehouse-management/warehouse/list")
    public List<LiangcangWarehouseListItemVO> warehouseList(LiangcangWarehousePageQueryParam param) {
        return warehouseDeviceBindingService.warehouseList(param);
    }

    /**
     * 货量与周转统计
     *
     * @param param Query：dimension、period；可选 warehouseId、按种类过滤 warehouseCategory
     */
    @GetMapping("/liangcang/warehouse-management/statistics")
    public WarehouseStatisticsVO warehouseStatistics(WarehouseStatisticsParam param) {
        return warehouseDeviceBindingService.warehouseStatistics(param);
    }

    /**
     * 可关联业务设备分页
     *
     * @param param Query：pageNumber、pageSize、productIds、areaIds、deviceName
     */
    @GetMapping("/liangcang/warehouse-management/application-devices/page")
    public Pagination<ApplicationRelatedDeviceVO> applicationRelatedDevicePage(@ModelAttribute ApplicationRelatedDevicePageParam param) {
        return warehouseDeviceBindingService.applicationRelatedDevicePage(param);
    }

}
