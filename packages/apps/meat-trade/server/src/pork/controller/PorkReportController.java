package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.service.AbattoirService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkReportModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkReportQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkReportVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.TallQuarantineTicketFileVO;
import com.guhecloud.cube.biz.meattrade.server.third.api.ThirdMeatTradeApiService;
import com.guhecloud.cube.biz.meattrade.server.third.config.ThirdMeatTradeConfig;
import com.guhecloud.cube.biz.meattrade.server.third.response.TallQuarantineTicketFileResponse;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BarcodeUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lzy
 * @date 2021/12/4
 */
@Slf4j
@RestController("meattrade-porkReportController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkReportController {

    private final PorkReportService porkReportService;
    private final AbattoirService abattoirService;

    private final UserClient userClient;
    private final FileClient fileClient;
    private final StallClient stallClient;
    private final SupplierClient supplierClient;
    private final ThirdMeatTradeApiService thirdMeatTradeApiService;

    /**
     * 猪肉滑轨来货报备新增
     *
     * @param param 新增参数
     */
    @PostMapping("/pork-report")
    public void addPorkReport(@Valid @RequestBody PorkReportModifyParam param) {
        this.porkReportService.addPorkReport(param);
    }

    /**
     * 来货报备审核
     *
     * @param id          报备主键
     * @param status      审核结果状态
     * @param auditRemark 不通过原因
     */
    @PutMapping("/pork-report/{id}/audit")
    public void auditPorkReport(@NotNull(message = "报备主键不能为空") @PathVariable("id") Long id,
                                @NotNull(message = "审核结果不能为空") @RequestParam(value = "status") PorkReportEnum status,
                                @RequestParam(value = "auditRemark", defaultValue = "") String auditRemark
                               ) {
        this.porkReportService.auditPorkReport(id, status, auditRemark);
    }

    /**
     * 来货报备撤销
     *
     * @param id 来货报备主键
     */
    @PutMapping("/pork-report/{id}/rescind")
    public void rescindPorkReport(@PathVariable("id") Long id) {
        this.porkReportService.rescindPorkReport(id);
    }

    /**
     * 来货报备详情信息
     *
     * @param id 报备主键
     *
     * @return 报备详情
     */
    @GetMapping("/pork-report/{id}")
    public PorkReportVO getById(@NotNull(message = "报备主键不能为空") @PathVariable("id") Long id) {
        // 获取报备信息
        PorkReport porkReport = this.porkReportService.getById(id);
        PorkReportVO porkReportVO = ObjectUtils.copying(porkReport, PorkReportVO.class);

        // 多线程获取相关信息
        CompletableFuture<SupplierDTO> supplierDTOCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return this.supplierClient.getSupplierById(porkReport.getSupplierId());
        });

        CompletableFuture<Abattoir> abattoirCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return this.abattoirService.getById(porkReport.getAbattoirId());
        });

        CompletableFuture<List<FileVO>> fileVOCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return this.fileClient.getByBusinessKey(porkReport.getId());
        });

        // join()阻塞，知道所有任务执行结束
        CompletableFuture.allOf(supplierDTOCompletableFuture, abattoirCompletableFuture, fileVOCompletableFuture).join();

        try {
            // 获取批发商详情
            SupplierDTO supplierDTO = supplierDTOCompletableFuture.get();
            porkReportVO.setSupplierName(Objects.isNull(supplierDTO) ? "" : supplierDTO.getName());
            porkReportVO.setSupplierCode(Objects.isNull(supplierDTO) ? "" : supplierDTO.getCode());
            porkReportVO.setRiskLevel(Objects.isNull(supplierDTO) ? null : supplierDTO.getRiskLevel());

            // 获取屠宰场信息
            Abattoir abattoir = abattoirCompletableFuture.get();
            porkReportVO.setAbattoirName(Objects.isNull(abattoir) ? "" : abattoir.getName());
            porkReportVO.setAbattoirCode(Objects.isNull(abattoir) ? "" : abattoir.getCode());

            // 获取档位信息（使用多线程时tenant为0导致信息查询不出来）
            if (Objects.nonNull(porkReport.getStallId())) {
                StallDTO stall = this.stallClient.getStallById(porkReport.getStallId());
                porkReportVO.setStallName(Objects.isNull(stall) ? "" : stall.getName());
            }

            // 扩展检疫证明图片
            List<FileVO> files = fileVOCompletableFuture.get();
            porkReportVO.setFiles(files);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 获取审核人信息
        if (Objects.nonNull(porkReport.getAuditorId())) {
            UserDTO user = this.userClient.getUser(porkReport.getAuditorId());
            porkReportVO.setAuditorName(Objects.isNull(user) ? "" : user.getName());
        }
        // 扩展检疫证号
        if (StringUtils.isNotEmpty(porkReport.getPratiqueCode())) {
            porkReportVO.setPratiqueCodes(Arrays.asList(porkReport.getPratiqueCode().split(",")));
        }
        return porkReportVO;
    }

    /**
     * 来货报备分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 来货报备分页信息
     */
    @GetMapping("/pork-report")
    public Pagination<PorkReportVO> pagePorkReport(@Valid PorkReportQueryParam queryParam) {
        // 获取批发商信息
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(queryParam.getKeyword(), tenant);
        if (StringUtils.isNotBlank(queryParam.getKeyword())) {
            queryParam.setSupplierIds(suppliers.stream().map(SupplierDTO::getId).collect(Collectors.toSet()));
        }
        queryParam.setTenant(tenant);
        Page<PorkReport> porkReportPage = this.porkReportService.pagePorkReport(queryParam);
        if (CollectionUtils.isEmpty(porkReportPage.getRecords())) {
            return Pagination.empty();
        }
        List<PorkReportVO> porkReports = ObjectUtils.copying(porkReportPage.getRecords(), PorkReportVO.class);
        // 获取 批发商主键/批发商信息 映射关系
        suppliers = this.supplierClient.listSuppliers("", tenant);
        Map<Long, List<SupplierDTO>> supplierMap = suppliers.stream().collect(Collectors.groupingBy(SupplierDTO::getId));

        // 获取 屠宰场主键/屠宰场信息 映射关系
        Set<Long> abattoirIds = porkReports.stream().map(PorkReportVO::getAbattoirId).collect(Collectors.toSet());
        List<Abattoir> abattoirs = this.abattoirService.getByIds(abattoirIds, SessionContextHolder.getRequester().getTenant());
        Map<Long, List<Abattoir>> abattoirMap = abattoirs.stream().collect(Collectors.groupingBy(Abattoir::getId));

        // 获取 档位主键/档位信息 映射关系
        List<StallDTO> stalls = this.stallClient.getStallList();
        Map<Long, List<StallDTO>> stallMap = stalls.stream().collect(Collectors.groupingBy(StallDTO::getId));

        // 获取 用户/用户信息 映射关系
        Set<Long> auditorIds = porkReports.stream().map(PorkReportVO::getAuditorId).collect(Collectors.toSet());
        List<UserDTO> users = this.userClient.listUserByIds(auditorIds);
        Map<Long, List<UserDTO>> userMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));

        // 构建信息
        porkReports = porkReports.stream().map(porkReport -> {
            if (abattoirMap.containsKey(porkReport.getAbattoirId())) {
                Abattoir abattoir = abattoirMap.get(porkReport.getAbattoirId()).get(0);
                porkReport.setAbattoirName(abattoir.getName());
                porkReport.setAbattoirCode(abattoir.getCode());
            }
            if (supplierMap.containsKey(porkReport.getSupplierId())) {
                SupplierDTO supplier = supplierMap.get(porkReport.getSupplierId()).get(0);
                porkReport.setSupplierName(supplier.getName());
                porkReport.setSupplierCode(supplier.getCode());
                porkReport.setRiskLevel(supplier.getRiskLevel());
            }
            if (stallMap.containsKey(porkReport.getStallId())) {
                StallDTO stall = stallMap.get(porkReport.getStallId()).get(0);
                porkReport.setStallName(stall.getName());
            }
            if (userMap.containsKey(porkReport.getAuditorId())) {
                UserDTO user = userMap.get(porkReport.getAuditorId()).get(0);
                porkReport.setAuditorName(Objects.isNull(user) ? "" : user.getName());
            }
            if (StringUtils.isNotEmpty(porkReport.getPratiqueCode())) {
                // 检疫证号
                porkReport.setPratiqueCodes(Arrays.asList(porkReport.getPratiqueCode().split(",")));
            }
            return porkReport;
        }).collect(Collectors.toList());

        return PagingUtils.convert(porkReportPage, porkReports);
    }

    /**
     * 小程序来货报备
     *
     * @param queryParam 查询参数
     *
     * @return 报备信息
     */
    @GetMapping("/pork-report/mp/page")
    public Pagination<PorkReportVO> pageMpPorkReport(@Valid PorkReportQueryParam queryParam) {
        // 根据当前批发商租户编号来获取当前市场的租户编号
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");

        Set<Long> supplierIds = Stream.of(supplier.getId()).collect(Collectors.toSet());
        queryParam.setSupplierIds(supplierIds);
        queryParam.setTenant(supplier.getTenant());
        // 获取该批发商的报备信息
        Page<PorkReport> page = this.porkReportService.pageMyPorkReport(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }

        // 构建信息
        List<PorkReportVO> porkReports = ObjectUtils.copying(page.getRecords(), PorkReportVO.class);

        // 获取 屠宰场主键/屠宰场信息 映射关系
        Set<Long> abattoirIds = porkReports.stream().map(PorkReportVO::getAbattoirId).collect(Collectors.toSet());
        List<Abattoir> abattoirs = this.abattoirService.getByIds(abattoirIds, supplier.getTenant());
        Map<Long, List<Abattoir>> abattoirMap = abattoirs.stream().collect(Collectors.groupingBy(Abattoir::getId));

        // todo 获取文件信息

        // 构建信息
        porkReports = porkReports.stream().map(porkReport -> {
            if (abattoirMap.containsKey(porkReport.getAbattoirId())) {
                Abattoir abattoir = abattoirMap.get(porkReport.getAbattoirId()).get(0);
                porkReport.setAbattoirName(abattoir.getName());
                porkReport.setAbattoirCode(abattoir.getCode());
            }
            if (porkReport.getStatus() == PorkReportEnum.PASSED) {
                this.extendPorkArrangeCode(porkReport, supplier);
            }
            // todo 需要优化
            porkReport.setFiles(this.fileClient.getByBusinessKey(porkReport.getId()));
            // 检疫证号
            porkReport.setPratiqueCodes(Arrays.asList(porkReport.getPratiqueCode().split(",")));
            return porkReport;
        }).collect(Collectors.toList());

        return PagingUtils.convert(page, porkReports);
    }

    private void extendPorkArrangeCode(PorkReportVO porkReport, SupplierDTO supplier) {
        String content = supplier.getCode() + "," + porkReport.getCode();
        BufferedImage qrcode = BarcodeUtils.qrcode(content, 180, 180);
        porkReport.setBarCode(BarcodeUtils.image2base64(qrcode));
    }

    /**
     * 获取报备单检疫票附件列表
     *
     * @param id 报备单id
     *
     * @return TallQuarantineTicketFileVO
     */
    @GetMapping("/pork-report/{id}/pratique-register/files")
    public List<TallQuarantineTicketFileVO> getPorkReportPratiqueFiles(@PathVariable("id") Long id) {
        PorkReport porkReport = this.porkReportService.getById(id);
        if (Objects.isNull(porkReport)) {
            return Collections.emptyList();
        }
        ThirdMeatTradeConfig thirdMeatTradeConfig = ThirdMeatTradeConfig.getRequestConfig();
        if (Objects.isNull(thirdMeatTradeConfig)) {
            List<FileVO> fileVOS = this.fileClient.getByBusinessKey(porkReport.getId());
            return fileVOS.stream().map(fileVO ->
                                                TallQuarantineTicketFileVO.builder()
                                                        .fileUrl(fileVO.getPath())
                                                        .fileName(fileVO.getName())
                                                        .build())
                    .collect(Collectors.toList());
        }
        List<TallQuarantineTicketFileResponse> files = thirdMeatTradeApiService.getTallQuarantineTicketFile(porkReport.getCode());
        return files.stream().map(response ->
                                          TallQuarantineTicketFileVO.builder()
                                                  .fileUrl(response.getFileUrl())
                                                  .fileName(response.getFileName())
                                                  .build()
                                 ).collect(Collectors.toList());
    }

}
