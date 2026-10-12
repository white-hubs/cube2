package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarBindingDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardChangeLogQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardBoundWarehousePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardConfigDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardPositionProfileSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardQualityInspectionDeleteParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseLocationCardQualityInspectionSaveParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseDeviceBindingService;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseLocationCardConfigService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangLocationCardInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardBoundWarehousePageVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardConfigDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseLocationCardFieldChangeLogVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 货位卡配置。
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiangcangWarehouseLocationCardController {

    private final WarehouseLocationCardConfigService warehouseLocationCardConfigService;
    private final WarehouseDeviceBindingService warehouseDeviceBindingService;

    /**
     * 保存仓位档案。
     */
    @PostMapping("/liangcang/warehouse-location-card/position-profile/save")
    public Boolean saveProfile(@RequestBody WarehouseLocationCardPositionProfileSaveParam param) {
        return warehouseLocationCardConfigService.saveProfile(param);
    }

    /**
     * 货位卡配置详情。
     */
    @PostMapping("/liangcang/warehouse-location-card/config/detail")
    public WarehouseLocationCardConfigDetailVO detail(@RequestBody WarehouseLocationCardConfigDetailParam param) {
        return warehouseLocationCardConfigService.detail(param);
    }

    /**
     * 已绑定粮仓分页。
     */
    @PostMapping("/liangcang/warehouse-location-card/bound-warehouse/page")
    public Pagination<WarehouseLocationCardBoundWarehousePageVO> boundWarehousePage(
            @RequestBody WarehouseLocationCardBoundWarehousePageParam param) {
        return warehouseLocationCardConfigService.boundWarehousePage(param);
    }

    /**
     * 货位卡信息
     */
    @GetMapping("/liangcang/warehouse-management/location-card/info")
    public LiangcangLocationCardInfoVO locationCardInfo(RadarBindingDetailParam param) {
        return warehouseDeviceBindingService.locationCardInfo(param);
    }

    /**
     * 新增质量检测记录。
     */
    @PostMapping("/liangcang/warehouse-location-card/quality-inspection/create")
    public Boolean createQualityInspection(@RequestBody WarehouseLocationCardQualityInspectionSaveParam param) {
        return warehouseLocationCardConfigService.createQualityInspection(param);
    }

    /**
     * 编辑质量检测记录。
     */
    @PostMapping("/liangcang/warehouse-location-card/quality-inspection/update")
    public Boolean updateQualityInspection(@RequestBody WarehouseLocationCardQualityInspectionSaveParam param) {
        return warehouseLocationCardConfigService.updateQualityInspection(param);
    }

    /**
     * 删除质量检测记录。
     */
    @PostMapping("/liangcang/warehouse-location-card/quality-inspection/delete")
    public Boolean deleteQualityInspection(@RequestBody WarehouseLocationCardQualityInspectionDeleteParam param) {
        return warehouseLocationCardConfigService.deleteQualityInspection(param);
    }

    /**
     * 字段变更记录。
     */
    @PostMapping("/liangcang/warehouse-location-card/change-log/list")
    public Pagination<WarehouseLocationCardFieldChangeLogVO> changeLogList(
            @RequestBody WarehouseLocationCardChangeLogQueryParam param) {
        return warehouseLocationCardConfigService.changeLogList(param);
    }

}
