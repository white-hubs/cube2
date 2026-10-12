package com.guhecloud.cube.customization.xialing.server.liangcang.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.dao.WarehousePersonPermissionMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehousePersonPermission;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehousePersonPermissionUpdateParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehousePersonPermissionService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehousePersonPermissionListVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.SetUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class WarehousePersonPermissionServiceImpl extends ServiceImpl<WarehousePersonPermissionMapper, WarehousePersonPermission>
        implements WarehousePersonPermissionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBindings(WarehousePersonPermissionUpdateParam param) {
        Assert.isParameterError(param == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Assert.isParameterError(param.getWarehouseId() == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        Long warehouseId = param.getWarehouseId();
        List<Long> incoming = param.getPersonIds();
        List<Long> normalized = ListUtils.distinct(incoming == null ? List.of() : incoming);

        remove(Wrappers.<WarehousePersonPermission>lambdaQuery()
                .eq(WarehousePersonPermission::getWarehouseId, warehouseId));
        if (ListUtils.isEmpty(normalized)) {
            return;
        }
        List<WarehousePersonPermission> rows = ListUtils.convertItem(normalized, personId -> {
            var row = new WarehousePersonPermission();
            row.setId(IdWorker.getId());
            row.setWarehouseId(warehouseId);
            row.setPersonId(personId);
            return row;
        });
        saveBatch(rows);
    }

    @Override
    public WarehousePersonPermissionListVO listByWarehouseId(Long warehouseId) {
        Assert.isParameterError(warehouseId == null, MsgBinding.WAREHOUSE_ID_REQUIRED);
        var rows = list(Wrappers.<WarehousePersonPermission>lambdaQuery()
                .select(WarehousePersonPermission::getPersonId)
                .eq(WarehousePersonPermission::getWarehouseId, warehouseId)
                .orderByAsc(WarehousePersonPermission::getPersonId));
        var vo = new WarehousePersonPermissionListVO();
        vo.setPersonIds(ListUtils.isEmpty(rows) ? List.of()
                : ListUtils.convertItem(rows, WarehousePersonPermission::getPersonId));
        return vo;
    }

    @Override
    public Set<Long> warehouseIdsBoundForPerson(Long personId, Collection<Long> warehouseIds) {
        if (personId == null || ListUtils.isEmpty(warehouseIds)) {
            return Set.of();
        }
        var rows = list(Wrappers.<WarehousePersonPermission>lambdaQuery()
                .select(WarehousePersonPermission::getWarehouseId)
                .eq(WarehousePersonPermission::getPersonId, personId)
                .in(WarehousePersonPermission::getWarehouseId, warehouseIds));
        if (ListUtils.isEmpty(rows)) {
            return Set.of();
        }
        return SetUtils.listToSet(rows, WarehousePersonPermission::getWarehouseId);
    }
}
