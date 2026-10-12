package com.guhecloud.cube.biz.meattrade.server.wallet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierWithdrawDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Withdraw;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 账户提现数据操作接口
 *
 * @author Woody
 * @date 2021/8/23
 */
@Mapper
@Repository("meattrade_WithdrawMapper")
public interface WithdrawMapper extends BaseMapper<Withdraw> {
    /**
     * 初始化提现记录并返回是否成功
     *
     * @param withdraw 提现记录
     *
     * @return true/false
     */
    boolean initializeWithdraw(Withdraw withdraw);

    /**
     * 通过账户提现申请并返回是否成功
     *
     * @param id       提现记录主键
     * @param operator 操作员
     * @param datetime 审核时间
     *
     * @return true/false
     */
    boolean pass(@Param("id") Long id, @Param("operator") String operator, @Param("datetime") LocalDateTime datetime);

    /**
     * 拒绝账户提现申请并返回是否成功
     *
     * @param id       提现记录主键
     * @param remark   拒绝备注
     * @param operator 操作员
     * @param datetime 审核时间
     */
    boolean reject(@Param("id") Long id, @Param("remark") String remark, @Param("operator") String operator,
                   @Param("datetime") LocalDateTime datetime
                  );

    /**
     * 取消账户提现申请并返回是否成功
     *
     * @param id       提现记录主键
     * @param remark   取消备注
     * @param datetime 取消时间
     */
    boolean cancel(@Param("id") Long id, @Param("remark") String remark, @Param("datetime") LocalDateTime datetime,
                   @Param("tenant") Integer tenant
                  );

    /**
     * 完成账户提现申请并返回是否成功
     *
     * @param id       提现记录主键
     * @param operator 操作员
     * @param datetime 完成时间
     */
    boolean complete(@Param("id") Long id, @Param("operator") String operator, @Param("datetime") LocalDateTime datetime,
                     @Param("tenant") Integer tenant
                    );

    /**
     * 获取商户提现记录
     *
     * @param id 提现记录主键
     *
     * @return 提现记录信息
     */
    SupplierWithdrawDTO getMerchantWithdraw(@Param("id") Long id);

    /**
     * 查询商户提现记录
     *
     * @param page  分页信息
     * @param id    提现记录主键
     * @param param 查询参数
     *
     * @return 商户提现记录分页信息
     */
    Page<SupplierWithdrawDTO> listSupplierWithdraws(Page<?> page, @Param("id") Long id,
                                                    @Param("param") WithdrawQueryParam param,
                                                    @Param("supplierIds") Collection<Long> supplierIds
                                                   );
}
