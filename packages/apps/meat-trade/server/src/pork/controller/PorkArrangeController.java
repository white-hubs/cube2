package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.entity.GoodsWeightConfig;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.MeatGoodsFreshLabelEnum;
import com.guhecloud.cube.biz.meattrade.server.base.service.AbattoirService;
import com.guhecloud.cube.biz.meattrade.server.base.service.GoodsWeightConfigService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SupplierTallyInsertParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TallyQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.TallyReportVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.TallyVO;
import com.guhecloud.cube.biz.meattrade.server.third.api.ThirdMeatTradeApiService;
import com.guhecloud.cube.biz.meattrade.server.third.response.GetPendingTalliesResponse;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 理货数据接口
 *
 * @author lzy
 * @date 2021/12/8
 */
@Slf4j
@RestController("meattrade-porkArrangeController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangeController {

    private final PorkArrangeService porkArrangeService;
    private final PorkArrangeAuditService porkArrangeAuditService;
    private final PorkReportService porkReportService;
    private final AbattoirService abattoirService;

    private final SupplierClient supplierClient;
    private final ThirdMeatTradeApiService thirdMeatTradeApiService;
    private final GoodsWeightConfigService goodsWeightConfigService;

    /**
     * 新增理货数据
     *
     * @param param
     */
    @PostMapping("/save-supplier/pork-arrange")
    public void saveSupplierAndPorkArrange(@Valid @RequestBody SupplierTallyInsertParam param) {
        this.porkReportService.saveSupplierAndPorkArrange(param);
    }

    /**
     * 获取第三方待理货批次数据信息
     *
     * @param param
     */
    @GetMapping("/third/getPendingTallies")
    public List<TallyVO> getThirdPendingTallies(@RequestParam Map<String, String> param) {
        List<GetPendingTalliesResponse> pendingTallies = this.thirdMeatTradeApiService.getPendingTallies(param);
        if (CollectionUtils.isEmpty(pendingTallies)) {
            return Collections.emptyList();
        }
        GetPendingTalliesResponse talliesResponse = pendingTallies.get(0);
        SupplierTallyInsertParam supplierTallyInsertParam = buildSupplierTallyInsertParam(param, talliesResponse);
        porkReportService.saveSupplierAndPorkArrange(supplierTallyInsertParam);
        TallyQueryParam tallyQueryParam = new TallyQueryParam();
        tallyQueryParam.setMidCode(talliesResponse.getMerchantId());
        tallyQueryParam.setMidType("phone");
        tallyQueryParam.setMidValue(talliesResponse.getMerchantId());
        tallyQueryParam.setTallyId(talliesResponse.getBatches().get(0).getTallyId());
        return this.getAuditTally(tallyQueryParam);
    }

    /**
     * 获取第三方报备信息
     *
     * @param param
     *
     * @return TallyVO
     */
    @GetMapping("/third/getTallies")
    public List<TallyVO> getThirdTallies(@RequestParam Map<String, String> param) {
        var pendingTallies = this.thirdMeatTradeApiService.getPendingTallies(param);
        if (CollectionUtils.isEmpty(pendingTallies)) {
            return Collections.emptyList();
        }
        var talliesResponse = pendingTallies.getFirst();
        return this.getTally(talliesResponse.getBatches().getFirst().getTallyId());
    }

    private SupplierTallyInsertParam buildSupplierTallyInsertParam(Map<String, String> param,
                                                                   GetPendingTalliesResponse talliesResponse
                                                                  ) {
        List<GoodsWeightConfig> goodsWeightConfigs = goodsWeightConfigService.listGoodWeightConfig();
        GoodsWeightConfig goodsWeightConfig = goodsWeightConfigs.stream().filter(config ->
                                                                                         talliesResponse.getBatches().get(0)
                                                                                                 .getGoodsCategoryId()
                                                                                                 .equals(config.getGoodsCategoryId()))
                .findFirst().orElse(null);
        SupplierTallyInsertParam supplierTallyInsertParam = new SupplierTallyInsertParam();
        supplierTallyInsertParam.setDeviceId(param.get("deviceId"));
        supplierTallyInsertParam.setMidType("phone");
        supplierTallyInsertParam.setMidCode(talliesResponse.getMerchantId());
        supplierTallyInsertParam.setMidName(talliesResponse.getMerchantName());
        supplierTallyInsertParam.setMidPhone(talliesResponse.getMerchantId());
        supplierTallyInsertParam.setTallyId(talliesResponse.getBatches().get(0).getTallyId());
        supplierTallyInsertParam.setGoodsCategoryId(talliesResponse.getBatches().get(0).getGoodsCategoryId());
        supplierTallyInsertParam.setGoodsCategoryName(
                Objects.nonNull(goodsWeightConfig) ? goodsWeightConfig.getGoodsCategoryName() : StringUtils.EMPTY_STRING);
        supplierTallyInsertParam.setAreaName(talliesResponse.getAreaName());
        supplierTallyInsertParam.setMeatFarmName(talliesResponse.getBatches().get(0).getMeatFarmName());
        supplierTallyInsertParam.setReportedCount(talliesResponse.getBatches().get(0).getReportedCount());
        supplierTallyInsertParam.setReportedWeight(talliesResponse.getBatches().get(0).getReportedWeight());
        if (StringUtils.isNotBlank(talliesResponse.getBatches().get(0).getFreshLabel())) {
            supplierTallyInsertParam.setFreshLabel(MeatGoodsFreshLabelEnum.valueOf(talliesResponse.getBatches().get(0).getFreshLabel()));
        }
        return supplierTallyInsertParam;
    }

    /**
     * 查询指定商户的待处理理货批次
     *
     * @param queryParam 查询参数
     *
     * @return 理货批次信息
     */
    @GetMapping("/getPendingTallies")
    public List<TallyVO> getAuditTally(TallyQueryParam queryParam) {
        // 获取批发商信息
        List<SupplierDTO> suppliers = this.supplierClient.getByMidType(queryParam.getMidType(), queryParam.getMidValue());
        Assert.isNotFound(CollectionUtils.isEmpty(suppliers), "不存在批发商信息!");

        // 市场租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<PorkReport> porkReports = new ArrayList<>();
        Set<Long> supplierIds = suppliers.stream().map(SupplierDTO::getId).collect(Collectors.toSet());
        Set<Long> porkReportIds = new HashSet<>();
        if (StringUtils.isNotEmpty(queryParam.getTallyId())) {
            PorkReport porkReport = this.porkReportService.getByCode(queryParam.getTallyId(), tenant);
            Assert.isNotFound(porkReport, "不存在报备号!");
            porkReportIds.add(porkReport.getId());
            porkReports.add(porkReport);
        } else {
            porkReports = this.porkReportService.listPorkReportBySuppliers(supplierIds, tenant);
            porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toSet());
        }

        // 获取理货信息
        List<PorkArrange> porkArranges = this.porkArrangeService.getPendingTallies(queryParam, porkReportIds);
        Map<Long, List<PorkArrange>> porkArrangeMap =
                porkArranges.stream().collect(Collectors.groupingBy(PorkArrange::getPorkReportId));

        // 构建信息
        List<PorkReport> finalPorkReports = porkReports;
        List<TallyVO> tallyVOS = suppliers.stream().map(supplier -> {
            TallyVO tallyVO = new TallyVO();
            tallyVO.setMerchantId(String.valueOf(supplier.getId()));
            tallyVO.setMerchantName(supplier.getName());
            tallyVO.setMerchantCode(supplier.getPhone());
            tallyVO.setPhone(supplier.getPhone());

            List<TallyReportVO> tallyReportVOS = finalPorkReports.stream().map(porkReport -> {
                if (porkArrangeMap.containsKey(porkReport.getId()) && Objects.equals(porkReport.getSupplierId(), supplier.getId())) {
                    PorkArrange porkArrange = porkArrangeMap.get(porkReport.getId()).get(0);
                    TallyReportVO tallyReportVO = new TallyReportVO();
                    tallyReportVO.setTallyId(porkReport.getCode());
                    //                    tallyReportVO.setProductName("白条猪");
                    tallyReportVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
                    tallyReportVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
                    tallyReportVO.setFreshLabel(porkReport.getFreshLabel());
                    tallyReportVO.setReportedCount(String.valueOf(porkReport.getReportSlices()));
                    tallyReportVO.setReportedWeight(String.valueOf(porkReport.getReportWeight()));
                    tallyReportVO.setProcessedCount(Objects.isNull(porkArrange.getSlices()) ? "0" :
                                                    String.valueOf(porkArrange.getSlices()));
                    tallyReportVO.setProcessedWeight(Objects.isNull(porkArrange.getWeight()) ? "0" :
                                                     String.valueOf(porkArrange.getWeight()));
                    return tallyReportVO;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            tallyVO.setBatches(tallyReportVOS);
            return tallyVO;
        }).collect(Collectors.toList());
        return tallyVOS;
    }

    /**
     * 查询理货批次
     *
     * @param tallyId 批次主键
     *
     * @return 理货批次信息
     */
    @GetMapping("/getTallies/{tallyId}")
    public List<TallyVO> getTally(@PathVariable("tallyId") String tallyId) {
        PorkReport validPorkReport = this.porkReportService.getByCode(tallyId, SessionContextHolder.getRequester().getTenant());
        Assert.isNotFound(validPorkReport, "不存在报备号!");
        // 获取批发商信息
        List<SupplierDTO> suppliers = this.supplierClient.getByMidType("merchantId", validPorkReport.getSupplierId().toString());
        Assert.isNotFound(CollectionUtils.isEmpty(suppliers), "不存在批发商信息!");

        List<PorkReport> porkReports = new ArrayList<>();
        Set<Long> porkReportIds = new HashSet<>();
        porkReportIds.add(validPorkReport.getId());
        porkReports.add(validPorkReport);

        // 获取理货信息
        List<PorkArrange> porkArranges = this.porkArrangeService.getTallies(porkReportIds);
        Map<Long, List<PorkArrange>> porkArrangeMap =
                porkArranges.stream().collect(Collectors.groupingBy(PorkArrange::getPorkReportId));

        // 构建信息
        List<PorkReport> finalPorkReports = porkReports;
        List<TallyVO> tallyVOS = suppliers.stream().map(supplier -> {
            TallyVO tallyVO = new TallyVO();
            tallyVO.setMerchantId(String.valueOf(supplier.getId()));
            tallyVO.setMerchantName(supplier.getName());
            tallyVO.setMerchantCode(supplier.getPhone());
            tallyVO.setPhone(supplier.getPhone());
            List<TallyReportVO> tallyReportVOS = finalPorkReports.stream().map(porkReport -> {
                if (porkArrangeMap.containsKey(porkReport.getId()) && Objects.equals(porkReport.getSupplierId(), supplier.getId())) {
                    PorkArrange porkArrange = porkArrangeMap.get(porkReport.getId()).get(0);
                    TallyReportVO tallyReportVO = new TallyReportVO();
                    tallyReportVO.setTallyId(porkReport.getCode());
                    //                    tallyReportVO.setProductName("白条猪");
                    tallyReportVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
                    tallyReportVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
                    tallyReportVO.setFreshLabel(porkReport.getFreshLabel());
                    tallyReportVO.setReportedCount(String.valueOf(porkReport.getReportSlices()));
                    tallyReportVO.setReportedWeight(String.valueOf(porkReport.getReportWeight()));
                    tallyReportVO.setProcessedCount(Objects.isNull(porkArrange.getSlices()) ? "0" :
                                                    String.valueOf(porkArrange.getSlices()));
                    tallyReportVO.setProcessedWeight(Objects.isNull(porkArrange.getWeight()) ? "0" :
                                                     String.valueOf(porkArrange.getWeight()));
                    return tallyReportVO;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            tallyVO.setBatches(tallyReportVOS);
            return tallyVO;
        }).collect(Collectors.toList());
        return tallyVOS;
    }

    /**
     * 小程序理货数据分页
     *
     * @param queryParam 查询参数
     *
     * @return 理货数据信息
     */
    @GetMapping("/pork-arrange/mp/page")
    public Pagination<PorkArrangeVO> pageMpPorkArrange(@Valid PorkArrangeQueryParam queryParam) {
        // 获取当前批发商信息
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");

        // 获取该批发商下的所有报备信息
        List<PorkReport> porkReports = this.porkReportService.listPorkReportBySupplier(supplier.getId(), supplier.getTenant());
        Set<Long> porkReportIds = porkReports.stream().map(PorkReport::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(porkReportIds)) {
            return Pagination.empty();
        }

        // 查询理货信息
        queryParam.setPorkReportIds(porkReportIds);
        queryParam.setTenant(supplier.getTenant());
        Page<PorkArrange> page = this.porkArrangeService.pageMpPorkArrange(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        List<PorkArrangeVO> porkArranges = ObjectUtils.copying(page.getRecords(), PorkArrangeVO.class);
        // 获取报备主键/报备信息映射关系
        Map<Long, List<PorkReport>> porkReportMap = porkReports.stream().collect(Collectors.groupingBy(PorkReport::getId));

        // 获取屠宰场主键/屠宰场信息映射关系
        Set<Long> abattoirIds = porkReports.stream().map(PorkReport::getAbattoirId).collect(Collectors.toSet());
        List<Abattoir> abattoirs = this.abattoirService.getByIds(abattoirIds, supplier.getTenant());
        Map<Long, List<Abattoir>> abattoirMap = abattoirs.stream().collect(Collectors.groupingBy(Abattoir::getId));

        // 获取理货审核主键/理货审核信息映射关系
        Set<Long> ids = porkArranges.stream().map(PorkArrangeVO::getId).collect(Collectors.toSet());
        List<PorkArrangeAudit> porkArrangeAudits = this.porkArrangeAuditService.listByPorkArranges(ids, supplier.getTenant());
        Map<Long, List<PorkArrangeAudit>> porkArrangeAuditMap =
                porkArrangeAudits.stream().collect(Collectors.groupingBy(PorkArrangeAudit::getPorkArrangeId));

        // 构建信息
        porkArranges = porkArranges.stream().map(porkArrange -> {
            if (porkReportMap.containsKey(porkArrange.getPorkReportId())) {
                PorkReport porkReport = porkReportMap.get(porkArrange.getPorkReportId()).get(0);
                porkArrange.setPorkReportSlices(porkReport.getReportSlices());
                porkArrange.setPorkReportWeight(porkReport.getReportWeight());
                porkArrange.setPorkReportTime(porkReport.getReportTime());
                porkArrange.setPorkReportCode(porkReport.getCode());
                porkArrange.setGoodsCategoryId(porkReport.getGoodsCategoryId());
                porkArrange.setGoodsCategoryName(porkReport.getGoodsCategoryName());
                porkArrange.setFreshLabel(porkReport.getFreshLabel());
                if (abattoirMap.containsKey(porkReport.getAbattoirId())) {
                    Abattoir abattoir = abattoirMap.get(porkReport.getAbattoirId()).get(0);
                    porkArrange.setAbattoirName(abattoir.getName());
                }
            }

            if (porkArrangeAuditMap.containsKey(porkArrange.getId())) {
                PorkArrangeAudit porkArrangeAudit = porkArrangeAuditMap.get(porkArrange.getId()).get(0);
                porkArrange.setAuditStatus(porkArrangeAudit.getAuditStatus());
                porkArrange.setRemark(porkArrangeAudit.getRemark());
            }

            porkArrange.setSlices(Objects.isNull(porkArrange.getSlices()) ? 0 : porkArrange.getSlices());
            porkArrange.setWeight(Objects.isNull(porkArrange.getWeight()) ? BigDecimal.ZERO : porkArrange.getWeight());

            return porkArrange;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, porkArranges);
    }

}
