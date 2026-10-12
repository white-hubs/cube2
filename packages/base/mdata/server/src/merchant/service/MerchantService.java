package com.guhecloud.cube.biz.mdata.server.merchant.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.merchant.dto.MerchantDTO;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.Merchant;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantCreateParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantStatQueryParam;
import com.guhecloud.cube.biz.mdata.server.merchant.param.MerchantUpdateParam;
import com.guhecloud.platform.base.model.Paging;

import java.util.Collection;
import java.util.List;

/**
 * 商户资料接口
 */
public interface MerchantService extends IService<Merchant> {

    /**
     * 商户资料信息新增
     *
     * @param param 商户资料参数
     */
    void save(MerchantCreateParam param);

    /**
     * 商户资料信息编辑
     *
     * @param param 商户资料参数
     */
    void update(MerchantUpdateParam param);

    /**
     * 获取卖家买家商户
     *
     * @param queryParam 关键字
     *
     * @return 商户信息
     */
    Page<Merchant> getMerchantsByBiz(MerchantStatQueryParam queryParam);

    /**
     * 根据商户主键查询商户信息
     *
     * @param id 商户主键
     *
     * @return 商户资料信息
     */
    Merchant getById(Long id);

    /**
     * 根据商户主键集合获取商户信息
     *
     * @param ids 商户主键集合
     *
     * @return 商户资料信息
     */
    List<Merchant> getByIds(List<Long> ids);

    /**
     * 根据用户主键获取商户信息
     *
     * @param userId 用户主键
     *
     * @return 商户信息
     */
    Merchant getByUserId(Long userId);

    /**
     * 根据用户主键获取商户信息集合
     *
     * @param userId 用户主键
     *
     * @return 用户信息列表
     */
    List<Merchant> listByUserId(Long userId);

    /**
     * 获取商户分页信息
     *
     * @param queryParam 商户查询参数
     *
     * @return 商户分页信息
     */
    Page<MerchantDTO> page(MerchantQueryParam queryParam);

    /**
     * 通过商户主键获取商户信息集合
     *
     * @param merchantIds 商户主键集合
     *
     * @return 商户信息集合
     */
    List<Merchant> listMerchantByIds(Collection<Long> merchantIds);

    /**
     * 获取买家商户信息
     *
     * @param keyword 关键字
     * @param paging  分页参数
     *
     * @return 买家商户信息
     */
    Page<Merchant> getBuyer(String keyword, Paging paging);

    /**
     * 根据关键字查找商户信息
     *
     * @param keyword 关键字
     *
     * @return 商户信息列表
     */
    List<Merchant> lookupMerchants(String keyword);

    /**
     * 根据用户主键获取商户信息
     *
     * @param userIds 用户主键集合
     *
     * @return 商户信息列表
     */
    List<Merchant> listUserMerchants(Collection<Long> userIds);

    /**
     * 模糊查询获取商户主键集合
     *
     * @param queryParam 查询参数
     *
     * @return 商户主键ids
     */
    List<Long> getMerchantIds(MerchantQueryParam queryParam);

    /**
     * 理货机获取指定商户
     *
     * @param midType  商户标识类型
     * @param midValue 商户标识值
     *
     * @return 商户信息
     */
    Merchant getByMidType(String midType, String midValue);

    /**
     * 获取有效商户
     *
     * @param id 商户主键
     *
     * @return 商户信息
     */
    Merchant getActiveMerchant(Long id);

    /**
     * 获取用户有效的商户
     *
     * @param userId 用户主键
     *
     * @return 商户信息
     */
    Merchant getUserActiveMerchant(Long userId);

    Page<Merchant> lookupMerchantPage(MerchantQueryParam queryParam);
}
