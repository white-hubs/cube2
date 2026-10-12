package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyExcelTemplateDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyExcelDTO;

import java.util.List;
import java.util.Map;

public interface MerchantCompanyService extends IService<Company> {

    public static final String BUSINESS_TYPE_SECURITY = "store-security";   // 安全协议
    public static final String BUSINESS_TYPE_PHOTO = "store-photo";         // 安全培训
    public static final String BUSINESS_TYPE_LOGO = "store-logo";           // 封面logo|商户灭火器/消火栓

    String save(CompanyDTO companyDTO);

    void update(CompanyDTO companyDTO);

    void remove(String id);

    void importData(String projectId, List<CompanyExcelTemplateDTO> companyExcelDTOS);

    String batchUpdateFile(CompanyDTO dto);

    Map<String, Object> getProgressInfo(String uuid);

    void removeBatch(List<String> ids);

}
