package com.guhecloud.cube.biz.mdata.server.supplier.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.supplier.dao.SupplierStallMapper;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierStall;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierStallService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierStallServiceImpl extends ServiceImpl<SupplierStallMapper, SupplierStall> implements SupplierStallService {
    @Override
    public List<SupplierStall> listBySupplier(@NonNull Long supplierId) {
        return this.lambdaQuery().eq(SupplierStall::getSupplierId, supplierId).list();
    }

    @Override
    public List<SupplierStall> listByStalls(Collection<Long> stallIds) {
        return CollectionUtils.isEmpty(stallIds) ? Collections.emptyList() :
               this.lambdaQuery().in(SupplierStall::getStallId, stallIds).list();
    }

}
