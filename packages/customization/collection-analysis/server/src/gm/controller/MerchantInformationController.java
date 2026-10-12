package com.guhecloud.cube.biz.collection.analysis.gm.controller;

import com.guhecloud.cube.biz.collection.analysis.gm.model.param.MerchantInformationParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.MerchantInformationQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.PurchaserExcelErrorParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.WholesalerExcelErrorParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.MerchantInformationResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.MerchantStatResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.PurchaserErrorExcelResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.WholesalerErrorExcelResult;
import com.guhecloud.cube.biz.collection.analysis.gm.service.MerchantInformationService;
import com.guhecloud.cube.biz.collection.analysis.gm.service.MerchantStatisticsService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * 用户画像控制层
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantInformationController {

    private final MerchantInformationService merchantInformationService;
    private final MerchantStatisticsService merchantStatisticsService;

    /**
     * 新增商户
     *
     * @param param 商户对象
     */
    @PostMapping("/merchant-information/add")
    public Long addMerchantInformation(@RequestBody @Valid MerchantInformationParam param) {
        return merchantInformationService.saveOrUpdateMerchantInformation(param);
    }

    /**
     * 编辑商户
     *
     * @param param 对象
     */
    @PutMapping("/merchant-information/edit")
    public Long editMerchantInformation(@RequestBody @Valid MerchantInformationParam param) {
        return merchantInformationService.saveOrUpdateMerchantInformation(param);
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @DeleteMapping("/merchant-information/remove")
    public void removeMerchantInformation(Long id) {
        merchantInformationService.removeMerchantInformation(id);
    }

    /**
     * 分页
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/merchant-information/page")
    public Pagination<MerchantInformationResult> pageMerchantInformation(MerchantInformationQueryParam param) {
        return merchantInformationService.pageMerchantInformation(param);
    }

    /**
     * 详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @GetMapping("/merchant-information/detail")
    public MerchantInformationResult detailMerchantInformation(Long id) {
        return merchantInformationService.detailMerchantInformation(id);
    }

    /**
     * 下载批发商模板
     *
     * @param request  请求对象
     * @param response 返回对象
     */
    @GetMapping("/merchant-information/template/download-wholesaler")
    public void downloadWholesalerTemplate(HttpServletRequest request, HttpServletResponse response) {
        merchantInformationService.downloadWholesalerTemplate(new ArrayList<>(), request, response);
    }

    /**
     * 下载采购商商模板
     *
     * @param request  请求对象
     * @param response 返回对象
     */
    @GetMapping("/merchant-information/template/download-purchaser")
    public void downloadPurchaserTemplate(HttpServletRequest request, HttpServletResponse response) {
        merchantInformationService.downloadPurchaserTemplate(new ArrayList<>(), request, response);
    }

    /**
     * 导入批发商模板数据
     *
     * @param replaceBool 是否直接覆盖相同数据
     * @param file        文件
     *
     * @return 失败结果
     */
    @PostMapping("/merchant-information/template/import-wholesaler")
    public WholesalerErrorExcelResult importWholesalerTemplate(@RequestParam(value = "replaceBool", required = false) Boolean replaceBool,
                                                               @RequestParam("file") MultipartFile file
                                                              ) {
        return merchantInformationService.importWholesalerTemplate(replaceBool, file);
    }

    /**
     * 导入采购商模板数据
     *
     * @param replaceBool 是否直接覆盖相同数据
     * @param file        文件
     *
     * @return 失败结果
     */
    @PostMapping("/merchant-information/template/import-purchaser")
    public PurchaserErrorExcelResult importPurchaserTemplate(@RequestParam(value = "replaceBool", required = false) Boolean replaceBool,
                                                             @RequestParam("file") MultipartFile file
                                                            ) {
        return merchantInformationService.importPurchaserTemplate(replaceBool, file);
    }

    /**
     * 下载批发商异常数据
     *
     * @param param    数据
     * @param request  请求对象
     * @param response 返回对象
     */
    @PostMapping("/merchant-information/download/wholesaler-error")
    public void downloadWholesalerError(@RequestBody WholesalerExcelErrorParam param, HttpServletRequest request,
                                        HttpServletResponse response
                                       ) {
        merchantInformationService.downloadWholesalerTemplate(param.getDataList(), request, response);
    }

    /**
     * 下载采购商异常数据
     *
     * @param param    数据
     * @param request  请求对象
     * @param response 返回对象
     */
    @PostMapping("/merchant-information/download/purchaser-error")
    public void downloadPurchaserError(@RequestBody PurchaserExcelErrorParam param, HttpServletRequest request,
                                       HttpServletResponse response
                                      ) {
        merchantInformationService.downloadPurchaserTemplate(param.getDataList(), request, response);
    }

    /**
     * 获取商户统计数据
     *
     * @return 统计
     */
    @GetMapping("/merchant-information/stat")
    public MerchantStatResult getMerchantStat() {
        return merchantStatisticsService.getMerchantStat();
    }

}
