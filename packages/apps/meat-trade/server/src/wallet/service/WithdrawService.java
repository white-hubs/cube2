package com.guhecloud.cube.biz.meattrade.server.wallet.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierWithdrawDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Withdraw;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.MineWithdrawQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawQueryParam;
import com.guhecloud.cube.biz.wallet.client.message.WithdrawMessage;

import java.util.Collection;

/**
 * 账户提现业务操作接口
 *
 * @author Woody
 * @date 2021/8/23
 */
public interface WithdrawService extends IService<Withdraw> {
    /**
     * 获取操作员
     *
     * @return 操作员姓名
     */
    String getOperator();

    /**
     * 用户提现
     *
     * @param userId 用户主键
     * @param param  提现参数
     *
     * @return 提现记录主键
     */
    Long withdraw(Long userId, WithdrawParam param);

    /**
     * 通过账户提现申请
     *
     * @param withdraw 提现记录
     * @param operator 操作员
     */
    void pass(Withdraw withdraw, String operator);

    /**
     * 拒绝账户提现申请
     *
     * @param withdraw 提现记录
     * @param remark   拒绝备注
     * @param operator 操作员
     */
    void reject(Withdraw withdraw, String remark, String operator);

    /**
     * 取消账户提现申请
     *
     * @param withdraw 提现记录
     * @param remark   取消备注
     */
    void cancel(Withdraw withdraw, String remark, Integer tenant);

    /**
     * 完成账户提现申请
     *
     * @param withdraw 提现记录
     * @param operator 操作员
     */
    void complete(Withdraw withdraw, String operator, Integer tenant);

    /**
     * 消费提现消息
     *
     * @param message 消息
     */
    void consumeMessage(WithdrawMessage message);

    /**
     * 获取商户提现信息
     *
     * @param id 提现记录主键
     *
     * @return 商户提现信息
     */
    SupplierWithdrawDTO getSupplierWithdraw(Long id);

    /**
     * 查询商户提现记录
     *
     * @param param       查询参数
     * @param supplierIds 商户主键列表
     *
     * @return 商户提现记录分页信息
     */
    Page<SupplierWithdrawDTO> listSupplierWithdraws(WithdrawQueryParam param, Collection<Long> supplierIds);

    /**
     * 查询用户提现记录
     *
     * @param userId 用户主键
     * @param param  查询参数
     *
     * @return 商户提现记录分页信息
     */
    Page<Withdraw> listUserWithdraws(Long userId, MineWithdrawQueryParam param);
}
