package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Inspector;
import com.guhecloud.cube.biz.meattrade.server.base.service.AbattoirService;
import com.guhecloud.cube.biz.meattrade.server.base.service.InspectorService;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderDetailService;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckResource;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckResourceQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PratiqueRegisterService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckResourceService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckResourceAppVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckResourcePageVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckResourceVO;
import com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 溯源接口
 */
@Slf4j
@RestController("meattrade-spotCheckResourceController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpotCheckResourceController {

    private final SpotCheckResourceService spotCheckResourceService;
    private final PorkArrangeDetailService porkArrangeDetailService;
    private final PratiqueRegisterService pratiqueRegisterService;
    private final PorkArrangeAuditService porkArrangeAuditService;
    private final PorkReportService porkReportService;
    private final AbattoirService abattoirService;
    private final SpotCheckService spotCheckService;
    private final InspectorService inspectorService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    private final SupplierClient supplierClient;
    private final StallClient stallClient;
    private final FileClient fileClient;
    private final UserClient userClient;

    @GetMapping("/spot-check-resource/{id}")
    public SpotCheckResourceVO getById(@PathVariable("id") Long id) {
        // 获取食安溯源信息
        SpotCheckResource spotCheckResource = this.spotCheckResourceService.getById(id);
        Assert.isNotFound(spotCheckResource, "不存在食安溯源信息");
        SpotCheckResourceVO spotCheckResourceVO = ObjectUtils.copying(spotCheckResource, SpotCheckResourceVO.class);

        Integer tenant = SessionContextHolder.getRequester().getTenant();

        // 批发商信息
        SupplierDTO supplier = this.supplierClient.getSupplierById(spotCheckResource.getSupplierId());
        spotCheckResourceVO.setSupplierName(Objects.isNull(supplier) ? "" : supplier.getName());
        spotCheckResourceVO.setSupplierCode(Objects.isNull(supplier) ? "" : supplier.getCode());

        // 人员集合
        List<Long> userIds = new ArrayList<>();

        // 获取报备信息
        PorkReport porkReport = this.porkReportService.getByCode(spotCheckResource.getPorkReportCode(), tenant);
        Assert.isNotFound(porkReport, "不存在来货报备信息");
        spotCheckResourceVO.setPratiqueCodes(Arrays.asList(porkReport.getPratiqueCode().split(",")));
        spotCheckResourceVO.setOriginalPlace(porkReport.getOriginPlace());
        spotCheckResourceVO.setArrivedTime(porkReport.getArrivedTime());
        spotCheckResourceVO.setVehiclePlate(porkReport.getVehiclePlate());
        spotCheckResourceVO.setPorkReportTime(porkReport.getReportTime());
        spotCheckResourceVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
        spotCheckResourceVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
        spotCheckResourceVO.setFreshLabel(porkReport.getFreshLabel());
        spotCheckResourceVO.setPorkReportAuditTime(porkReport.getAuditTime());
        userIds.add(porkReport.getAuditorId());
        // 获取检疫证明
        spotCheckResourceVO.setFileVOS(this.fileClient.getByBusinessKey(porkReport.getId()));

        // 获取档位信息
        StallDTO stallDTO = this.stallClient.getStallById(porkReport.getStallId());
        spotCheckResourceVO.setStallId(Objects.isNull(stallDTO) ? null : stallDTO.getId());
        spotCheckResourceVO.setStallName(Objects.isNull(stallDTO) ? "" : stallDTO.getName());

        // 获取屠宰场
        Abattoir abattoir = this.abattoirService.getById(porkReport.getAbattoirId());
        spotCheckResourceVO.setAbattoirName(Objects.isNull(abattoir) ? "" : abattoir.getName());

        // 扩展检疫票信息
        PratiqueRegister pratiqueRegister = this.pratiqueRegisterService.getByPorkReport(porkReport.getId(), tenant);
        userIds.add(pratiqueRegister.getRegistrantId());
        spotCheckResourceVO.setRegisterTime(pratiqueRegister.getRegisterTime());

        // 扩展抽检信息
        SpotCheck spotCheck = this.spotCheckService.getByPorkReport(porkReport.getId(), tenant);
        spotCheckResourceVO.setClenbuterol(spotCheck.getClenbuterol());
        spotCheckResourceVO.setWatered(spotCheck.getWatered());
        spotCheckResourceVO.setSpotCheckTime(spotCheck.getSpotCheckTime());

        // 获取抽检员
        Inspector inspector = this.inspectorService.getById(spotCheck.getInspectorId());
        spotCheckResourceVO.setInspectorName(Objects.isNull(inspector) ? "" : inspector.getName());

        // 扩展理货审核信息
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        spotCheckResourceVO.setPorkArrangeAuditTime(porkArrangeAudit.getAuditTime());
        spotCheckResourceVO.setPorkArrangeAuditStatus(porkArrangeAudit.getAuditStatus());
        userIds.add(porkArrangeAudit.getAuditorId());

        // 扩展订单信息
        Order order = this.orderService.getById(spotCheckResource.getOrderId());
        UserDTO customer = this.userClient.getUser(order.getCustomerId());
        spotCheckResourceVO.setCustomerName(Objects.isNull(customer) ? "" : customer.getName());
        spotCheckResourceVO.setOrderTime(order.getOrderTime());

        // 扩展商品详细信息
        PorkArrangeDetail porkArrangeDetail =
                this.porkArrangeDetailService.getById(spotCheckResource.getPorkArrangeDetailId());
        spotCheckResourceVO.setLabel(Objects.isNull(porkArrangeDetail) ? "" : porkArrangeDetail.getLabel());
        spotCheckResourceVO.setFootMarkCode(Objects.isNull(porkArrangeDetail) ? "" :
                                            porkArrangeDetail.getFootMarkCode());

        // 获取用户
        UserQueryParam userQueryParam = UserQueryParam.builder().ids(userIds).pageSize(userIds.size()).build();
        Pagination<UserDTO> users = this.userClient.listUsers(userQueryParam);
        Map<Long, List<UserDTO>> userMap = users.getRecords().stream().collect(Collectors.groupingBy(UserDTO::getId));
        spotCheckResourceVO.setPorkReportAuditorName(!userMap.containsKey(porkReport.getAuditorId()) ?
                                                     "" : userMap.get(porkReport.getAuditorId()).get(0).getName());
        spotCheckResourceVO.setRegisterName(!userMap.containsKey(pratiqueRegister.getRegistrantId()) ?
                                            "" : userMap.get(pratiqueRegister.getRegistrantId()).get(0).getName());
        spotCheckResourceVO.setPorkArrangeAuditorName(!userMap.containsKey(porkArrangeAudit.getAuditorId()) ?
                                                      "" : userMap.get(porkArrangeAudit.getAuditorId()).get(0).getName());

        return spotCheckResourceVO;
    }

    /**
     * 小程序获取食安溯源信息
     *
     * @param id 理货片数编码
     *
     * @return 食安溯源信息
     */
    @GetMapping("/spot-check/resource/mp/{id}")
    public SpotCheckResourceAppVO getPorkArrangeDetailResource(@PathVariable("id") Long id) {
        SpotCheckResource spotCheckResource = this.spotCheckResourceService.getById(id);
        Assert.isNotFound(spotCheckResource, "不存在食安溯源信息!");
        // 获取市场租户编号
        SupplierDTO supplier = this.supplierClient.getSupplierById(spotCheckResource.getSupplierId());
        Assert.isNotFound(supplier, "无效批发商信息!");

        SpotCheckResourceAppVO result = new SpotCheckResourceAppVO();
        List<SpotCheckResourceAppVO.spotCheckResource> resources = new ArrayList<>();

        PorkArrangeDetail porkArrangeDetail = this.porkArrangeDetailService.getById(spotCheckResource.getPorkArrangeDetailId());
        Assert.isNotFound(porkArrangeDetail, "不存在理货片数信息!");
        // 获取商户信息
        result.setPorkReportCode(porkArrangeDetail.getPorkReportCode());
        result.setSupplierId(supplier.getId());
        result.setSupplierCode(supplier.getCode());
        result.setSupplierName(supplier.getName());
        result.setLabel(porkArrangeDetail.getLabel());
        result.setFootMarkCode(porkArrangeDetail.getFootMarkCode());

        SessionContextHolder.requestTenant(supplier.getTenant());

        // 获取报备信息
        PorkReport porkReport = this.porkReportService.getByCode(porkArrangeDetail.getPorkReportCode(), supplier.getTenant());
        if (Objects.nonNull(porkReport)) {
            result.setGoodsCategoryId(porkReport.getGoodsCategoryId());
            result.setGoodsCategoryName(porkReport.getGoodsCategoryName());
            result.setFreshLabel(porkReport.getFreshLabel());
            // 构建上游信息
            this.extendUpStreamInfo(resources, porkReport);
            // 构建进场信息
            this.extendMarchInfo(resources, supplier, porkReport);
        } else {
            return result;
        }
        // 构建检疫票据
        PratiqueRegister pratiqueRegister = this.pratiqueRegisterService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        if (Objects.nonNull(pratiqueRegister)) {
            this.extendPratiqueInfo(resources, pratiqueRegister);
        }
        // 构建抽检信息
        SpotCheck spotCheck = this.spotCheckService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        if (Objects.nonNull(spotCheck)) {
            this.extendSpotCheckInfo(resources, spotCheck);
        }
        // 构建数据审核信息
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        if (Objects.nonNull(porkArrangeAudit)) {
            this.extendPorkArrangeAuditInfo(resources, porkArrangeAudit);
        }
        // 构建交易信息
        OrderDetail orderDetail = this.orderDetailService.getByPorkArrangeDetail(porkArrangeDetail.getId(), supplier.getTenant());
        if (Objects.isNull(orderDetail)) {
            result.setResources(resources);
            return result;
        }
        Order order = this.orderService.getById(orderDetail.getOrderId());
        if (Objects.nonNull(order)) {
            this.extendOrderInfo(resources, porkReport, order);
        }
        result.setResources(resources);
        return result;
    }

    @GetMapping("/spot-check-resource/page")
    public Pagination<SpotCheckResourcePageVO> page(@Valid SpotCheckResourceQueryParam queryParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        // 获取批发商信息
        List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), tenant);
        Set<Long> supplierIds = suppliers.stream().map(SupplierDTO::getId).collect(Collectors.toSet());
        queryParam.setSupplierIds(supplierIds);
        queryParam.setTenant(tenant);

        Page<SpotCheckResourcePageVO> page = this.spotCheckResourceService.pageSCR(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        // 获取批发商主键/批发商信息映射关系
        supplierIds = page.getRecords().stream().map(SpotCheckResourcePageVO::getSupplierId).collect(Collectors.toSet());
        suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));
        // 构建信息
        page.getRecords().forEach(spotCheckResource -> {
            if (supplierMap.containsKey(spotCheckResource.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(spotCheckResource.getSupplierId()).getFirst();
                spotCheckResource.setSupplierName(supplier.getName());
                spotCheckResource.setSupplierCode(supplier.getCode());
            }
        });
        return PagingUtils.convert(page);
    }

    /**
     * 根据理货编码获取溯源信息
     *
     * @param code 理货片数编码
     *
     * @return 食安溯源信息
     */
    @GetMapping("/spot-check/resource/{code}/pork-arrange-detail")
    public SpotCheckResourceAppVO getPorkArrangeDetailResource(@PathVariable("code") String code) {
        // 获取市场租户编号
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);

        SpotCheckResourceAppVO result = new SpotCheckResourceAppVO();
        List<SpotCheckResourceAppVO.spotCheckResource> resources = new ArrayList<>();
        // 获取当天理货片数信息
        Period period = new Period(DateUtils.getMidTime(LocalDateTime.now()), DateUtils.addDays(DateUtils.getMidTime(LocalDateTime.now()), 1));
        PorkArrangeDetail porkArrangeDetail = this.porkArrangeDetailService.getByCode(code, period, supplier.getTenant());
        Assert.isNotFound(porkArrangeDetail, "不存在理货片数信息!");
        // 获取商户信息
        result.setPorkReportCode(porkArrangeDetail.getPorkReportCode());
        result.setWeight(porkArrangeDetail.getWeight());
        result.setSupplierCode(supplier.getCode());
        result.setSupplierName(supplier.getName());
        result.setLabel(porkArrangeDetail.getLabel());
        result.setFootMarkCode(porkArrangeDetail.getFootMarkCode());

        SessionContextHolder.requestTenant(supplier.getTenant());

        // 获取报备信息
        PorkReport porkReport = this.porkReportService.getByCode(porkArrangeDetail.getPorkReportCode(), supplier.getTenant());
        if (Objects.nonNull(porkReport)) {
            result.setGoodsCategoryId(porkReport.getGoodsCategoryId());
            result.setGoodsCategoryName(porkReport.getGoodsCategoryName());
            result.setFreshLabel(porkReport.getFreshLabel());
            // 构建上游信息
            this.extendUpStreamInfo(resources, porkReport);
            // 构建进场信息
            this.extendMarchInfo(resources, supplier, porkReport);
        } else {
            return result;
        }
        // 构建检疫票据
        PratiqueRegister pratiqueRegister = this.pratiqueRegisterService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        if (Objects.nonNull(pratiqueRegister)) {
            this.extendPratiqueInfo(resources, pratiqueRegister);
        }
        // 构建抽检信息
        SpotCheck spotCheck = this.spotCheckService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        if (Objects.nonNull(spotCheck)) {
            this.extendSpotCheckInfo(resources, spotCheck);
        }
        // 构建数据审核信息
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        if (Objects.nonNull(porkArrangeAudit)) {
            this.extendPorkArrangeAuditInfo(resources, porkArrangeAudit);
        }
        // 构建交易信息
        OrderDetail orderDetail = this.orderDetailService.getByPorkArrangeDetail(porkArrangeDetail.getId(), supplier.getTenant());
        if (Objects.isNull(orderDetail)) {
            result.setResources(resources);
            return result;
        }
        Order order = this.orderService.getById(orderDetail.getOrderId());
        if (Objects.nonNull(order)) {
            this.extendOrderInfo(resources, porkReport, order);
        }
        result.setResources(resources);
        return result;
    }

    private void extendOrderInfo(List<SpotCheckResourceAppVO.spotCheckResource> resources, PorkReport porkReport, Order order) {
        SpotCheckResourceAppVO.spotCheckResource resource = new SpotCheckResourceAppVO.spotCheckResource();
        resource.setTitle("交易信息");
        Map<Object, Object> map = new HashMap<>();
        StallDTO stallDTO = this.stallClient.getStallById(porkReport.getStallId());
        map.put("stallName", Objects.isNull(stallDTO) ? "" : stallDTO.getName());
        UserDTO customer = this.userClient.getUser(order.getCustomerId());
        map.put("customerName", Objects.isNull(customer) ? "" : customer.getName());
        map.put("orderTime", order.getOrderTime());
        resource.setValues(map);
        resources.add(resource);
    }

    private void extendPorkArrangeAuditInfo(List<SpotCheckResourceAppVO.spotCheckResource> resources, PorkArrangeAudit porkArrangeAudit) {
        SpotCheckResourceAppVO.spotCheckResource resource = new SpotCheckResourceAppVO.spotCheckResource();
        resource.setTitle("数据审核");
        Map<Object, Object> map = new HashMap<>();
        UserDTO user = this.userClient.getUser(porkArrangeAudit.getAuditorId());
        map.put("porkArrangeAuditorName", Objects.isNull(user) ? "" : user.getName());
        map.put("porkArrangeAuditTime", porkArrangeAudit.getAuditTime());
        map.put("porkArrangeAuditStatus", porkArrangeAudit.getAuditStatus());
        resource.setValues(map);
        resources.add(resource);
    }

    private void extendSpotCheckInfo(List<SpotCheckResourceAppVO.spotCheckResource> resources, SpotCheck spotCheck) {
        SpotCheckResourceAppVO.spotCheckResource resource = new SpotCheckResourceAppVO.spotCheckResource();
        resource.setTitle("抽检信息");
        Map<Object, Object> map = new HashMap<>();
        Inspector inspector = this.inspectorService.getById(spotCheck.getInspectorId());
        map.put("inspectorName", Objects.isNull(inspector) ? "" : inspector.getName());
        map.put("clenbuterol", spotCheck.getClenbuterol());
        map.put("watered", spotCheck.getWatered());
        map.put("spotCheckTime", spotCheck.getSpotCheckTime());
        resource.setValues(map);
        resources.add(resource);
    }

    private void extendPratiqueInfo(List<SpotCheckResourceAppVO.spotCheckResource> resources, PratiqueRegister pratiqueRegister) {
        SpotCheckResourceAppVO.spotCheckResource resource = new SpotCheckResourceAppVO.spotCheckResource();
        resource.setTitle("检疫票据");
        Map<Object, Object> map = new HashMap<>();
        map.put("registerTime", pratiqueRegister.getRegisterTime());
        UserDTO user = this.userClient.getUser(pratiqueRegister.getRegistrantId());
        map.put("registerName", Objects.isNull(user) ? "" : user.getName());
        resource.setValues(map);
        resources.add(resource);
    }

    private void extendMarchInfo(List<SpotCheckResourceAppVO.spotCheckResource> resources, SupplierDTO supplier, PorkReport porkReport) {
        SpotCheckResourceAppVO.spotCheckResource resource = new SpotCheckResourceAppVO.spotCheckResource();
        resource.setTitle("进场信息");
        Map<Object, Object> map = new HashMap<>();
        map.put("arrivedTime", porkReport.getArrivedTime());
        map.put("supplierName", supplier.getName());
        map.put("vehiclePlate", porkReport.getVehiclePlate());
        UserDTO user = this.userClient.getUser(porkReport.getAuditorId());
        map.put("porkReportAuditorName", Objects.isNull(user) ? "" : user.getName());
        map.put("porkReportAuditTime", porkReport.getAuditTime());
        map.put("files", this.fileClient.getByBusinessKey(porkReport.getId()));
        resource.setValues(map);
        resources.add(resource);
    }

    /**
     * 上游信息
     */
    private void extendUpStreamInfo(List<SpotCheckResourceAppVO.spotCheckResource> resources, PorkReport porkReport) {
        SpotCheckResourceAppVO.spotCheckResource resource = new SpotCheckResourceAppVO.spotCheckResource();
        resource.setTitle("上游信息");
        Abattoir abattoir = this.abattoirService.getById(porkReport.getAbattoirId());
        Map<Object, Object> map = new HashMap<>();
        map.put("abattoirName", abattoir.getName());
        map.put("reportTime", porkReport.getReportTime());
        map.put("originPlace", porkReport.getOriginPlace());
        resource.setValues(map);
        resources.add(resource);
    }

}
