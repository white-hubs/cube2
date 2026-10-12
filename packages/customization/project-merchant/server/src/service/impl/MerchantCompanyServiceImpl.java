package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyExcelTemplateDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyMapper;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyUserService;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantCompanyService;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.utils.CompanyUploadFileUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantCompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements MerchantCompanyService {

    private final CompanyMapper companyMapper;
    private final SpaceService spaceService;
    private final CompanyUserService companyUserService;

    private final CompanyUploadFileUtils uploadFileUtils;
    private final StallClient stallClient;

    @Override
    public String save(CompanyDTO companyDTO) {
        Company company = companyDTO.convert();
        company.setCompanyPhotoFileId(
                ObjectUtil.isNotEmpty(companyDTO.getCompanyPhotoFileIds()) ? StrUtil.join(",", companyDTO.getCompanyPhotoFileIds()) : "");
        company.setSecurityFileId(
                ObjectUtil.isNotEmpty(companyDTO.getSecurityFileIds()) ? StrUtil.join(",", companyDTO.getSecurityFileIds()) : "");
        company.setTrainingPhotoFileId(
                ObjectUtil.isNotEmpty(companyDTO.getTrainingPhotoFileIds()) ? StrUtil.join(",", companyDTO.getTrainingPhotoFileIds()) : "");
        company.setBusinessLicenseFile(
                ObjectUtil.isNotEmpty(companyDTO.getBusinessLicenseFileList()) ? StrUtil.join(",", companyDTO.getBusinessLicenseFileList())
                                                                               : "");
        company.setCompanyFileId(
                ObjectUtil.isNotEmpty(companyDTO.getCompanyFileIds()) ? StrUtil.join(",", companyDTO.getCompanyFileIds()) : "");
        company.setCode(BizCodeUtils.generate("SP"));
        company.setCreateBy(SecurityContextHolder.getPrincipal().getName());
        company.setCreateTime(LocalDateTime.now());
        if (Boolean.TRUE.equals(companyDTO.getLongTerm())) {
            company.setEndTime(null);
        }
        companyMapper.insert(company);
        companyDTO.setId(company.getId());
        this.saveCompanySpace(companyDTO);
        this.markFiles(companyDTO);
        return companyDTO.getId();
    }

    @Override
    public void update(CompanyDTO companyDTO) {
        Company sourceCompany = this.companyMapper.selectById(companyDTO.getId());
        Assert.isNotFound(sourceCompany, "商户信息不存在");
        Company company = companyDTO.convert();
        // 删除原有的档位关联
        this.removeCompanySpace(companyDTO);
        // 保存新的档位关联
        this.saveCompanySpace(companyDTO);
        company.setCompanyPhotoFileId(
                ObjectUtil.isNotEmpty(companyDTO.getCompanyPhotoFileIds()) ? StrUtil.join(",", companyDTO.getCompanyPhotoFileIds()) : null);
        company.setSecurityFileId(
                ObjectUtil.isNotEmpty(companyDTO.getSecurityFileIds()) ? StrUtil.join(",", companyDTO.getSecurityFileIds()) : "");
        company.setTrainingPhotoFileId(
                ObjectUtil.isNotEmpty(companyDTO.getTrainingPhotoFileIds()) ? StrUtil.join(",", companyDTO.getTrainingPhotoFileIds())
                                                                            : null);
        company.setBusinessLicenseFile(
                ObjectUtil.isNotEmpty(companyDTO.getBusinessLicenseFileList()) ? StrUtil.join(",", companyDTO.getBusinessLicenseFileList())
                                                                               : "");
        company.setCompanyFileId(
                ObjectUtil.isNotEmpty(companyDTO.getCompanyFileIds()) ? StrUtil.join(",", companyDTO.getCompanyFileIds()) : "");
        company.setId(sourceCompany.getId());
        company.setDeleted(false);
        company.setCreateBy(sourceCompany.getCreateBy());
        company.setCreateTime(sourceCompany.getCreateTime());
        if (Boolean.TRUE.equals(companyDTO.getLongTerm())) {
            company.setEndTime(null);
        }
        if (ObjectUtil.isEmpty(company.getExpirationNotice())) {
            company.setExpirationNotice(sourceCompany.getExpirationNotice());
        }
        companyMapper.updateById(company);
    }

    @Override
    public void remove(String id) {
        List<CompanyUser> list = companyUserService.lambdaQuery().eq(CompanyUser::getCompanyId, id).list();
        if (list.size() > 0) {
            throw new ServiceException("请先移除该商户下的所有用户!");
        }
        // 将档位中的关联商铺ID置为空
        spaceService.lambdaUpdate().eq(Space::getCompanyId, id).set(Space::getCompanyId, "").update();
        // 删除商铺
        companyMapper.deleteById(id);
    }

    @Override
    public void removeBatch(List<String> ids) {
        if (ids == null || ids.size() < 1) {
            return;
        }
        ids.forEach(this::remove);
    }

    @Override
    public void importData(String projectId, List<CompanyExcelTemplateDTO> companyExcelDTOS) {
        List<ErrorExcelDTO> dtos = new ArrayList<>();
        int i = 2;
        List<StallDTO> stallDTOList = stallClient.getStallList();
        Map<String, StallDTO> stallMap = new HashMap<>();
        stallDTOList.stream().forEach(ex -> {
            if (ObjectUtil.isNotEmpty(ex.getName())) {
                stallMap.put(ex.getName(), ex);
            }
        });
        for (CompanyExcelTemplateDTO companyExcelDTO : companyExcelDTOS) {
            Company company = Convert.convert(Company.class, companyExcelDTO);
            if (ObjectUtil.isNotEmpty(company.getStallName())) {
                StallDTO stallDTO = stallMap.get(company.getStallName());
                if (ObjectUtil.isNotEmpty(stallDTO)) {
                    company.setStallId(stallDTO.getId());
                }
            }
            company.setProjectId(projectId);
            company.setCode(BizCodeUtils.generate("SP"));
            company.setCreateTime(LocalDateTime.now());
            company.setCreateBy(SecurityContextHolder.getPrincipal().getName());
            companyMapper.insert(company);
            i++;
        }
        if (dtos.size() > 0) {
            throw new ServiceException("导入数据异常");
        }
    }

    @Override
    public String batchUpdateFile(CompanyDTO dto) {
        String uuid = UUID.randomUUID().toString();
        uploadFileUtils.batchUpload(dto, uuid);
        return uuid;
    }

    @Override
    public Map<String, Object> getProgressInfo(String uuid) {
        Map<String, Object> map = new HashMap<>();
        int progressValue = uploadFileUtils.getProgressValue(uuid);
        map.put("progress", progressValue);
        map.put("errors", uploadFileUtils.getErrorsValue(uuid));

        if (progressValue == 100) {
            uploadFileUtils.deleteByUuid(uuid);
        }

        return map;
    }

    private void saveCompanyIdToSpace(String spaceNames, String companyId, String companyName) {
        if (StringUtils.isEmpty(spaceNames)) {
            return;
        }
        String replace = spaceNames.replace("\n\t", "");
        String[] names = replace.split(",");
        for (String name : names) {
            Space space = spaceService.lambdaQuery().eq(Space::getName, name).one();
            if (space == null) {
                break;
            }
            if (StringUtils.isNotEmpty(space.getCompanyId())) {
                break;
            }
            space.setCompanyId(companyId);
            space.setCompanyName(companyName);
            spaceService.updateById(space);

        }
    }

    private void saveCompanySpace(CompanyDTO companyDTO) {
        companyDTO.getSpaceIds().forEach(spaceId -> {
            if (spaceService.getById(spaceId) != null) {
                spaceService.lambdaUpdate().eq(Space::getId, spaceId).set(Space::getCompanyId, companyDTO.getId())
                        .set(Space::getCompanyName, companyDTO.getName()).update();
            }
        });
    }

    private void removeCompanySpace(CompanyDTO companyDTO) {
        spaceService.lambdaUpdate().eq(Space::getCompanyId, companyDTO.getId()).set(Space::getCompanyId, "").set(Space::getCompanyName, "")
                .update();
    }

    private void markFiles(CompanyDTO dto) {

        //        if (dto.getSecurityFileIds() != null && dto.getSecurityFileIds().size() > 0) {
        //            for (String fileId : dto.getSecurityFileIds()) {
        //                fileApi.mark(fileId, BUSINESS_TYPE_SECURITY, dto.getId());
        //            }
        //        }
        //        if (dto.getTrainingPhotoFileIds() != null && dto.getTrainingPhotoFileIds().size() > 0) {
        //            for (String fileId : dto.getTrainingPhotoFileIds()) {
        //                fileApi.mark(fileId, BUSINESS_TYPE_PHOTO, dto.getId());
        //            }
        //        }
        //        if (dto.getCompanyPhotoFileIds() != null && dto.getCompanyPhotoFileIds().size() > 0) {
        //            for (String fileId : dto.getCompanyPhotoFileIds()) {
        //                fileApi.mark(fileId, BUSINESS_TYPE_LOGO, dto.getId());
        //            }
        //        }
    }

}
