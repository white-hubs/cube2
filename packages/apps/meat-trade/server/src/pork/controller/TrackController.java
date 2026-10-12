package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Inspector;
import com.guhecloud.cube.biz.meattrade.server.base.service.AbattoirService;
import com.guhecloud.cube.biz.meattrade.server.base.service.InspectorService;
import com.guhecloud.cube.biz.meattrade.server.common.facade.device.service.MeattradeDeviceQueryService;
import com.guhecloud.cube.biz.meattrade.server.common.facade.device.vo.MeattradeDeviceVO;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderDetailService;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.Track;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TrackDetailQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TrackQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PratiqueRegisterService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.TrackService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.TrackBizVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.TrackDetailVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.TrackPageVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.TrackPorkReportVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.TrackSupplierVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 供货商品跟进接口
 */
@Slf4j
@RestController("meattrade-trackController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrackController {

    private final PorkReportService porkReportService;
    private final AbattoirService abattoirService;
    private final PratiqueRegisterService pratiqueRegisterService;
    private final SpotCheckService spotCheckService;
    private final InspectorService inspectorService;
    private final PorkArrangeAuditService porkArrangeAuditService;
    private final PorkArrangeDetailService porkArrangeDetailService;
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final TrackService trackService;
    private final MeattradeDeviceQueryService meattradeDeviceQueryService;

    private final SupplierClient supplierClient;
    private final StallClient stallClient;
    private final FileClient fileClient;
    private final UserClient userClient;

    /**
     * 供货跟进获取商户信息
     *
     * @param code 批次号
     *
     * @return 商户信息
     */
    @GetMapping("/track/supplier/{code}")
    public TrackSupplierVO getTrackMerchant(@NotEmpty(message = "报备号不能为空") @PathVariable("code") String code) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        PorkReport porkReport = this.porkReportService.getByCode(code, tenant);
        SupplierDTO supplier = this.supplierClient.getSupplierById(porkReport.getSupplierId());
        TrackSupplierVO trackSupplierVO = ObjectUtils.copying(supplier, TrackSupplierVO.class);
        trackSupplierVO.setPorkReportCode(code);
        trackSupplierVO.setProductName(porkReport.getGoodsCategoryName());
        return trackSupplierVO;
    }

    /**
     * 供货商来货报备信息
     *
     * @param code 批次号
     *
     * @return 来货报备信息
     */
    @GetMapping("/track/pork-report/{code}")
    public TrackPorkReportVO getTrackPorkReport(@NotEmpty(message = "报备号不能为空") @PathVariable("code") String code) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        PorkReport porkReport = this.porkReportService.getByCode(code, tenant);
        Assert.isNotFound(porkReport, "不存在来货报备信息!");
        TrackPorkReportVO trackPorkReportVO = ObjectUtils.copying(porkReport, TrackPorkReportVO.class);

        StallDTO stall = this.stallClient.getStallById(porkReport.getStallId());
        trackPorkReportVO.setStallCode(Objects.isNull(stall) ? "" : stall.getName());

        Abattoir abattoir = this.abattoirService.getById(porkReport.getAbattoirId());
        trackPorkReportVO.setAbattoirName(Objects.isNull(abattoir) ? "" : abattoir.getName());
        trackPorkReportVO.setAbattoirCode(Objects.isNull(abattoir) ? "" : abattoir.getCode());

        List<FileVO> fileVOS = this.fileClient.getByBusinessKey(porkReport.getId());
        trackPorkReportVO.setFileVOS(fileVOS);

        return trackPorkReportVO;
    }

    /**
     * 供货商品跟进业务流程信息
     *
     * @param code 批次号
     *
     * @return 业务流程信息
     */
    @GetMapping("/track/biz")
    public TrackBizVO getTrackBiz(@NotEmpty(message = "报备号不能为空") String code) {
        TrackBizVO trackBizVO = new TrackBizVO();
        List<Long> userIds = new ArrayList<>();
        Integer tenant = SessionContextHolder.getRequester().getTenant();

        // 报备信息
        PorkReport porkReport = this.porkReportService.getByCode(code, tenant);
        Assert.isNotFound(porkReport, "不存在报备信息!");
        if (Objects.nonNull(porkReport.getAuditorId())) {
            userIds.add(porkReport.getAuditorId());
        }
        trackBizVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
        trackBizVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
        trackBizVO.setFreshLabel(porkReport.getFreshLabel());
        trackBizVO.setPorkReportAuditTime(porkReport.getAuditTime());
        trackBizVO.setPorkReportAuditStatus(porkReport.getStatus());

        // 登记检疫票
        PratiqueRegister pratiqueRegister = this.pratiqueRegisterService.getByPorkReport(porkReport.getId(), tenant);
        if (Objects.nonNull(pratiqueRegister)) {
            userIds.add(Objects.isNull(pratiqueRegister.getRegistrantId()) ? 0 : pratiqueRegister.getRegistrantId());
            trackBizVO.setRegisterTime(pratiqueRegister.getRegisterTime());
            trackBizVO.setRegisterTickets(pratiqueRegister.getPratiqueTickets());
            trackBizVO.setPratiqueCodes(Objects.isNull(pratiqueRegister.getPratiqueCode()) ? Collections.emptyList() :
                                        Arrays.asList(pratiqueRegister.getPratiqueCode().split(",")));
        }

        // 抽检信息
        SpotCheck spotCheck = this.spotCheckService.getByPorkReport(porkReport.getId(), tenant);
        if (Objects.nonNull(spotCheck)) {
            Inspector inspector = this.inspectorService.getById(spotCheck.getInspectorId());
            trackBizVO.setInspectorName(Objects.isNull(inspector) ? "" : inspector.getName());
            trackBizVO.setSpotCheckTime(spotCheck.getSpotCheckTime());
            trackBizVO.setClenbuterol(spotCheck.getClenbuterol());
            trackBizVO.setWatered(spotCheck.getWatered());
        }

        // 理货数据审核信息
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getByPorkReport(porkReport.getId(), porkReport.getTenant());
        if (Objects.nonNull(porkArrangeAudit)) {
            userIds.add(Objects.isNull(porkArrangeAudit.getAuditorId()) ? 0 : porkArrangeAudit.getAuditorId());
            trackBizVO.setPorkArrangeAuditStatus(porkArrangeAudit.getAuditStatus());
            trackBizVO.setPorkArrangeAuditTime(porkArrangeAudit.getAuditTime());
        }

        // 构建用户
        UserQueryParam userQueryParam = UserQueryParam.builder().ids(userIds).build();
        Pagination<UserDTO> users = this.userClient.listUsers(userQueryParam);
        Map<Long, List<UserDTO>> userMap = users.getRecords().stream().collect(Collectors.groupingBy(UserDTO::getId));
        trackBizVO.setPorkReportAuditorName(
                !userMap.containsKey(porkReport.getAuditorId()) ? "" : userMap.get(porkReport.getAuditorId()).get(0).getName());
        trackBizVO.setRegisterName(Objects.isNull(pratiqueRegister) ? "" :
                                   !userMap.containsKey(pratiqueRegister.getRegistrantId()) ? "" : userMap
                                           .get(pratiqueRegister.getRegistrantId()).get(0).getName());
        trackBizVO.setPorkArrangeAuditorName(Objects.isNull(porkArrangeAudit) ? "" :
                                             !userMap.containsKey(porkArrangeAudit.getAuditorId()) ? "" : userMap
                                                     .get(porkArrangeAudit.getAuditorId()).get(0).getName());

        return trackBizVO;
    }

    /**
     * 商品明细查询
     *
     * @param queryParam 查询参数
     *
     * @return 商品明细分页查询
     */
    @GetMapping("/track/details")
    public Pagination<TrackDetailVO> trackDetails(TrackDetailQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取理货详情
        Page<PorkArrangeDetail> page = this.porkArrangeDetailService.pageByTrack(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        Set<Long> porkArrangeDetailIds = page.getRecords().stream().map(PorkArrangeDetail::getId).collect(Collectors.toSet());

        // 获取订单商品详情信息
        List<OrderDetail> orderDetails = this.orderDetailService.listUnCanceledByPorkArrangeDetails(porkArrangeDetailIds, tenant);
        //获取理货设备信息
        Set<Long> deviceIds = page.getRecords().stream().filter(porkArrangeDetail -> StringUtils.isNotEmpty(porkArrangeDetail.getPlace())
                                                                                     && StringUtils.isNumeric(porkArrangeDetail.getPlace()))
                .map(porkArrange -> Long.valueOf(porkArrange.getPlace()))
                .collect(Collectors.toSet());
        Map<Long, MeattradeDeviceVO> simpleInstanceDTOMap = this.meattradeDeviceQueryService.queryDeviceByIds(deviceIds)
                .stream().collect(Collectors.toMap(MeattradeDeviceVO::getId, obj -> obj, (o, n) -> n));
        List<TrackDetailVO> trackDetailVOS = ObjectUtils.copying(page.getRecords(), TrackDetailVO.class);
        if (CollectionUtils.isEmpty(orderDetails)) {
            trackDetailVOS = trackDetailVOS.stream().map(trackDetailVO -> {
                trackDetailVO.setSell(Objects.nonNull(trackDetailVO.getPrice()));
                trackDetailVO.setResult(trackDetailVO.getCheat());
                if (StringUtils.isNotEmpty(trackDetailVO.getPlace()) &&
                    StringUtils.isNumeric(trackDetailVO.getPlace()) &&
                    simpleInstanceDTOMap.containsKey(Long.valueOf(trackDetailVO.getPlace()))) {
                    MeattradeDeviceVO simpleInstanceDTO = simpleInstanceDTOMap.get(Long.valueOf(trackDetailVO.getPlace()));
                    trackDetailVO.setPlace(simpleInstanceDTO.getName());
                } else {
                    trackDetailVO.setPlace(null);
                }
                return trackDetailVO;
            }).collect(Collectors.toList());
            return PagingUtils.convert(page, trackDetailVOS);
        }

        Set<Long> orderIds = orderDetails.stream().map(OrderDetail::getOrderId).collect(Collectors.toSet());
        Map<Long, List<OrderDetail>> orderDetailMap =
                orderDetails.stream().collect(Collectors.groupingBy(OrderDetail::getPorkArrangeDetailId));

        // 获取订单信息
        List<Order> orders = this.orderService.listByIds(orderIds);
        Map<Long, List<Order>> orderMap = orders.stream().collect(Collectors.groupingBy(Order::getId));
        // 获取客户信息
        Set<Long> customerIds = orders.stream().map(Order::getCustomerId).collect(Collectors.toSet());
        List<UserDTO> customers = this.userClient.listUserByIds(customerIds);
        Map<Long, List<UserDTO>> customerMap = customers.stream().collect(Collectors.groupingBy(UserDTO::getId));
        trackDetailVOS = trackDetailVOS.stream().map(trackDetailVO -> {
            //已经售卖的白条 组装交易信息
            if (Objects.nonNull(trackDetailVO.getPrice()) && orderDetailMap.containsKey(trackDetailVO.getId())) {
                OrderDetail orderDetail = orderDetailMap.get(trackDetailVO.getId()).get(0);
                if (orderMap.containsKey(orderDetail.getOrderId())) {
                    Order order = orderMap.get(orderDetail.getOrderId()).get(0);
                    if (customerMap.containsKey(order.getCustomerId())) {
                        UserDTO customer = customerMap.get(order.getCustomerId()).get(0);
                        trackDetailVO.setCustomerName(customer.getName());
                    }
                    trackDetailVO.setOrderTime(order.getOrderTime());
                }
                trackDetailVO.setLoss(orderDetail.getLoss());
                trackDetailVO.setAmount(orderDetail.getPrice());
                trackDetailVO.setCommission(orderDetail.getCommission());
            }
            if (StringUtils.isNotEmpty(trackDetailVO.getPlace()) &&
                StringUtils.isNumeric(trackDetailVO.getPlace()) &&
                simpleInstanceDTOMap.containsKey(Long.valueOf(trackDetailVO.getPlace()))) {
                MeattradeDeviceVO simpleInstanceDTO = simpleInstanceDTOMap.get(Long.valueOf(trackDetailVO.getPlace()));
                trackDetailVO.setPlace(simpleInstanceDTO.getName());
            } else {
                trackDetailVO.setPlace(null);
            }

            trackDetailVO.setSell(Objects.nonNull(trackDetailVO.getPrice()));
            trackDetailVO.setResult(trackDetailVO.getCheat());
            return trackDetailVO;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, trackDetailVOS);
    }

    /**
     * 供货跟进分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 供货跟进信息
     */
    @GetMapping("/track")
    public Pagination<TrackPageVO> page(@Valid TrackQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        //        // 获取商户信息
        List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), tenant);
        Set<Long> supplierIds = suppliers.stream().map(SupplierDTO::getId).collect(Collectors.toSet());
        queryParam.setSupplierIds(supplierIds);

        queryParam.setTenant(tenant);
        Page<Track> page = this.trackService.pageTrack(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 获取来货报备主键/报备信息映射关系
        Set<Long> porkReportIds = page.getRecords().stream().map(Track::getPorkReportId).collect(Collectors.toSet());
        List<PorkReport> porkReports = this.porkReportService.listByIds(porkReportIds);
        Map<Long, List<PorkReport>> porkReportMap = porkReports.stream().collect(Collectors.groupingBy(PorkReport::getId));

        // 获取批发商主键/批发商信息映射关系
        supplierIds = page.getRecords().stream().map(Track::getSupplierId).collect(Collectors.toSet());
        suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取理货审核主键/理货审核信息映射关系
        Set<Long> porkArrangeAuditIds = page.getRecords().stream().map(Track::getPorkArrangeAuditId).collect(Collectors.toSet());
        List<PorkArrangeAudit> porkArrangeAudits = this.porkArrangeAuditService.listByIds(porkArrangeAuditIds);
        Map<Long, List<PorkArrangeAudit>> porkArrangeAuditMap =
                porkArrangeAudits.stream().collect(Collectors.groupingBy(PorkArrangeAudit::getId));

        // 构建信息
        List<TrackPageVO> tracks = ObjectUtils.copying(page.getRecords(), TrackPageVO.class);
        tracks = tracks.stream().map(track -> {
            if (supplierMap.containsKey(track.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(track.getSupplierId()).get(0);
                track.setSupplierName(supplier.getName());
                track.setSupplierCode(supplier.getCode());
            }
            if (porkReportMap.containsKey(track.getPorkReportId())) {
                PorkReport porkReport = porkReportMap.get(track.getPorkReportId()).get(0);
                track.setPorkReportCode(porkReport.getCode());
                track.setGoodsCategoryId(porkReport.getGoodsCategoryId());
                track.setGoodsCategoryName(porkReport.getGoodsCategoryName());
                track.setFreshLabel(porkReport.getFreshLabel());
                track.setPorkReportSlices(porkReport.getReportSlices());
                track.setPorkReportWeight(porkReport.getReportWeight());
                track.setPorkReportStatus(porkReport.getStatus());
            }
            if (porkArrangeAuditMap.containsKey(track.getPorkArrangeAuditId())) {
                PorkArrangeAudit porkArrangeAudit = porkArrangeAuditMap.get(track.getPorkArrangeAuditId()).get(0);
                track.setPorkArrangeAuditStatus(porkArrangeAudit.getAuditStatus());
            }
            return track;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, tracks);
    }

}
