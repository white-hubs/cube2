package com.guhecloud.cube.iot.mgt.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;

import java.util.List;

public interface SysDictItemService extends IService<SysDictItem> {


    Long queryDictIdByAdminId(Long id);

    List<SysDictItem> queryDictItemByIdAndTenantId(long dictId, Integer tenantId);

    int countNumberUniqueInBrand(Long numberDictId, Long brandItemId, String numberLabel);

    long countBrandUse(Long id);
}
