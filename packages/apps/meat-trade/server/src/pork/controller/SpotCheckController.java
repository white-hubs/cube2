package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Dictionary;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Inspector;
import com.guhecloud.cube.biz.meattrade.server.base.service.DictionaryService;
import com.guhecloud.cube.biz.meattrade.server.base.service.InspectorService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheck;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SpotCheckQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckDetailVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.SpotCheckVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 食安抽检接口
 */
@Slf4j
@RestController("meattrade-spotCheckController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpotCheckController {

    private final SpotCheckService spotCheckService;
    private final PorkReportService porkReportService;
    private final InspectorService inspectorService;
    private final SpotCheckDetailService spotCheckDetailService;
    private final DictionaryService dictionaryService;
    private final PorkArrangeAuditService porkArrangeAuditService;

    private final SupplierClient supplierClient;
    private final StallClient stallClient;

    /**
     * 食安抽检编辑
     *
     * @param id    食安抽检主键
     * @param param 编辑参数
     */
    @PutMapping("/spot-check/{id}")
    public void updateSpotCheck(@PathVariable("id") Long id, @RequestBody SpotCheckModifyParam param) {
        this.spotCheckService.updateSpotCheck(id, param);
    }

    /**
     * 食安抽检详情信息
     *
     * @param id 食安抽检主键
     *
     * @return 食安抽检信息
     */
    @GetMapping("/spot-check/{id}")
    public SpotCheckVO getById(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        SpotCheck spotCheck = this.spotCheckService.getById(id);
        Assert.isNotFound(spotCheck, "不存在食安抽检信息!");

        // 获取报备信息
        PorkReport porkReport = this.porkReportService.getById(spotCheck.getPorkReportId());
        Assert.isNotFound(porkReport, "不存在来货报备信息");

        // 获取批发商信息
        SupplierDTO supplier = this.supplierClient.getSupplierById(porkReport.getSupplierId());

        // 获取档位信息
        StallDTO stall = this.stallClient.getStallById(porkReport.getStallId());

        // 获取抽检项信息
        List<SpotCheckDetail> spotCheckDetails = this.spotCheckDetailService.getBySpotCheck(spotCheck.getId(), spotCheck.getTenant());

        // 获取抽检项字典名称
        List<Dictionary> dictionaries = this.dictionaryService.getByType("CLENBUTEROL");
        Map<Long, List<Dictionary>> dictionaryMap = dictionaries.stream().collect(Collectors.groupingBy(Dictionary::getId));
        List<SpotCheckDetailVO> detailVOS = spotCheckDetails.stream().map(spotCheckDetail -> {
            SpotCheckDetailVO detailVO = ObjectUtils.copying(spotCheckDetail, SpotCheckDetailVO.class);
            if (dictionaryMap.containsKey(detailVO.getSpotCheckItemId())) {
                Dictionary dictionary = dictionaryMap.get(detailVO.getSpotCheckItemId()).get(0);
                detailVO.setSpotCheckItemName(dictionary.getName());
                SpotCheckEnum passed = detailVO.getPassed();
                detailVO.setPassed(passed == SpotCheckEnum.UNSELECTED ? null : passed);
                detailVO.setSelected(passed == SpotCheckEnum.UNSELECTED ? SpotCheckEnum.UNSELECTED : SpotCheckEnum.SELECTED);
            }
            return detailVO;
        }).collect(Collectors.toList());

        // 构建信息
        SpotCheckVO spotCheckVO = ObjectUtils.copying(spotCheck, SpotCheckVO.class);
        spotCheckVO.setSupplierName(Objects.isNull(supplier) ? "" : supplier.getName());
        spotCheckVO.setSupplierCode(Objects.isNull(supplier) ? "" : supplier.getCode());
        spotCheckVO.setPorkReportCode(porkReport.getCode());
        spotCheckVO.setPorkReportSlices(porkReport.getReportSlices());
        spotCheckVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
        spotCheckVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
        spotCheckVO.setFreshLabel(porkReport.getFreshLabel());
        spotCheckVO.setStallName(Objects.isNull(stall) ? "" : stall.getName());
        spotCheckVO.setDetails(detailVOS);

        // 获取抽检员信息
        if (Objects.nonNull(spotCheck.getInspectorId())) {
            Inspector inspector = this.inspectorService.getById(spotCheck.getInspectorId());
            spotCheckVO.setInspectorName(Objects.isNull(inspector) ? "" : inspector.getName());
        }

        return spotCheckVO;
    }

    /**
     * 食安抽检分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 食安抽检信息
     */
    @GetMapping("/spot-check/page")
    public Pagination<SpotCheckVO> pageSpotCheck(@Valid SpotCheckQueryParam queryParam) {
        // 市场租户编号
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        if (StringUtils.isNotEmpty(queryParam.getKeyword())) {
            // 获取批发商信息
            List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), SessionContextHolder.getRequester()
                    .getTenant());
            Set<Long> supplierIds = suppliers.stream().map(SupplierDTO::getId).collect(Collectors.toSet());
            queryParam.setSupplierIds(supplierIds);
        }
        queryParam.setTenant(tenant);
        // 获取食安抽检信息
        Page<SpotCheckVO> page = this.spotCheckService.getSpotCheckPage(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<SpotCheckVO> spotChecks = ObjectUtils.copying(page.getRecords(), SpotCheckVO.class);

        // 获取批发商主键/批发商映射关系
        Set<Long> supplierIds = page.getRecords().stream().map(SpotCheckVO::getSupplierId).collect(Collectors.toSet());
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取报备主键/报备信息映射关系

        // 获取抽检人员主键/抽检人员信息映射关系
        List<Inspector> inspectors = this.inspectorService.list();
        Map<Long, List<Inspector>> inspectorMap = inspectors.stream().collect(Collectors.groupingBy(Inspector::getId));

        // 获取食安抽检对应的理货审核信息
        Set<Long> spotCheckIds = spotChecks.stream().map(SpotCheckVO::getId).collect(Collectors.toSet());
        List<PorkArrangeAudit> porkArrangeAudits = this.porkArrangeAuditService.listBySpotChecks(spotCheckIds, tenant);
        Map<Long, List<PorkArrangeAudit>> porkArrangeAuditMap =
                porkArrangeAudits.stream().collect(Collectors.groupingBy(PorkArrangeAudit::getSpotCheckId));

        // 构建信息
        spotChecks = spotChecks.stream().map(spotCheck -> {
            if (supplierMap.containsKey(spotCheck.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(spotCheck.getSupplierId()).get(0);
                spotCheck.setSupplierName(supplier.getName());
                spotCheck.setSupplierCode(supplier.getCode());
                spotCheck.setRiskLevel(supplier.getRiskLevel());
            }
            if (inspectorMap.containsKey(spotCheck.getInspectorId())) {
                Inspector inspector = inspectorMap.get(spotCheck.getInspectorId()).get(0);
                spotCheck.setInspectorName(inspector.getName());
            }
            if (porkArrangeAuditMap.containsKey(spotCheck.getId())) {
                PorkArrangeAudit porkArrangeAudit = porkArrangeAuditMap.get(spotCheck.getId()).get(0);
                spotCheck.setMark(porkArrangeAudit.getAuditStatus() == PorkArrangeAuditEnum.AUDIT);
            }
            return spotCheck;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, spotChecks);
    }

}
