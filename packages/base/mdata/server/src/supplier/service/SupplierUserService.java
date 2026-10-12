package com.guhecloud.cube.biz.mdata.server.supplier.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierUser;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserCreateParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierUserUpdateParam;

import java.io.Serializable;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/14
 */
public interface SupplierUserService extends IService<SupplierUser> {

    /**
     * 商家用户新增
     *
     * @param param 新增参数
     */
    void save(SupplierUserCreateParam param);

    /**
     * 商家用户编辑
     *
     * @param param 编辑参数
     */
    void update(SupplierUserUpdateParam param);

    void remove(Serializable id);

    /**
     * 获取商户用户关联信息
     *
     * @param supplierId 商户主键
     * @param userId     用户主键
     *
     * @return 商户用户关联信息
     */
    List<SupplierUser> listSupplierUser(Long supplierId, Long userId);

    List<SupplierUser> listSupplierUser(Long userId);

    /**
     * 获取商户用户关联信息列表
     *
     * @param queryParam 查询参数
     *
     * @return 商户用户关联信息列表
     */
    Page<SupplierUser> page(SupplierUserQueryParam queryParam);

}
