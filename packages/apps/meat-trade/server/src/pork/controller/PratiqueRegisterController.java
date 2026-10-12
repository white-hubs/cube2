package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PratiqueRegister;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkReportQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PratiqueRegisterQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PratiqueRegisterService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PratiqueRegisterVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登记检疫票接口
 */
@Slf4j
@RestController("meattrade-pratiqueRegisterController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PratiqueRegisterController {

    private final PratiqueRegisterService pratiqueRegisterService;
    private final PorkReportService porkReportService;
    private final PorkArrangeAuditService porkArrangeAuditService;

    private final SupplierClient supplierClient;
    private final UserClient userClient;

    /**
     * 登记检疫票修改
     *
     * @param id    检疫票主键
     * @param param 编辑参数
     */
    @PutMapping("/pratique-register/{id}")
    public void updatePratiqueRegister(@NotNull(message = "登记检疫票主键不能为空") @PathVariable("id") Long id,
                                       @Valid @RequestBody PratiqueRegisterModifyParam param
                                      ) {
        this.pratiqueRegisterService.updatePratiqueRegister(id, param);
    }

    /**
     * 登记检疫票详情信息
     *
     * @param id 登记检疫票主键
     *
     * @return 登记检疫票信息
     */
    @GetMapping("/pratique-register/{id}")
    public PratiqueRegisterVO getById(@NotNull(message = "登记检疫票主键不能为空") @PathVariable("id") Long id) {
        PratiqueRegister pratiqueRegister = this.pratiqueRegisterService.getById(id);
        Assert.isNotFound(pratiqueRegister, "不存在登记检疫票信息!");
        // 获取报备信息
        PorkReport porkReport = this.porkReportService.getById(pratiqueRegister.getPorkReportId());
        Assert.isNotFound(porkReport, "不存在报备信息!");

        // 获取批发商信息
        SupplierDTO supplier = this.supplierClient.getSupplierById(porkReport.getSupplierId());

        // 构建信息
        PratiqueRegisterVO registerVO = ObjectUtils.copying(pratiqueRegister, PratiqueRegisterVO.class);
        registerVO.setSupplierName(Objects.isNull(supplier) ? "" : supplier.getName());
        registerVO.setSupplierCode(Objects.isNull(supplier) ? "" : supplier.getCode());
        registerVO.setRiskLevel(Objects.isNull(supplier) ? null : supplier.getRiskLevel());
        registerVO.setPratiqueCodes(Objects.isNull(registerVO.getPratiqueCode()) ? Collections.emptyList() :
                                    Arrays.asList(registerVO.getPratiqueCode().split(",")));
        registerVO.setPorkReportCode(porkReport.getCode());
        registerVO.setPorkReportSlices(porkReport.getReportSlices());
        registerVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
        registerVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
        registerVO.setFreshLabel(porkReport.getFreshLabel());
        return registerVO;
    }

    /**
     * 登记检疫票分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 登记检疫票信息
     */
    @GetMapping("/pratique-register/page")
    public Pagination<PratiqueRegisterVO> pagePratiqueRegister(@Valid PratiqueRegisterQueryParam queryParam) {
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
        // 获取登记检疫票信息
        Page<PratiqueRegisterVO> page = this.pratiqueRegisterService.pagePratiqueRegister(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<PratiqueRegisterVO> pageVOS = ObjectUtils.copying(page.getRecords(), PratiqueRegisterVO.class);
        // 获取来货报备主键/来货报备信息映射关系
        PorkReportQueryParam porkReportQueryParam = new PorkReportQueryParam();
        porkReportQueryParam.setTenant(tenant);
        Set<Long> supplierIds = page.getRecords().stream().map(PratiqueRegisterVO::getSupplierId).collect(Collectors.toSet());
        // 获取批发商主键/批发商映射关系
        List<SupplierDTO> suppliers = this.supplierClient.listByIds(supplierIds);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取登记员主键/用户信息映射关系
        Set<Long> registrantIds = pageVOS.stream().map(PratiqueRegisterVO::getRegistrantId).filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<UserDTO> users = this.userClient.listUserByIds(registrantIds);
        Map<Long, List<UserDTO>> userMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));

        // 获取对应登记检疫票是否审核
        Set<Long> pratiqueRegisterIds = page.getRecords().stream().map(PratiqueRegisterVO::getId).collect(Collectors.toSet());
        List<PorkArrangeAudit> porkArrangeAudits = this.porkArrangeAuditService.listByPratiqueRgisters(pratiqueRegisterIds, tenant);
        Map<Long, List<PorkArrangeAudit>> pratiqueRegisterMap =
                porkArrangeAudits.stream().collect(Collectors.groupingBy(PorkArrangeAudit::getPratiqueRegisterId));
        // 构建信息
        pageVOS = pageVOS.stream().map(pr -> {
            if (supplierMap.containsKey(pr.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(pr.getSupplierId()).get(0);
                pr.setSupplierId(supplier.getId());
                pr.setSupplierName(supplier.getName());
                pr.setSupplierCode(supplier.getCode());
            }
            if (userMap.containsKey(pr.getRegistrantId())) {
                UserDTO user = userMap.get(pr.getRegistrantId()).get(0);
                pr.setRegistrantName(user.getName());
            }
            if (pratiqueRegisterMap.containsKey(pr.getId())) {
                PorkArrangeAudit porkArrangeAudit = pratiqueRegisterMap.get(pr.getId()).get(0);
                pr.setMark(porkArrangeAudit.getAuditStatus() == PorkArrangeAuditEnum.AUDIT);
            }
            return pr;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, pageVOS);
    }

}
