package com.guhecloud.cube.biz.mdata.server.supplier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierStall;

import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/16
 */
public interface SupplierStallService extends IService<SupplierStall> {

    List<SupplierStall> listBySupplier(Long supplierId);

    List<SupplierStall> listByStalls(Collection<Long> stallIds);

}
