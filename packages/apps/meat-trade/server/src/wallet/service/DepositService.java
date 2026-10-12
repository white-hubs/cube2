package com.guhecloud.cube.biz.meattrade.server.wallet.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierDepositDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Deposit;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.DepositMerchantQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.DepositQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.RechargeParam;
import com.guhecloud.cube.biz.wallet.client.message.RechargeMessage;

import java.util.Collection;
import java.util.List;

/**
 * 账户充值业务操作接口
 *
 * @author Woody
 * @date 2021/9/7
 */
public interface DepositService extends IService<Deposit> {
    /**
     * 商户充值
     *
     * @param supplierId 商户主键
     * @param param      账户充值参数
     *
     * @return 充值记录主键
     */
    Long recharge(Long supplierId, RechargeParam param);

    Long rechargeV2(Long userId, RechargeParam param);

    /**
     * 消费充值消息
     *
     * @param message 消息
     */
    void consumeMessage(RechargeMessage message);

    /**
     * 查询充值商户信息
     *
     * @param supplierIds 商户主键集合
     * @param param       查询参数
     *
     * @return 商户列表分页
     */
    Page<SupplierDepositDTO> listDepositSuppliers(DepositMerchantQueryParam param, Collection<Long> supplierIds);

    /**
     * 查询商户充值记录
     *
     * @param param       查询参数
     * @param supplierIds 商户主键列表
     *
     * @return 商户充值记录列表分页
     */
    Page<SupplierDepositDTO> listSupplierDeposits(DepositQueryParam param, List<Long> supplierIds);
}
