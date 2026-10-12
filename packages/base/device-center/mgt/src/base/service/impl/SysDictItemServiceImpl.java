package com.guhecloud.cube.iot.mgt.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.iot.mgt.base.mapper.SysDictItemMapper;
import com.guhecloud.cube.iot.mgt.base.model.SysDictItem;
import com.guhecloud.cube.iot.mgt.base.service.SysDictItemService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {
    @Resource
    private SysDictItemMapper sysDictItemMapper;

    @Override
    public Long queryDictIdByAdminId(Long adminId) {
        return sysDictItemMapper.queryDictIdByAdminId(adminId);
    }

    @Override
    public List<SysDictItem> queryDictItemByIdAndTenantId(long dictId, Integer tenantId) {
        return sysDictItemMapper.queryDictItemByIdAndTenantId(dictId, tenantId);
    }

    @Override
    public int countNumberUniqueInBrand(Long numberDictId, Long brandItemId, String numberLabel) {
        return sysDictItemMapper.countNumberUniqueInBrand(numberDictId, brandItemId, numberLabel);
    }

    @Override
    public long countBrandUse(Long id) {
        return sysDictItemMapper.countBrandUse(id);
    }

    public List<SysDictItem> queryItemListByParentIdAndDictId(long parentId, long dictId, Integer tenant) {
        return sysDictItemMapper.queryItemListByParentIdAndDictId(parentId, dictId, tenant);
    }
}
