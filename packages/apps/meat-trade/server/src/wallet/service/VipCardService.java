package com.guhecloud.cube.biz.meattrade.server.wallet.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierVipCardDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.VipCard;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardBindParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.VipCardQueryParam;

import java.util.Collection;
import java.util.List;

/**
 * 会员卡业务操作接口
 *
 * @author Woody
 * @date 2021/8/22
 */
public interface VipCardService extends IService<VipCard> {
    /**
     * 判断文本是否是密文
     *
     * @param text 文本信息
     *
     * @return true/false
     */
    boolean isCiphertext(String text);

    /**
     * 编码会员卡内容
     *
     * @param content 会员卡内容
     *
     * @return 编码信息
     */
    String encode(VipCard.Content content);

    /**
     * 解码会员卡内容
     *
     * @param ciphertext 内容密文
     *
     * @return 会员卡内容
     */
    VipCard.Content decode(String ciphertext);

    /**
     * 绑定商户会员卡
     *
     * @param supplierId 商户主键
     * @param param      绑定参数
     */
    void bind(Long supplierId, VipCardBindParam param);

    /**
     * 解绑会员卡
     *
     * @param id 会员卡主键
     */
    void unbind(Long id);

    /**
     * 获取用户会员卡列表
     *
     * @param supplierIds 商户主键集合
     *
     * @return 会员卡列表
     */
    List<VipCard> getUserCards(Collection<Long> supplierIds);

    /**
     * 查询会员卡列表
     *
     * @param param       查询参数
     * @param supplierIds 商户主键列表
     *
     * @return 会员卡列表分页
     */
    Page<SupplierVipCardDTO> listCards(VipCardQueryParam param, Collection<Long> supplierIds);

    /**
     * 查询会员卡列表
     *
     * @param keyword 关键字
     * @param tenant  市场租户编号
     *
     * @return 会员卡列表
     */
    List<VipCard> listCards(String keyword, Integer tenant);
}
