package com.guhecloud.cube.biz.meattrade.server.wallet.controller;

import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.BusinessType;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.MineTransactionQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.SupplierTransactionVO;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.SupplierVO;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.TransactionBusinessVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.wallet.client.TransactionClient;
import com.guhecloud.cube.biz.wallet.client.dto.TransactionDTO;
import com.guhecloud.cube.biz.wallet.client.param.TransactionQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.RegexUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 交易记录接口
 *
 * @author Woody
 * @date 2021/9/14
 */
@Validated
@RestController("meattrade_TransactionController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("DoubleBraceInitialization")
public class TransactionController {
    private final TransactionClient transactionClient;
    private final SupplierClient supplierClient;
    private final UserClient userClient;

    private Pagination<SupplierTransactionVO> listTransactions(TransactionQueryParam param) {
        // 获取钱包交易记录列表分页信息
        Pagination<TransactionDTO> pagination = this.transactionClient.listTransactions(param);
        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return Pagination.empty();
        }

        // 根据交易记录用户主键获取相关的商户信息
        Set<Long> userIds = pagination.getRecords().stream().flatMap(transaction ->
                                                                             Stream.of(transaction.getSourceUserId(), transaction.getTargetUserId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, UserDTO> users = userClient.listUserByIds(userIds).stream()
                .collect(Collectors.toMap(UserDTO::getId, m -> m, (o, n) -> n));

        // 转换交易记录信息
        List<SupplierTransactionVO> transactions = pagination.getRecords().stream().map(record -> {
            BusinessType type = BusinessType.valueOf(record.getBusiness());
            SupplierTransactionVO.Business business = SupplierTransactionVO.Business.builder()
                    .id(record.getBusinessId()).type(type.name()).name(type.getDescription()).build();
            UserDTO source = users.get(record.getSourceUserId());
            UserDTO target = users.get(record.getTargetUserId());
            SupplierVO sourceVO = ObjectUtils.copying(source, SupplierVO.class);
            if (sourceVO == null) {
                sourceVO = new SupplierVO();
                sourceVO.setName("市场方");
            }
            SupplierVO targetVO = ObjectUtils.copying(target, SupplierVO.class);
            if (targetVO == null) {
                targetVO = new SupplierVO();
                targetVO.setName("市场方");
            }
            return SupplierTransactionVO.builder().flow(record.getFlow()).business(business).sourceMerchant(sourceVO)
                    .targetMerchant(targetVO).amount(record.getAmount()).remark(record.getRemark())
                    .createTime(DateUtils.parse(record.getCreateTime().getTime())).build();
        }).collect(Collectors.toList());
        return PagingUtils.convert(pagination, transactions);
    }

    /**
     * 获取账户交易业务类型
     *
     * @return 账户交易业务类型列表
     */
    @GetMapping("/account/transaction/businesses")
    public List<TransactionBusinessVO> listTransactionBusinesses() {
        return Stream.of(BusinessType.values()).map(type -> TransactionBusinessVO.builder().code(type.name())
                .name(type.getDescription()).build()).collect(Collectors.toList());
    }

    /**
     * 获取我的交易记录
     *
     * @param param 查询参数
     *
     * @return 交易记录列表分页
     */
    @GetMapping("/mine/account/transactions")
    public Pagination<SupplierTransactionVO> listMineTransactions(@Valid MineTransactionQueryParam param) {
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(SessionContextHolder.getRequester().getTenant());
        Assert.isNotFound(supplier, "商家不存在，无交易流水");
        Long userId = SecurityContextHolder.getPrincipal().getId();
        TransactionQueryParam query = TransactionQueryParam.builder().userIds(Collections.singleton(userId))
                .flow(param.getFlow()).tenant(supplier.getTenant()).build();
        return this.listTransactions(query);
    }

    /**
     * 获取我的交易记录（采购商）
     *
     * @param param 查询参数
     *
     * @return 交易记录列表分页
     */
    @GetMapping("/mine/purchaser/account/transactions")
    public Pagination<SupplierTransactionVO> listMinePurchaserTransactions(@Valid MineTransactionQueryParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        TransactionQueryParam query = TransactionQueryParam.builder().userIds(Collections.singleton(userId))
                .flow(param.getFlow()).tenant(tenant).build();
        return this.listTransactions(query);
    }

    /**
     * 查询所有交易记录
     *
     * @param param 查询参数
     *
     * @return 交易记录列表分页
     */
    @GetMapping("/supplier/account/transactions")
    public Pagination<SupplierTransactionVO> listTransactions(
            @Valid com.guhecloud.cube.biz.meattrade.server.wallet.param.TransactionQueryParam param
                                                             ) {
        TransactionQueryParam query = ObjectUtils.copying(param, TransactionQueryParam.class);
        if (RegexUtils.isInteger(param.getKeyword())) {
            query.setBusinessId(Long.parseLong(param.getKeyword()));
        } else if (!StringUtils.isEmpty(param.getKeyword())) {
            return Pagination.empty();
        }
        query.setTenant(param.getTenant());
        return this.listTransactions(query);
    }

    /**
     * 查询市场交易记录
     *
     * @param param 查询参数
     *
     * @return 交易记录列表分页
     */
    @GetMapping("/platform/account/transactions")
    public Pagination<SupplierTransactionVO> listPlatformTransactions(
            @Valid com.guhecloud.cube.biz.meattrade.server.wallet.param.TransactionQueryParam param
                                                                     ) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TransactionQueryParam query = ObjectUtils.copying(param, TransactionQueryParam.class);
        Set<Long> userIds = new HashSet<Long>() {{
            add(null);
        }};
        query.setUserIds(userIds);
        query.setTenant(tenant);
        if (RegexUtils.isInteger(param.getKeyword())) {
            query.setBusinessId(Long.parseLong(param.getKeyword()));
        } else if (!StringUtils.isEmpty(param.getKeyword())) {
            return Pagination.empty();
        }
        return this.listTransactions(query);
    }

    /**
     * 查询商户交易记录
     *
     * @param supplierId 商户主键
     * @param param      查询参数
     *
     * @return 交易记录列表分页
     */
    @GetMapping("/supplier/{supplierId}/account/transactions")
    public Pagination<SupplierTransactionVO> listMerchantTransactions(
            @PathVariable("supplierId") @Min(value = 1, message = "商户主键不能小于1") Long supplierId,
            @Valid com.guhecloud.cube.biz.meattrade.server.wallet.param.TransactionQueryParam param
                                                                     ) {
        SupplierDTO supplier = this.supplierClient.getSupplierById(supplierId);
        TransactionQueryParam query = ObjectUtils.copying(param, TransactionQueryParam.class);
        query.setUserIds(Collections.singleton(supplier.getAdminUserId()));
        query.setTenant(param.getTenant());
        if (RegexUtils.isInteger(param.getKeyword())) {
            query.setBusinessId(Long.parseLong(param.getKeyword()));
        }
        return this.listTransactions(query);
    }

    @GetMapping("/supplier/{userId}/account/transactions/V2")
    public Pagination<SupplierTransactionVO> listMerchantTransactionsV2(@PathVariable("userId") Long userId,
                                                                        @Valid
                                                                        com.guhecloud.cube.biz.meattrade.server.wallet.param.TransactionQueryParam param
                                                                       ) {
        TransactionQueryParam query = ObjectUtils.copying(param, TransactionQueryParam.class);
        query.setUserIds(Collections.singleton(userId));
        query.setTenant(param.getTenant());
        if (RegexUtils.isInteger(param.getKeyword())) {
            query.setBusinessId(Long.parseLong(param.getKeyword()));
        }
        return this.listTransactions(query);
    }
}
