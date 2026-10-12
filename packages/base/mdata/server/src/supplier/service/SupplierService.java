package com.guhecloud.cube.biz.mdata.server.supplier.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierModifyMpParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierModifyParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierPageVO;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierVO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/3
 */
public interface SupplierService extends IService<Supplier> {

    Long addSupplier(SupplierModifyParam param);

    void importSupplierTemplate(List<SupplierModifyParam> param);

    Long addSupplierAndAuthentication(SupplierModifyParam param);

    Supplier addSupplierByMp(SupplierModifyMpParam param);

    void updateSupplierAdminUser(Long adminUserId, String adminUserName);

    void updateSupplier(Long id, SupplierModifyParam param);

    void updateInnerSupplier(Long id, SupplierModifyParam param);

    Supplier getById(Serializable id);

    Supplier getByName(Serializable name);

    Supplier getByAdminPhone(String adminPhone);

    Supplier getBySupplierTenant(Integer tenant);

    Page<Supplier> pageSuppliers(SupplierQueryParam queryParam);

    Page<Supplier> pageSupplier(SupplierQueryParam queryParam);

    Page<UserDTO> getCustomers(SupplierQueryParam queryParam);

    /**
     * 查询供应商和采购商
     */
    Page<SupplierVO> getCustomersV2(SupplierQueryParam queryParam);

    List<SupplierVO> listCustomersV2(SupplierQueryParam queryParam);

    List<Supplier> listMySupplierTenants(Long adminUserId);

    List<Supplier> listSuppliersTenants();

    List<Supplier> listSuppliersByUserAdminId(Long userAdminId, Integer tenant);

    List<Supplier> listSupplier(String keyword, Integer tenant);

    List<Supplier> listOutSupplier(String keyword, Integer tenant);

    List<Supplier> listByUserIds(Collection<? extends Serializable> userIds, Integer tenant);

    List<Supplier> listByIds(Collection<? extends Serializable> ids);

    List<Supplier> getByMidType(String midType, String midValue, Integer tenant);

    /**
     * 分页查询供应商认证信息
     *
     * @param param
     *
     * @return
     */
    Page<SupplierPageVO> pageSupplierDetail(SupplierQueryParam param);

}
