package com.guhecloud.cube.biz.mdata.server.merchant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantUser;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUserUpdateParam;

import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/10/11
 */
public interface MerchantUserService extends IService<MerchantUser> {

    /**
     * 商家用户新增
     *
     * @param param 新增参数
     */
    void save(MerchantUserCreateParam param);

    /**
     * 商家用户编辑
     *
     * @param param 编辑参数
     */
    void update(MerchantUserUpdateParam param);

    /**
     * 获取商户用户关联信息
     *
     * @param merchantId 商户主键
     * @param userId     用户主键
     *
     * @return 商户用户关联信息
     */
    MerchantUser getMerchantUser(Long merchantId, Long userId);

    /**
     * 获取商户用户关联信息列表
     *
     * @param userId 用户主键
     *
     * @return 商户用户关联信息列表
     */
    List<MerchantUser> listMerchantUsers(Long userId);

    /**
     * 获取商户用户关联信息列表
     *
     * @param userIds 用户主键集合
     *
     * @return 商户用户关联信息列表
     */
    List<MerchantUser> listMerchantUsersByUserIds(Collection<Long> userIds);

    /**
     * 获取商户用户关联信息列表
     *
     * @param merchantIds 商户主键
     *
     * @return 商户用户关联信息列表
     */
    List<MerchantUser> listMerchantUsers(List<Long> merchantIds);

    /**
     * 获取商户用户关联信息列表
     *
     * @param queryParam 查询参数
     *
     * @return 商户用户关联信息列表
     */
    Page<MerchantUser> page(MerchantUserQueryParam queryParam);

}
