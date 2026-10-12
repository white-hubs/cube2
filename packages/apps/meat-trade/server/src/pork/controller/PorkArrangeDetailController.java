package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrange;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetailLog;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeSaleEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeCheatModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.TallyDataModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeAuditService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailLogService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeDetailLogVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeDetailMpVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeDetailVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 理货片数详情接口
 */
@Slf4j
@RestController("meattrade-porkArrangeDetailController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangeDetailController {

    private final PorkArrangeService porkArrangeService;
    private final PorkArrangeDetailLogService porkArrangeDetailLogService;
    private final PorkReportService porkReportService;
    private final PorkArrangeAuditService porkArrangeAuditService;
    private final PorkArrangeDetailService porkArrangeDetailService;
    private final FileClient fileClient;
    private final SupplierClient supplierClient;

    /**
     * 理货机上报理货数据
     *
     * @param param 上报数据参数
     */
    @PostMapping("/setTallyData")
    public void setTallyData(@Valid @RequestBody TallyDataModifyParam param) {
        this.porkArrangeDetailService.addTallyData(param);
    }

    /**
     * 修改理货嫌疑状态
     *
     * @param param
     */
    @PostMapping("/pork-arrange-detail/modify-cheat")
    public void updatePorkArrangeDetailCheat(@Valid @RequestBody PorkArrangeCheatModifyParam param) {
        this.porkArrangeDetailService.updatePorkArrangeDetailCheat(param);
    }

    /**
     * 理货审核详情根据报备号获取理货信息详情
     *
     * @param porkReportCode 报备号
     *
     * @return 理货详情信息
     */
    @GetMapping("/pork-arrange-detail/{porkReportCode}/list")
    public List<PorkArrangeDetailVO> listPorkArrangeDetails(@PathVariable("porkReportCode") String porkReportCode) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.getByPorkReportCode(porkReportCode, tenant);
        List<PorkArrangeDetailVO> porkArrangeDetailVOS = ObjectUtils.copying(porkArrangeDetails, PorkArrangeDetailVO.class);
        //获取所有文件id
        List<Long> fileIds = new ArrayList<>();
        porkArrangeDetailVOS.stream().filter(detail -> StringUtils.isNotEmpty(detail.getTransferFiles()))
                .forEach(detail -> {
                    fileIds.addAll(Arrays.stream(detail.getTransferFiles().split(",")).map(Long::valueOf).collect(Collectors.toList()));
                });
        FileParam fileParam = new FileParam();
        fileParam.setIds(fileIds);
        List<FileVO> fileVOS = this.fileClient.getFileList(fileParam);
        for (PorkArrangeDetailVO detail : porkArrangeDetailVOS) {
            if (StringUtils.isNotEmpty(detail.getTransferFiles())) {
                List<Long> files = Arrays.stream(detail.getTransferFiles().split(",")).map(Long::valueOf)
                        .collect(Collectors.toList());
                List<FileVO> porkDetailFiles = fileVOS.stream().filter(fileVO -> files.contains(fileVO.getId()))
                        .collect(Collectors.toList());
                detail.setPicPaths(porkDetailFiles.stream().map(FileVO::getPath).collect(Collectors.joining(",")));
                detail.setPicFilePaths(porkDetailFiles.stream().map(FileVO::getPath).collect(Collectors.toList()));
            } else if (StringUtils.isNotEmpty(detail.getPicPaths())) {
                detail.setPicFilePaths(Arrays.stream(detail.getPicPaths().split(",")).collect(Collectors.toList()));
            }
        }
        return porkArrangeDetailVOS;
    }

    /**
     * 理货审核详情根据报备号获取理货信息详情
     *
     * @param porkReportCode 报备号
     *
     * @return 理货详情信息
     */
    @GetMapping("/pork-arrange-detail/{porkReportCode}/page")
    public Pagination<PorkArrangeDetailVO> getPorkArrangeDetailsPage(@PathVariable("porkReportCode") String porkReportCode, Paging paging) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Page<PorkArrangeDetail> porkArrangeDetailPage = this.porkArrangeDetailService
                .listByPorkReportCodes(Collections.singletonList(porkReportCode), null, tenant, paging);
        if (CollectionUtils.isEmpty(porkArrangeDetailPage.getRecords())) {
            return Pagination.empty();
        }
        List<PorkArrangeDetailVO> porkArrangeDetailVOS = ObjectUtils.copying(porkArrangeDetailPage.getRecords(), PorkArrangeDetailVO.class);
        porkArrangeDetailVOS.forEach(this::extendFileInfo);
        return PagingUtils.convert(porkArrangeDetailPage, porkArrangeDetailVOS);
    }

    /**
     * 根据报备号获取理货信息详情
     *
     * @param code 报备号
     *
     * @return 理货信息详情
     */
    @GetMapping("/pork-arrange-detail/{code}/pork-report")
    public PorkArrangeDetailMpVO getMpPorkArrangeDetail(@PathVariable("code") String code) {
        // 获取批发商信息
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        Integer tenant = supplier.getTenant();

        // 获取理货信息
        PorkReport porkReport = this.porkReportService.getByCode(code, tenant);
        Assert.isNotFound(porkReport, "不存在报备信息!");
        PorkArrangeDetailMpVO detailMpVO = new PorkArrangeDetailMpVO();

        // 获取理货详情信息
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listPorkArrangeDetails(code, tenant);
        if (CollectionUtils.isEmpty(porkArrangeDetails)) {
            // 获取理货状态
            return getEmptyPorkArrangeDetail(supplier, porkReport, detailMpVO);
        }
        // 获取理货审核信息
        return extendPorkArrangeDetailMpVO(supplier, porkReport, detailMpVO, porkArrangeDetails);
    }

    /**
     * 根据报备号获取理货信息详情
     *
     * @param porkReportId 报备主键
     *
     * @return 理货信息详情
     */
    @GetMapping("/pork-arrange-detail/{porkReportId}/pork-report/id")
    public PorkArrangeDetailMpVO getMpPorkArrangeDetailByPorkReport(@PathVariable("porkReportId") Long porkReportId) {
        // 获取批发商信息
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        Integer tenant = supplier.getTenant();

        // 获取理货信息
        PorkReport porkReport = this.porkReportService.getById(porkReportId);
        Assert.isNotFound(porkReport, "不存在报备信息!");
        PorkArrangeDetailMpVO detailMpVO = new PorkArrangeDetailMpVO();

        // 获取理货详情信息
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listPorkArrangeDetails(porkReport.getCode(), tenant);
        if (CollectionUtils.isEmpty(porkArrangeDetails)) {
            // 获取理货状态
            return getEmptyPorkArrangeDetail(supplier, porkReport, detailMpVO);
        }
        return extendPorkArrangeDetailMpVO(supplier, porkReport, detailMpVO, porkArrangeDetails);

    }

    /**
     * 获取当前用户未出售商品(小程序)
     *
     * @return 理货数据详情集合
     */
    @GetMapping("/pork-arrange-detail/mp/my-unsold-details")
    public Pagination<PorkArrangeDetailVO> getMyUnSoldPorkArrangeDetail(PorkArrangeQueryParam param) {
        // 获取当前批发商信息
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        param.setTenant(supplier.getTenant());
        param.setSupplierId(supplier.getId());
        param.setAuditEnum(PorkArrangeAuditEnum.PASSED);
        Page<PorkArrangeDetail> page = porkArrangeDetailService.pageByComplex(param);
        List<PorkArrangeDetailVO> porkArrangeDetails = ObjectUtils.copying(page.getRecords(), PorkArrangeDetailVO.class);
        return PagingUtils.convert(page, porkArrangeDetails);
    }

    /**
     * 批次挂钩理货记录信息
     *
     * @param porkReportCode
     * @param label
     *
     * @return
     */
    @GetMapping("/pork-arrange-detail/log-records")
    public List<PorkArrangeDetailLogVO> getPorkArrangeDetailLogs(@RequestParam String porkReportCode, @RequestParam String label) {
        List<PorkArrangeDetailLog> porkArrangeDetailLogs = this.porkArrangeDetailLogService.getPorkArrangeDetailLogs(porkReportCode, label);
        if (CollectionUtils.isEmpty(porkArrangeDetailLogs)) {
            return Collections.emptyList();
        }
        List<PorkArrangeDetailLogVO> porkArrangeDetailLogVOS = ObjectUtils.copying(porkArrangeDetailLogs, PorkArrangeDetailLogVO.class);
        porkArrangeDetailLogVOS.forEach(this::extendFileInfo);
        return porkArrangeDetailLogVOS;
    }

    private PorkArrangeDetailMpVO getEmptyPorkArrangeDetail(SupplierDTO supplier, PorkReport porkReport, PorkArrangeDetailMpVO detailMpVO) {
        PorkArrange porkArrange = this.porkArrangeService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        detailMpVO.setStatus(porkArrange == null ? null : porkArrange.getStatus());
        detailMpVO.setSupplierId(supplier.getId());
        detailMpVO.setPorkReportCode(porkReport.getCode());
        detailMpVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
        detailMpVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
        detailMpVO.setFreshLabel(porkReport.getFreshLabel());
        detailMpVO.setPorkArrangeTime(null);
        detailMpVO.setPorkArrangeSlices(0);
        detailMpVO.setPorkArrangeWeight(BigDecimal.ZERO);
        return detailMpVO;
    }

    private PorkArrangeDetailMpVO extendPorkArrangeDetailMpVO(SupplierDTO supplier, PorkReport porkReport, PorkArrangeDetailMpVO detailMpVO,
                                                              List<PorkArrangeDetail> porkArrangeDetails
                                                             ) {
        // 获取理货审核信息
        PorkArrangeAudit porkArrangeAudit = this.porkArrangeAuditService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        detailMpVO.setAuditStatus(porkArrangeAudit.getAuditStatus());
        detailMpVO.setPorkReportCode(porkReport.getCode());
        detailMpVO.setRemark(porkArrangeAudit.getRemark());

        //报备信息
        detailMpVO.setGoodsCategoryId(porkReport.getGoodsCategoryId());
        detailMpVO.setGoodsCategoryName(porkReport.getGoodsCategoryName());
        detailMpVO.setFreshLabel(porkReport.getFreshLabel());

        // 获取理货信息
        PorkArrange porkArrange = porkArrangeService.getByPorkReport(porkReport.getId(), supplier.getTenant());
        detailMpVO.setPorkArrangeSlices(porkArrange.getSlices());
        detailMpVO.setStatus(porkArrange.getStatus());
        detailMpVO.setPorkArrangeWeight(porkArrange.getWeight());
        detailMpVO.setPorkArrangeTime(porkArrange.getPorkArrangeTime());
        // 构建信息
        List<PorkArrangeDetailVO> details = ObjectUtils.copying(porkArrangeDetails, PorkArrangeDetailVO.class);
        details = details.stream().peek(detail -> {
            if (StringUtils.isEmpty(detail.getPrice())) {
                detail.setSaleStatus(PorkArrangeSaleEnum.NOT_SOLD);
            }
            extendFileInfo(detail);
        }).collect(Collectors.toList());
        detailMpVO.setRecords(details);
        return detailMpVO;
    }

    private void extendFileInfo(PorkArrangeDetailVO detail) {
        if (StringUtils.isNotEmpty(detail.getTransferFiles())) {
            List<Long> fileIds = Arrays.stream(detail.getTransferFiles().split(",")).map(fileId -> Long.valueOf(fileId))
                    .collect(Collectors.toList());
            List<FileVO> fileVOS = this.fileClient.getByIds(fileIds);
            detail.setPicPaths(fileVOS.stream().map(FileVO::getPath).collect(Collectors.joining(",")));
            detail.setPicFilePaths(fileVOS.stream().map(FileVO::getPath).collect(Collectors.toList()));
        } else if (StringUtils.isNotEmpty(detail.getPicPaths())) {
            detail.setPicFilePaths(Arrays.stream(detail.getPicPaths().split(",")).collect(Collectors.toList()));
        }
    }

    private void extendFileInfo(PorkArrangeDetailLogVO detail) {
        if (StringUtils.isNotEmpty(detail.getTransferFiles())) {
            List<Long> fileIds = Arrays.stream(detail.getTransferFiles().split(",")).map(fileId -> Long.valueOf(fileId))
                    .collect(Collectors.toList());
            List<FileVO> fileVOS = this.fileClient.getByIds(fileIds);
            detail.setPicPaths(fileVOS.stream().map(FileVO::getPath).collect(Collectors.joining(",")));
            detail.setPicFilePaths(fileVOS.stream().map(FileVO::getPath).collect(Collectors.toList()));
        } else if (StringUtils.isNotEmpty(detail.getPicPaths())) {
            detail.setPicFilePaths(Arrays.stream(detail.getPicPaths().split(",")).collect(Collectors.toList()));
        }
    }
}
