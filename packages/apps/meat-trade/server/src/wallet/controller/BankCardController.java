package com.guhecloud.cube.biz.meattrade.server.wallet.controller;

import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.BankCardQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.SupplierBankCardVO;
import com.guhecloud.cube.biz.wallet.client.BankClient;
import com.guhecloud.cube.biz.wallet.client.dto.BankCardDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 银行卡接口
 *
 * @author Woody
 * @date 2021/9/14
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BankCardController {
    private final BankClient bankClient;
    private final SupplierClient supplierClient;

    /**
     * 查询银行卡列表
     *
     * @param param 查询参数
     *
     * @return 银行卡列表分页
     */
    @GetMapping("/supplier-bank-cards")
    public Pagination<SupplierBankCardVO> listCards(@Valid BankCardQueryParam param) {
        // 根据关键字查询商户
        Set<Long> userIds = null;
        List<SupplierDTO> suppliers = null;
        if (!StringUtils.isEmpty(param.getKeyword())) {
            suppliers = this.supplierClient.listSuppliers(param.getKeyword(), param.getTenant());
            userIds = suppliers.stream().map(SupplierDTO::getAdminUserId).collect(Collectors.toSet());
        }

        // 结合商户查询银行卡列表
        Pagination<BankCardDTO> pagination =
                this.bankClient.listCards(com.guhecloud.cube.biz.wallet.client.param.BankCardQueryParam.builder()
                                                  .userIds(userIds).keyword(param.getKeyword()).period(param.getPeriod())
                                                  .tenant(param.getTenant())
                                                  .pageNumber(param.getPageNumber())
                                                  .pageSize(param.getPageSize()).build());
        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return Pagination.empty();
        } else if (CollectionUtils.isEmpty(suppliers)) {
            // 根据银行卡用户查询商户列表
            userIds = pagination.getRecords().stream().map(BankCardDTO::getUserId).collect(Collectors.toSet());
            suppliers = this.supplierClient.listByUserIds(userIds, param.getTenant());
        }

        // 构建商户银行卡列表分页信息
        Map<Long, SupplierDTO> userSuppliers = CollectionUtils.isEmpty(suppliers) ? Collections.emptyMap() :
                                               suppliers.stream()
                                                       .collect(Collectors.toMap(SupplierDTO::getAdminUserId, m -> m, (o, n) -> n));
        List<SupplierBankCardVO> cards = pagination.getRecords().stream().map(card -> {
            SupplierDTO supplier = userSuppliers.get(card.getUserId());
            SupplierDTO supplierDTO = ObjectUtils.copying(supplier, SupplierDTO.class);
            if (supplierDTO != null) {
                supplierDTO.setId(supplier.getId());
            }
            return SupplierBankCardVO.builder().id(card.getId()).code(card.getCode()).idcard(card.getIdcard())
                    .bank(card.getBank().getDescription()).owner(card.getOwner()).issuer(card.getIssuer())
                    .createTime(DateUtils.parse(card.getCreateTime().getTime())).supplier(supplierDTO).build();
        }).collect(Collectors.toList());
        return PagingUtils.convert(pagination, cards);
    }
}
