package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehousePersonPermission;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehousePersonPermissionUpdateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehousePersonPermissionListVO;

import java.util.Collection;
import java.util.Set;

/**
 * 粮仓仓库人员权限绑定
 * 维护智慧仓维度与人员 id 的绑定（按仓整体替换）。
 */
public interface WarehousePersonPermissionService extends IService<WarehousePersonPermission> {

    /**
     * 更新绑定的人员
     *
     * @param param 粮仓 id 与人员 id 列表（整体替换）
     */
    void updateBindings(WarehousePersonPermissionUpdateParam param);

    /**
     * 查询某仓已绑定人员
     *
     * @param warehouseId 智慧仓库粮仓 id
     * @return 人员 id 列表（去重、排序）
     */
    WarehousePersonPermissionListVO listByWarehouseId(Long warehouseId);

    /**
     * 在给定粮仓 id 集合中，返回已与指定人员建立权限绑定的粮仓 id。
     *
     * @param personId       人员 id（通常即登录用户 Principal.id）
     * @param warehouseIds   待校验的粮仓 id 列表
     * @return 已配置的粮仓 id 集合（为空时表示均无绑定）
     */
    Set<Long> warehouseIdsBoundForPerson(Long personId, Collection<Long> warehouseIds);
}
