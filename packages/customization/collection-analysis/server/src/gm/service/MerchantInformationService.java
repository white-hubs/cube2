package com.guhecloud.cube.biz.collection.analysis.gm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.PurchaserExcelDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.WholesalerExcelDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.MerchantInformationParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.param.MerchantInformationQueryParam;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.MerchantInformationResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.PurchaserErrorExcelResult;
import com.guhecloud.cube.biz.collection.analysis.gm.model.result.WholesalerErrorExcelResult;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.MerchantInformationPO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 商户画像
 */
public interface MerchantInformationService extends IService<MerchantInformationPO> {
    @Transactional(rollbackFor = Exception.class)
    Long saveOrUpdateMerchantInformation(MerchantInformationParam param);

    void removeMerchantInformation(Long id);

    Pagination<MerchantInformationResult> pageMerchantInformation(MerchantInformationQueryParam param);

    MerchantInformationResult detailMerchantInformation(Long id);

    void downloadWholesalerTemplate(List<WholesalerExcelDTO> dataList, HttpServletRequest request, HttpServletResponse response);

    void downloadPurchaserTemplate(List<PurchaserExcelDTO> dataList, HttpServletRequest request, HttpServletResponse response);

    @Transactional(rollbackFor = Exception.class)
    WholesalerErrorExcelResult importWholesalerTemplate(Boolean replaceBool, MultipartFile file);

    @Transactional(rollbackFor = Exception.class)
    PurchaserErrorExcelResult importPurchaserTemplate(Boolean replaceBool, MultipartFile file);
}
