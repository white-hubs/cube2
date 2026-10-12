package com.guhecloud.cube.biz.meattrade.server.wallet.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierDepositDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Deposit;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.DepositMerchantQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.DepositQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 账户充值数据操作接口
 *
 * @author Woody
 * @date 2021/9/7
 */
@Mapper
@Repository("meattrade_DepositMapper")
public interface DepositMapper extends BaseMapper<Deposit> {
    /**
     * 初始化充值记录并返回是否成功
     *
     * @param deposit 充值记录
     *
     * @return true/false
     */
    boolean initializeDeposit(Deposit deposit);

    /**
     * 完成账户充值并返回是否成功
     *
     * @param id       充值记录主键
     * @param datetime 完成时间
     *
     * @return true/false
     */
    boolean complete(@Param("id") Long id, @Param("datetime") LocalDateTime datetime);

    /**
     * 查询充值商户信息
     *
     * @param page  分页信息
     * @param param 查询参数
     *
     * @return 商户列表分页
     */
    Page<SupplierDepositDTO> listDepositSuppliers(Page<?> page, @Param("param") DepositMerchantQueryParam param,
                                                  @Param("supplierIds") Collection<Long> supplierIds
                                                 );

    /**
     * 查询商户充值信息
     *
     * @param page  分页信息
     * @param id    充值记录主键
     * @param param 查询参数
     *
     * @return 商户列表分页
     */
    Page<SupplierDepositDTO> listSupplierDeposits(Page<?> page, @Param("id") Long id,
                                                  @Param("param") DepositQueryParam param,
                                                  @Param("supplierIds") List<Long> supplierIds
                                                 );
}
