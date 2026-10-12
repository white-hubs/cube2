package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehousePersonPermissionUpdateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehousePersonPermissionService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehousePersonPermissionListVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 粮仓权限配置
 * 维护智慧仓与人员的绑定关系。
 *
 * @module 长沙霞凝-粮舱
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WarehousePermissionController {

    private final WarehousePersonPermissionService warehousePersonPermissionService;

    /**
     * 更新粮仓绑定人员
     * 以请求体中的列表为准整体替换该仓下已有绑定；personIds 为空则表示清空。
     *
     * @param param JSON：warehouseId、personIds
     */
    @PostMapping("/liangcang/warehouse-permission/bindings")
    public void updateBindings(@Valid @RequestBody WarehousePersonPermissionUpdateParam param) {
        warehousePersonPermissionService.updateBindings(param);
    }

    /**
     * 查询粮仓绑定人员列表
     *
     * @param warehouseId 智慧仓库粮仓 id
     * @return personIds 已绑定人员 id（有序）
     */
    @GetMapping("/liangcang/warehouse-permission/bindings")
    public WarehousePersonPermissionListVO listBindings(@RequestParam @NotNull Long warehouseId) {
        return warehousePersonPermissionService.listByWarehouseId(warehouseId);
    }
}
