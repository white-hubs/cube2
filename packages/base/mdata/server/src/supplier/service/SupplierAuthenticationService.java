package com.guhecloud.cube.biz.mdata.server.supplier.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierAuthentication;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierAuthenticatedEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierAuthenticationModifyParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierAuthenticationQueryParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/4
 */
public interface SupplierAuthenticationService extends IService<SupplierAuthentication> {

    void addSupplierAuthentication(SupplierAuthenticationModifyParam param);

    void auditSupplierAuthentication(Long id, SupplierAuthenticatedEnum status, String remark);

    void resetSupplierAuthentication(SupplierAuthentication supplierAuthentication);

    SupplierAuthentication getById(Serializable id);

    SupplierAuthentication getActiveBySupplierId(Serializable supplierId, Integer tenant);

    List<SupplierAuthentication> getLastBySupplierId(Serializable supplierId, Integer tenant);

    Page<SupplierAuthentication> pageSupplierAuthentication(SupplierAuthenticationQueryParam queryParam);

    List<SupplierAuthentication> listBySupplierIds(Collection<? extends Serializable> supplierIds, Integer tenant);

    List<SupplierAuthentication> listSupplierAuthentication(SupplierAuthenticationQueryParam queryParam, Integer tenant);
}
