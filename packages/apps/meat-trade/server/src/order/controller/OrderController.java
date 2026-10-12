package com.guhecloud.cube.biz.meattrade.server.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.client.param.SupplierQueryParam;
import com.guhecloud.cube.biz.meattrade.server.order.dto.OrderCommissionDTO;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Payment;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.OrderEnum;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderCommissionModifyParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderModifyParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderQueryParam;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderDetailService;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.order.service.PaymentService;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderDetailVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderGoodsCategoryVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderPageVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderQrcodeVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderVO;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckResource;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckResourceService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BarcodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 订单接口
 *
 * @author lzy
 * @date 2021/12/8
 */
@Slf4j
@RestController("meattrade-orderController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final OrderDetailService orderDetailService;
    private final PorkArrangeDetailService porkArrangeDetailService;
    private final SpotCheckResourceService spotCheckResourceService;

    private final SupplierClient supplierClient;
    private final UserClient userClient;

    /**
     * 订单新增
     *
     * @param param 订单新增信息
     */
    @PostMapping("/order")
    public Long addOrder(@Valid @RequestBody OrderModifyParam param) {
        return this.orderService.addOrder(param);
    }

    /**
     * 订单取消
     *
     * @param id 订单主键
     */
    @PutMapping("/order/{id}/canceled")
    public void cancelOrder(@PathVariable("id") Long id) {
        this.orderService.cancelOrder(id);
    }

    /**
     * 订单删除
     *
     * @param id 订单主键
     */
    @DeleteMapping("/order/{id}")
    public void removeOrder(@PathVariable("id") Long id) {
        this.orderService.removeOrder(id);
    }

    /**
     * 订单详情
     *
     * @param id 订单主键
     *
     * @return 订单信息
     */
    @GetMapping("/order/{id}")
    public OrderVO getById(@PathVariable("id") Long id) {
        Order order = this.orderService.getById(id);
        Assert.isNotFound(order, "不存在订单信息!");
        OrderVO orderVO = ObjectUtils.copying(order, OrderVO.class);

        // 多线程获取相关信息
        CompletableFuture<SupplierDTO> supplier = CompletableFuture.supplyAsync(() -> {
            return this.supplierClient.getSupplierById(order.getSupplierId());
        });

        CompletableFuture<UserDTO> user = CompletableFuture.supplyAsync(() -> {
            return this.userClient.getUser(order.getCustomerId());
        });

        CompletableFuture<Payment> payment = CompletableFuture.supplyAsync(() -> {
            return this.paymentService.getByOrderId(order.getId(), order.getTenant());
        });

        // join() 阻塞，直到所有的任务执行结束
        CompletableFuture.allOf(supplier, user, payment).join();

        try {
            // 获取批发商信息
            SupplierDTO supplierDTO = supplier.get();
            orderVO.setSupplierName(Objects.isNull(supplierDTO) ? "" : supplierDTO.getName());
            orderVO.setSupplierCode(Objects.isNull(supplierDTO) ? "" : supplierDTO.getCode());
            orderVO.setSupplierPhone(Objects.isNull(supplierDTO) ? "" : supplierDTO.getPhone());

            // 获取客户信息
            UserDTO customer = user.get();
            orderVO.setCustomerName(Objects.isNull(customer) ? "" : customer.getName());
            orderVO.setCustomerPhone(Objects.isNull(customer) ? "" : customer.getMobile());

            // 获取订单支付方式
            Payment p = payment.get();
            orderVO.setWay(Objects.isNull(p) ? null : p.getMode());

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 计算片数
        List<OrderDetail> orderDetails = this.orderDetailService.listByOrderId(order.getId(), order.getTenant());
        orderVO.setTotalSlices(orderDetails.size());

        // 获取订单详情信息
        orderVO.setDetails(this.extendOrderDetails(ObjectUtils.copying(orderDetails, OrderDetail.class), order));
        return orderVO;
    }

    /**
     * 订单查询
     *
     * @param queryParam 查询参数
     *
     * @return 订单信息
     */
    @GetMapping("/order")
    public Pagination<OrderPageVO> pageOrder(@Valid OrderQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        if (StringUtils.isEmpty(queryParam.getKeyword())) {
            queryParam.setKeyword("");
        }
        // 获取客户信息
        List<UserDTO> customers = this.userClient.listUsers(queryParam.getKeyword());
        Set<Long> customerIds = customers.stream().map(UserDTO::getId).collect(Collectors.toSet());
        queryParam.setCustomerIds(customerIds);
        queryParam.setTenant(tenant);

        Page<Order> page = this.orderService.pageOrder(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<OrderPageVO> orders = ObjectUtils.copying(page.getRecords(), OrderPageVO.class);

        // 获取订单支付方式
        Set<Long> orderIds = orders.stream().map(OrderPageVO::getId).collect(Collectors.toSet());
        List<Payment> payments = this.paymentService.listByOrderIds(orderIds, tenant);
        Map<Long, List<Payment>> paymentMap = payments.stream().collect(Collectors.groupingBy(Payment::getOrderId));

        // 获取批发商主键/批发商信息映射关系
        Set<Long> supplierIds = orders.stream().map(OrderPageVO::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取用户列表
        customerIds = orders.stream().map(OrderPageVO::getCustomerId).collect(Collectors.toSet());
        customers = this.userClient.listUserByIds(customerIds);
        Map<Long, List<UserDTO>> customerMap = customers.stream().collect(Collectors.groupingBy(UserDTO::getId));

        orders = orders.stream().map(order -> {
            if (supplierMap.containsKey(order.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(order.getSupplierId()).get(0);
                order.setSupplierName(supplier.getName());
                order.setSupplierCode(supplier.getCode());
                order.setSupplierPhone(supplier.getPhone());
            }
            if (customerMap.containsKey(order.getCustomerId())) {
                UserDTO customer = customerMap.get(order.getCustomerId()).get(0);
                order.setCustomerName(customer.getName());
                order.setCustomerPhone(customer.getMobile());
            }
            if (paymentMap.containsKey(order.getId())) {
                Payment payment = paymentMap.get(order.getId()).get(0);
                order.setWay(payment.getMode());
            }
            return order;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, orders);
    }

    /**
     * 批发商小程序订单分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 订单信息
     */
    @GetMapping("/order/mp-supplier/page")
    public Pagination<OrderPageVO> pageMpSupplierOrder(@Valid OrderQueryParam queryParam) {
        // 获取批发商信息
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");

        queryParam.setSupplierId(supplier.getId());
        queryParam.setTenant(supplier.getTenant());
        Page<Order> page = this.orderService.pageMpSupplierOrder(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        List<OrderPageVO> orders = ObjectUtils.copying(page.getRecords(), OrderPageVO.class);
        // 获取客户信息
        Set<Long> customerIds = orders.stream().map(OrderPageVO::getCustomerId).collect(Collectors.toSet());
        List<UserDTO> users = this.userClient.listUserByIds(customerIds);
        Map<Long, List<UserDTO>> customerMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));

        // 获取订单详情片数
        Set<Long> orderIds = orders.stream().map(OrderPageVO::getId).collect(Collectors.toSet());
        List<OrderGoodsCategoryVO> orderGoodsCategoryVOS = this.orderDetailService.listOrderGoodsCategoryByOrderIds(orderIds, supplier.getTenant());
        Map<Long, List<OrderGoodsCategoryVO>> orderGoodsCategoryMap =
                orderGoodsCategoryVOS.stream().collect(Collectors.groupingBy(OrderGoodsCategoryVO::getOrderId));
        // 构建信息
        orders = orders.stream().map(order -> {
            if (orderGoodsCategoryMap.containsKey(order.getId())) {
                order.setOrderGoodsCategories(orderGoodsCategoryMap.get(order.getId()));
                order.setTotal(orderGoodsCategoryMap.get(order.getId()).stream().mapToInt(OrderGoodsCategoryVO::getTotal).sum());
            }
            if (customerMap.containsKey(order.getCustomerId())) {
                UserDTO customer = customerMap.get(order.getCustomerId()).get(0);
                order.setCustomerId(customer.getId());
                order.setCustomerName(customer.getName());
                order.setCustomerPhone(customer.getMobile());
            }
            order.setSupplierUserId(supplier.getAdminUserId());
            order.setSupplierName(supplier.getName());
            order.setSupplierCode(supplier.getCode());
            order.setSupplierPhone(supplier.getPhone());

            // 卖家待付款展示付款码
            if (OrderEnum.WAITED.equals(order.getStatus())) {
                BufferedImage qrcode = BarcodeUtils.qrcode(String.valueOf(order.getId()), 180, 180);
                order.setQrcode(BarcodeUtils.image2base64(qrcode));
            }
            return order;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, orders);
    }

    /**
     * 采购商订单分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 订单信息
     */
    @GetMapping("/order/mp-purchaser/page")
    public Pagination<OrderPageVO> pageMpPurchaserOrder(@Valid OrderQueryParam queryParam) {
        // 获取采购商信息
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Long userId = SecurityContextHolder.getPrincipal().getId();
        queryParam.setTenant(tenant);
        queryParam.setCustomerId(userId);
        Page<Order> page = this.orderService.pageOrder(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<OrderPageVO> orders = ObjectUtils.copying(page.getRecords(), OrderPageVO.class);
        // 获取客户信息
        Set<Long> customerIds = page.getRecords().stream().map(Order::getCustomerId).collect(Collectors.toSet());
        List<UserDTO> users = this.userClient.listUserByIds(customerIds);
        Map<Long, List<UserDTO>> userMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));

        // 获取订单详情
        Set<Long> orderIds = page.getRecords().stream().map(Order::getId).collect(Collectors.toSet());
        List<OrderGoodsCategoryVO> orderGoodsCategoryVOS = this.orderDetailService.listOrderGoodsCategoryByOrderIds(orderIds, tenant);
        Map<Long, List<OrderGoodsCategoryVO>> orderGoodsCategoryMap =
                orderGoodsCategoryVOS.stream().collect(Collectors.groupingBy(OrderGoodsCategoryVO::getOrderId));
        // 获取批发商信息
        Set<Long> supplierIds = page.getRecords().stream().map(Order::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        orders = orders.stream().map(order -> {
            if (userMap.containsKey(order.getCustomerId())) {
                UserDTO user = userMap.get(order.getCustomerId()).get(0);
                order.setCustomerName(user.getName());
                order.setCustomerPhone(user.getMobile());
            }
            if (supplierMap.containsKey(order.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(order.getSupplierId()).get(0);
                order.setSupplierName(supplier.getName());
                order.setSupplierCode(supplier.getCode());
                order.setSupplierPhone(supplier.getPhone());
                order.setSupplierUserId(supplier.getAdminUserId());
            }
            if (orderGoodsCategoryMap.containsKey(order.getId())) {
                order.setOrderGoodsCategories(orderGoodsCategoryMap.get(order.getId()));
                order.setTotal(orderGoodsCategoryMap.get(order.getId()).stream().mapToInt(OrderGoodsCategoryVO::getTotal).sum());
            }
            return order;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, orders);
    }

    /**
     * 小程序开单获取客户信息
     *
     * @param keyword 关键字
     * @param paging  分页查询
     *
     * @return 客户信息
     */
    @GetMapping("/order/customers")
    public Pagination<com.guhecloud.cube.biz.mdata.client.dto.UserDTO> getCustomers(
            @RequestParam(value = "keyword", defaultValue = "") String keyword, Paging paging) {
        if (StringUtils.isEmpty(keyword)) {
            return Pagination.empty();
        }
        SupplierQueryParam supplierQueryParam = new SupplierQueryParam();
        supplierQueryParam.setKeyword(keyword);
        supplierQueryParam.setPageSize(paging.getPageSize());
        supplierQueryParam.setPageNumber(paging.getPageNumber());
        Pagination<com.guhecloud.cube.biz.mdata.client.dto.UserDTO> page = this.supplierClient.getCustomers(supplierQueryParam);
        return page;
    }

    /**
     * 获取订单付款码
     *
     * @param orderId 订单主键
     *
     * @return 付款码信息
     */
    @GetMapping("/order/qrcode/{orderId}")
    public OrderQrcodeVO getOrderOrcode(@PathVariable("orderId") Long orderId) {
        Order order = this.orderService.getById(orderId);
        Assert.isNotFound(order, "不存在订单信息!");

        BufferedImage qrcode = BarcodeUtils.qrcode(String.valueOf(orderId), 180, 180);
        OrderQrcodeVO orderQrcodeVO = OrderQrcodeVO.builder()
                .price(order.getAmount())
                .type("order")
                .qrcode(BarcodeUtils.image2base64(qrcode))
                .orderId(orderId)
                .build();
        return orderQrcodeVO;
    }

    private List<OrderDetailVO> extendOrderDetails(List<OrderDetail> orderDetails, Order order) {
        if (CollectionUtils.isEmpty(orderDetails)) {
            return Collections.emptyList();
        }
        // 获取理货数据信息
        Set<Long> porkArrangeDetailIds = orderDetails.stream().map(OrderDetail::getPorkArrangeDetailId).collect(Collectors.toSet());
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listByIds(porkArrangeDetailIds);
        Map<Long, List<PorkArrangeDetail>> porkArrangeDetailMap =
                porkArrangeDetails.stream().collect(Collectors.groupingBy(PorkArrangeDetail::getId));

        // 获取食安溯源信息
        List<SpotCheckResource> spotCheckResources = this.spotCheckResourceService.listByOrder(order.getId(), order.getTenant());
        Map<Long, List<SpotCheckResource>> spotCheckResourceMap =
                spotCheckResources.stream().collect(Collectors.groupingBy(SpotCheckResource::getPorkArrangeDetailId));

        List<OrderDetailVO> orderDetailVOS = orderDetails.stream().map(orderDetail -> {
            OrderDetailVO orderDetailVO = ObjectUtils.copying(orderDetail, OrderDetailVO.class);
            if (porkArrangeDetailMap.containsKey(orderDetail.getPorkArrangeDetailId())) {
                PorkArrangeDetail porkArrangeDetail = porkArrangeDetailMap.get(orderDetail.getPorkArrangeDetailId()).get(0);
                orderDetailVO.setLabel(porkArrangeDetail.getLabel());
                orderDetailVO.setPorkReportCode(porkArrangeDetail.getPorkReportCode());
                orderDetailVO.setFootMarkCode(porkArrangeDetail.getFootMarkCode());
            }
            if (spotCheckResourceMap.containsKey(orderDetail.getPorkArrangeDetailId())) {
                SpotCheckResource spotCheckResource = spotCheckResourceMap.get(orderDetail.getPorkArrangeDetailId()).get(0);
                orderDetailVO.setSpotCheckResourceId(spotCheckResource.getId());
            }
            return orderDetailVO;
        }).collect(Collectors.toList());
        return orderDetailVOS;
    }

    /**
     * 计算分佣规则
     *
     * @param queryParam 查询参数
     */
    @PutMapping("/order/calculate/rules")
    public List<OrderCommissionDTO> calculateCommissionRules(@Valid @RequestBody OrderCommissionModifyParam queryParam) {
        return this.orderService.calculateCommissionRules(queryParam);
    }

}
