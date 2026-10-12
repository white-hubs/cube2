package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAuditLog;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeAuditQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditLogService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PratiqueRegisterService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeAuditLogVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeAuditVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 理货数据审核接口
 */
@Slf4j
@RestController("meattrade-porkArrangeAuditController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangeAuditController {

    private final PorkReportService porkReportService;
    private final PorkArrangeAuditService porkArrangeAuditService;
    private final PratiqueRegisterService pratiqueRegisterService;
    private final SpotCheckService spotCheckService;
    private final PorkArrangeService porkArrangeService;
    private final AbattoirService abattoirService;
    private final InspectorService inspectorService;
    private final PorkArrangeAuditLogService porkArrangeAuditLogService;
    private final MeattradeDeviceQueryService meattradeDeviceQueryService;

    private final SupplierClient supplierClient;
    private final StallClient stallClient;
    private final UserClient userClient;

    /**
     * 理货数据审核
     *
     * @param id     理货数据审核主键
     * @param status 理货数据审核状态
     * @param remark 不通过原因
     */
    @PutMapping("/pork-arrange-audit/{id}/audit")
    public void audit(@PathVariable("id") Long id,
                      @RequestParam("status") PorkArrangeAuditEnum status,
                      @RequestParam(value = "remark", defaultValue = "") String remark
                     ) {
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getById(id);
        Assert.isNotFound(porkArrangeAudit, "不存在理货数据审核信息!");

        // 获取理货信息
        PorkArrange porkArrange = this.porkArrangeService.getById(porkArrangeAudit.getPorkArrangeId());
        Assert.isNotFound(porkArrange, "不存在理货数据信息!");
        Assert.isNotAcceptable(porkArrange.getStatus() != PorkArrangeEnum.FINISHED, "当前理货状态不是为理货完成!");

        // 获取食安抽检信息
        SpotCheck spotCheck = this.spotCheckService.getById(porkArrangeAudit.getSpotCheckId());
        Assert.isNotFound(spotCheck, "不存在食安抽检信息!");
        Assert.isNotAcceptable(spotCheck.getClenbuterol() == SpotCheckEnum.UNSELECTED, "瘦肉精还未检测完成!");
        Assert.isNotAcceptable(spotCheck.getWatered() == SpotCheckEnum.UNSELECTED, "含水量还未检测完成!");

        // 获取检疫票信息
        PratiqueRegister pratiqueRegister = this.pratiqueRegisterService.getById(porkArrangeAudit.getPratiqueRegisterId());
        Assert.isNotFound(pratiqueRegister, "不存在检疫票信息!");
        Assert.isNotAcceptable(Objects.isNull(pratiqueRegister.getRegistrantId()), "检疫票信息还未登记!");

        // 进行审核
        this.porkArrangeAuditService.auditPorkArrangeAudit(porkArrangeAudit, status, remark);
    }

    /**
     * 理货数据审核撤销
     *
     * @param id 理货数据审核主键
     */
    @PutMapping("/pork-arrange-audit/{id}/rescind")
    public void rescind(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getById(id);
        Assert.isNotFound(porkArrangeAudit, "不存在理货数据审核信息!");
        Assert.isNotAcceptable(PorkArrangeAuditEnum.PASSED == porkArrangeAudit.getAuditStatus(), "理货数据审核已通过,无法撤销!");
        Assert.isNotAcceptable(PorkArrangeAuditEnum.FAILED != porkArrangeAudit.getAuditStatus(), "理货审核状态不是未通过,无法撤销!");

        this.porkArrangeAuditService.rescindPorkArrangeAudit(porkArrangeAudit);
    }

    /**
     * 理货数据审核详情
     *
     * @param id 理货数据审核主键
     *
     * @return 理货数据审核信息
     */
    @GetMapping("/pork-arrange-audit/{id}")
    public PorkArrangeAuditVO getById(@NotNull(message = "主键不能空") @PathVariable("id") Long id) {
        // 获取理货审核信息
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getById(id);
        Assert.isNotFound(porkArrangeAudit, "不存在理货审核信息!");
        PorkArrangeAuditVO porkArrangeAuditVO = ObjectUtils.copying(porkArrangeAudit, PorkArrangeAuditVO.class);

        // 获取来货报备信息
        PorkReport porkReport = this.porkReportService.getById(porkArrangeAudit.getPorkReportId());
        if (Objects.nonNull(porkReport)) {
            porkArrangeAuditVO.setPorkReportCode(porkReport.getCode());
            porkArrangeAuditVO.setPorkReportSlices(porkReport.getReportSlices());
            porkArrangeAuditVO.setPorkReportWeight(porkReport.getReportWeight());
            porkArrangeAuditVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
            porkArrangeAuditVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
            porkArrangeAuditVO.setFreshLabel(porkReport.getFreshLabel());
            porkArrangeAuditVO.setOriginPlace(porkReport.getOriginPlace());

            // 获取屠宰场信息
            Abattoir abattoir = this.abattoirService.getById(porkReport.getAbattoirId());
            porkArrangeAuditVO.setAbattoirName(Objects.isNull(abattoir) ? "" : abattoir.getName());

            // 获取批发商信息
            SupplierDTO supplier = this.supplierClient.getSupplierById(porkReport.getSupplierId());
            porkArrangeAuditVO.setSupplierName(Objects.isNull(supplier) ? "" : supplier.getName());
            porkArrangeAuditVO.setSupplierCode(Objects.isNull(supplier) ? "" : supplier.getCode());

            if (Objects.nonNull(porkReport.getStallId())) {
                // 获取档位信息
                StallDTO stall = this.stallClient.getStallById(porkReport.getStallId());
                porkArrangeAuditVO.setStallName(Objects.isNull(stall) ? "" : stall.getName());
            }
        }

        // 获取检疫票信息
        PratiqueRegister pratiqueRegister = this.pratiqueRegisterService.getById(porkArrangeAudit.getPratiqueRegisterId());
        if (Objects.nonNull(pratiqueRegister)) {
            UserDTO user = this.userClient.getUser(pratiqueRegister.getRegistrantId());
            porkArrangeAuditVO.setRegistrantName(Objects.isNull(user) ? "" : user.getName());
            porkArrangeAuditVO.setPratiqueTickets(
                    Objects.isNull(pratiqueRegister.getPratiqueTickets()) ? 0 : pratiqueRegister.getPratiqueTickets());
            porkArrangeAuditVO.setPratiqueCodes(Objects.isNull(pratiqueRegister.getPratiqueCode()) ? Collections.emptyList() :
                                                Arrays.asList(pratiqueRegister.getPratiqueCode().split(",")));
        }

        // 获取食安抽检信息
        SpotCheck spotCheck = this.spotCheckService.getById(porkArrangeAudit.getSpotCheckId());
        if (Objects.nonNull(spotCheck)) {
            porkArrangeAuditVO.setClenbuterol(spotCheck.getClenbuterol());
            porkArrangeAuditVO.setWatered(spotCheck.getWatered());
            SpotCheckEnum result =
                    spotCheck.getClenbuterol() == SpotCheckEnum.NEGATIVE && spotCheck.getWatered() == SpotCheckEnum.NEGATIVE ?
                    SpotCheckEnum.NEGATIVE : SpotCheckEnum.POSITIVE;
            porkArrangeAuditVO.setSpotCheckStatus(result);

            // 获取抽检员信息
            Inspector inspector = this.inspectorService.getById(spotCheck.getInspectorId());
            porkArrangeAuditVO.setInspectorName(Objects.isNull(inspector) ? "" : inspector.getName());
        }

        // 获取理货信息
        PorkArrange porkArrange = this.porkArrangeService.getById(porkArrangeAudit.getPorkArrangeId());
        porkArrangeAuditVO.setPorkArrangeStatus(
                Objects.isNull(porkArrange.getStatus()) ? PorkArrangeEnum.UNKNOWN : porkArrange.getStatus());
        porkArrangeAuditVO.setPorkArrangeSlices(Objects.isNull(porkArrange.getSlices()) ? 0 : porkArrange.getSlices());
        porkArrangeAuditVO.setPorkArrangeWeight(Objects.isNull(porkArrange.getWeight()) ? BigDecimal.ZERO : porkArrange.getWeight());
        porkArrangeAuditVO.setPorkArrangePlace(Objects.isNull(porkArrange.getPlace()) ? "" : porkArrange.getPlace());
        porkArrangeAuditVO.setPorkArrangeTime(Objects.isNull(porkArrange.getPorkArrangeTime()) ? null : porkArrange.getPorkArrangeTime());

        return porkArrangeAuditVO;
    }

    /**
     * 理货审核分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 理货审核信息
     */
    @GetMapping("/pork-arrange-audit/page")
    public Pagination<PorkArrangeAuditVO> pagePorkArrangeAudit(@Valid PorkArrangeAuditQueryParam queryParam) {
        // 市场租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        if (StringUtils.isNotEmpty(queryParam.getKeyword())) {
            // 获取批发商信息
            List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), SessionContextHolder.getRequester()
                    .getTenant());
            Set<Long> supplierIds = suppliers.stream().map(SupplierDTO::getId).collect(Collectors.toSet());
            queryParam.setSupplierIds(supplierIds);
        }
        // 获取理货审核信息
        queryParam.setTenant(tenant);
        Page<PorkArrangeAuditVO> page = this.porkArrangeAuditService.pagePorkArrangeAudit(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        // 获取批发商主键/批发商信息映射关系
        Set<Long> supplierIds = page.getRecords().stream().map(PorkArrangeAuditVO::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取登记检疫票主键/登记检疫票信息映射关系
        Set<Long> pratiqueRegisterIds = page.getRecords().stream().map(PorkArrangeAuditVO::getPratiqueRegisterId)
                .collect(Collectors.toSet());
        PratiqueRegisterQueryParam pratiqueRegisterQueryParam = new PratiqueRegisterQueryParam();
        pratiqueRegisterQueryParam.setIds(pratiqueRegisterIds);
        pratiqueRegisterQueryParam.setTenant(tenant);
        List<PratiqueRegister> pratiqueRegisters = this.pratiqueRegisterService.listPratiqueRegister(pratiqueRegisterQueryParam);
        Map<Long, List<PratiqueRegister>> pratiqueMap = pratiqueRegisters.stream().collect(Collectors.groupingBy(PratiqueRegister::getId));

        // 获取食安抽检主键/抽检信息映射关系
        Set<Long> spotCheckIds = page.getRecords().stream().map(PorkArrangeAuditVO::getSpotCheckId).collect(Collectors.toSet());
        SpotCheckQueryParam spotCheckQueryParam = new SpotCheckQueryParam();
        spotCheckQueryParam.setIds(spotCheckIds);
        spotCheckQueryParam.setTenant(tenant);
        List<SpotCheck> spotChecks = this.spotCheckService.listSpotCheck(spotCheckQueryParam);
        Map<Long, List<SpotCheck>> spotCheckMap = spotChecks.stream().collect(Collectors.groupingBy(SpotCheck::getId));
        //理货设备查询
        //获取理货设备信息
        Set<Long> deviceIds = page.getRecords().stream().filter(porkArrange -> StringUtils.isNotEmpty(porkArrange.getPorkArrangePlace())
                                                                               && StringUtils.isNumeric(porkArrange.getPorkArrangePlace()))
                .map(porkArrange -> Long.valueOf(porkArrange.getPorkArrangePlace()))
                .collect(Collectors.toSet());
        Map<Long, MeattradeDeviceVO> simpleInstanceDTOMap = this.meattradeDeviceQueryService.queryDeviceByIds(deviceIds)
                .stream().collect(Collectors.toMap(MeattradeDeviceVO::getId, obj -> obj, (o, n) -> n));
        page.getRecords().forEach(porkArrangeAudit -> {
            if (supplierMap.containsKey(porkArrangeAudit.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(porkArrangeAudit.getSupplierId()).get(0);
                porkArrangeAudit.setSupplierName(supplier.getName());
                porkArrangeAudit.setSupplierCode(supplier.getCode());
                porkArrangeAudit.setRiskLevel(supplier.getRiskLevel());
            }
            if (pratiqueMap.containsKey(porkArrangeAudit.getPratiqueRegisterId())) {
                PratiqueRegister pratiqueRegister = pratiqueMap.get(porkArrangeAudit.getPratiqueRegisterId()).get(0);
                porkArrangeAudit.setPratiqueTickets(pratiqueRegister.getPratiqueTickets());
            }
            if (spotCheckMap.containsKey(porkArrangeAudit.getSpotCheckId())) {
                SpotCheck spotCheck = spotCheckMap.get(porkArrangeAudit.getSpotCheckId()).get(0);
                porkArrangeAudit.setClenbuterol(spotCheck.getClenbuterol());
                porkArrangeAudit.setWatered(spotCheck.getWatered());
                //                porkArrangeAudit.setSpotCheckStatus();
            }
            if (StringUtils.isNotEmpty(porkArrangeAudit.getPorkArrangePlace()) &&
                StringUtils.isNumeric(porkArrangeAudit.getPorkArrangePlace()) &&
                simpleInstanceDTOMap.containsKey(Long.valueOf(porkArrangeAudit.getPorkArrangePlace()))) {
                MeattradeDeviceVO simpleInstanceDTO = simpleInstanceDTOMap.get(Long.valueOf(porkArrangeAudit.getPorkArrangePlace()));
                porkArrangeAudit.setPorkArrangePlace(simpleInstanceDTO.getName());
            } else {
                porkArrangeAudit.setPorkArrangePlace(null);
            }
            porkArrangeAudit.setMark(this.isMark(porkArrangeAudit));
        });

        return PagingUtils.convert(page);
    }

    /**
     * 理货审核记录
     *
     * @param id 理货审核主键
     *
     * @return 理货审核记录信息
     */
    @GetMapping("/pork-arrange-audit/log/{id}")
    public List<PorkArrangeAuditLogVO> list(@NotNull(message = "理货审核主键不能为空") @Min(value = 1, message = "理货审核主键不能小于1")
                                            @PathVariable("id") Long id
                                           ) {
        List<PorkArrangeAuditLog> porkArrangeAuditLogs = this.porkArrangeAuditLogService.listByPorkArrangeAuditId(id);
        if (CollectionUtils.isEmpty(porkArrangeAuditLogs)) {
            return Collections.emptyList();
        }
        List<PorkArrangeAuditLogVO> porkArrangeAuditLogVOS = ObjectUtils.copying(porkArrangeAuditLogs, PorkArrangeAuditLogVO.class);
        // 扩展审核员
        List<Long> auditorIds = porkArrangeAuditLogs.stream().map(PorkArrangeAuditLog::getAuditorId).collect(Collectors.toList());
        UserQueryParam userQueryParam = UserQueryParam.builder().ids(auditorIds).pageSize(auditorIds.size()).build();
        Pagination<UserDTO> users = userClient.listUsers(userQueryParam);
        Map<Long, List<UserDTO>> userMap = users.getRecords().stream().collect(Collectors.groupingBy(UserDTO::getId));
        for (PorkArrangeAuditLogVO porkArrangeAuditLog : porkArrangeAuditLogVOS) {
            if (!MapUtils.isEmpty(userMap) && userMap.containsKey(porkArrangeAuditLog.getAuditorId())) {
                porkArrangeAuditLog.setAuditorName(userMap.get(porkArrangeAuditLog.getAuditorId()).get(0).getName());
            }
        }
        return porkArrangeAuditLogVOS;
    }

    private Boolean isMark(PorkArrangeAuditVO porkArrangeAudit) {
        if (porkArrangeAudit.getPorkArrangeStatus() != PorkArrangeEnum.FINISHED) {
            return true;
        }
        if (porkArrangeAudit.getClenbuterol() == SpotCheckEnum.UNSELECTED && porkArrangeAudit.getWatered() == SpotCheckEnum.UNSELECTED) {
            return true;
        }
        return Objects.isNull(porkArrangeAudit.getPratiqueTickets());
    }

}
