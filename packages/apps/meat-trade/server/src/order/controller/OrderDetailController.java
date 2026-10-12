package com.guhecloud.cube.biz.meattrade.server.order.controller;

import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderDetailService;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderDetailVO;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 订单详情接口
 *
 * @author lzy
 * @date 2021/12/10
 */
@Slf4j
@RestController("meattrade-orderDetailController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final PorkArrangeDetailService porkArrangeDetailService;

    /**
     * 根据订单主键获取订单详情信息
     *
     * @param orderId 订单主键
     *
     * @return 订单详情信息
     */
    @GetMapping("/order-detail/{orderId}/order")
    public List<OrderDetailVO> listByOrder(@PathVariable("orderId") Long orderId) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<OrderDetail> orderDetails = this.orderDetailService.listByOrderId(orderId, tenant);
        if (CollectionUtils.isEmpty(orderDetails)) {
            return Collections.emptyList();
        }

        // 获取理货片数信息
        Set<Long> porkArrangeDetailIds = orderDetails.stream().map(OrderDetail::getPorkArrangeDetailId).collect(Collectors.toSet());
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listByIds(porkArrangeDetailIds);
        Map<Long, List<PorkArrangeDetail>> porkArrangeDetailMap =
                porkArrangeDetails.stream().collect(Collectors.groupingBy(PorkArrangeDetail::getId));

        // 构建信息
        List<OrderDetailVO> orderDetailVOS = ObjectUtils.copying(orderDetails, OrderDetailVO.class);
        orderDetailVOS = orderDetailVOS.stream().map(orderDetail -> {
            if (porkArrangeDetailMap.containsKey(orderDetail.getPorkArrangeDetailId())) {
                PorkArrangeDetail porkArrangeDetail = porkArrangeDetailMap.get(orderDetail.getPorkArrangeDetailId()).get(0);
                orderDetail.setPorkReportCode(porkArrangeDetail.getPorkReportCode());
                orderDetail.setLabel(porkArrangeDetail.getLabel());
                orderDetail.setFootMarkCode(porkArrangeDetail.getFootMarkCode());
            }
            return orderDetail;
        }).collect(Collectors.toList());

        return orderDetailVOS;
    }

}
