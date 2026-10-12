package com.guhecloud.cube.biz.meattrade.server.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Settlement;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.OrderEnum;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderQueryParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderSettleBatchParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderSettleParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementQueryParam;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.order.service.SettlementService;
import com.guhecloud.cube.biz.meattrade.server.order.vo.SettlementVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 结算接口
 *
 * @author lzy
 * @date 2021/12/9
 */
@Slf4j
@Validated
@RestController("meattrade-settlementController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SettlementController {

    private final SettlementService settlementService;
    private final OrderService orderService;

    private final SupplierClient supplierClient;

    /**
     * 完成结算
     *
     * @param id    结算记录主键
     * @param param 结算更新信息
     */
    @PutMapping("/order/settlement/{id}")
    public void complete(@PathVariable("id") @Min(value = 1, message = "结算记录主键不能小于1") Long id,
                         @Valid @RequestBody OrderSettleParam param
                        ) {
        Settlement settlement = this.settlementService.getById(id);
        Assert.isNotFound(settlement, "结算记录不存在");
        SupplierDTO supplier = this.supplierClient.getSupplierById(settlement.getSupplierId());
        Assert.isNotFound(supplier, "不存在批发商信息!");
        this.settlementService.complete(settlement, supplier, param.getPassword());
    }

    /**
     * 批量处理完成结算
     *
     * @param param 结算更新信息
     */
    @PutMapping("/order/batch/settlement")
    public void batchComplete(@Valid @RequestBody OrderSettleBatchParam param) {
        // 批量结算，查询传入的结算信息，判断每个商户下的结算信息是否存在，将存在的结算信息进行结算
        List<Long> settlementIds = param.getOrderSettles().stream().map(OrderSettleBatchParam.OrderSettle::getSettleId)
                .collect(Collectors.toList());
        List<Settlement> settlements = this.settlementService.listByIds(settlementIds);
        Assert.isNotFound(CollectionUtils.isEmpty(settlements), "结算记录不存在");
        Map<Long, List<Settlement>> settleMap = settlements.stream().collect(Collectors.groupingBy(Settlement::getId));

        // 获取商家信息
        Set<Long> supplierIds = settlements.stream().map(Settlement::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        Map<Long, List<OrderSettleBatchParam.OrderSettle>> supplierSettleMap =
                param.getOrderSettles().stream().collect(Collectors.groupingBy(OrderSettleBatchParam.OrderSettle::getSupplierId));
        // 遍历每个商户的结算信息,并进行结算
        supplierSettleMap.forEach((supplierId, settles) -> {
            if (supplierMap.containsKey(supplierId)) {
                SupplierDTO supplierDTO = supplierMap.get(supplierId).get(0);
                List<Long> settleIds = settles.stream().map(OrderSettleBatchParam.OrderSettle::getSettleId).collect(Collectors.toList());
                List<Settlement> settlementList = settleIds.stream().map(settleId -> {
                    if (settleMap.containsKey(settleId)) {
                        return settleMap.get(settleId).get(0);
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                this.settlementService.batchComplete(settlementList, supplierDTO, param.getPassword());
            }
        });
    }

    /**
     * 结算管理分页
     *
     * @param queryParam 查询参数
     *
     * @return 结算信息
     */
    @GetMapping("/settlement/page")
    public Pagination<SettlementVO> pageSettlement(@Valid SettlementQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        queryParam.setTenant(tenant);
        // 获取结算信息
        Page<Settlement> page = this.settlementService.pageSettlement(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        // 获取批发商信息
        Set<Long> supplierIds = page.getRecords().stream().map(Settlement::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取订单信息
        OrderQueryParam orderQueryParam = new OrderQueryParam();
        orderQueryParam.setTenant(tenant);
        orderQueryParam.setStatus(OrderEnum.FINISHED);
        List<Order> orders = this.orderService.listOrder(orderQueryParam);
        Map<String, List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(Order::getCode));

        // 构建信息
        List<SettlementVO> settles = ObjectUtils.copying(page.getRecords(), SettlementVO.class);
        settles = settles.stream().map(settlement -> {
            if (supplierMap.containsKey(settlement.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(settlement.getSupplierId()).get(0);
                settlement.setSupplierName(supplier.getName());
                settlement.setSupplierCode(supplier.getCode());
            }
            if (orderMap.containsKey(settlement.getOrderCode())) {
                Order order = orderMap.get(settlement.getOrderCode()).get(0);
                settlement.setAmount(order.getAmount());
                settlement.setActualAmount(order.getAmount().subtract(order.getCommission()));
            }
            return settlement;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, settles);
    }

    /**
     * 结算分页查询（小程序）
     *
     * @param queryParam 查询参数
     *
     * @return 结算信息
     */
    @GetMapping("/settlement/mp/page")
    public Pagination<SettlementVO> pageMpSettlement(@Valid SettlementQueryParam queryParam) {
        // 获取市场租户编号
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(SessionContextHolder.getRequester().getTenant());
        Assert.isNotFound(supplier, "批发商信息不能为空!");
        queryParam.setTenant(supplier.getTenant());
        queryParam.setSupplierId(supplier.getId());
        // 获取结算信息
        Page<Settlement> page = this.settlementService.pageSettlement(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 构建信息
        List<SettlementVO> settles = ObjectUtils.copying(page.getRecords(), SettlementVO.class);
        settles = settles.stream().map(settlement -> {
            settlement.setActualAmount(settlement.getAmount().subtract(settlement.getCommission()));
            return settlement;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, settles);
    }

}
