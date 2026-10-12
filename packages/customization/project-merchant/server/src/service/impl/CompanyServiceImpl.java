package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyMapper;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyService;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyUserService;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.utils.CompanyUploadFileUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    private final CompanyMapper companyMapper;
    private final SpaceService spaceService;
    private final CompanyUserService companyUserService;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    private final CompanyUploadFileUtils uploadFileUtils;

    @Override
    public String save(CompanyDTO companyDTO) {
        Company company = companyDTO.convert();
        company.setCode(BizCodeUtils.generate("SP"));
        companyMapper.insert(company);
        companyDTO.setId(company.getId());
        this.saveCompanySpace(companyDTO);
        this.markFiles(companyDTO);
        return companyDTO.getId();
    }

    @Override
    public void update(CompanyDTO companyDTO) {
        // 删除原有的档位关联
        this.removeCompanySpace(companyDTO);
        // 保存新的档位关联
        this.saveCompanySpace(companyDTO);
        this.updateFiles(companyDTO);
        companyMapper.updateById(companyDTO.convert());
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
    public void importData(String projectId, List<CompanyExcelDTO> companyExcelDTOS) {
        List<ErrorExcelDTO> dtos = new ArrayList<>();
        int i = 2;
        for (CompanyExcelDTO companyExcelDTO : companyExcelDTOS) {
            companyExcelDTO.setName(companyExcelDTO.getName().replace("\n", ""));
            companyExcelDTO.setSpaceNames(companyExcelDTO.getSpaceNames().replace("\n", ""));

            Company company = companyExcelDTO.convert();
            //id不为空，修改;否则 新增
            if (ObjectUtil.isNotEmpty(companyExcelDTO.getId())) {
                Company companyById = companyMapper.selectById(companyExcelDTO.getId());
                if (companyById != null) {
                    companyMapper.updateById(company);
                } else {
                    dtos.add(new ErrorExcelDTO(i, "ID", "ID不存在"));
                }
            } else {
                Company companyByName = companyMapper.selectOne(Wrappers.<Company>lambdaQuery().eq(Company::getProjectId, projectId)
                                                                        .eq(Company::getName, companyExcelDTO.getName()));
                if (companyByName == null) {
                    company.setProjectId(projectId);
                    company.setCode(BizCodeUtils.generate("SP"));
                    companyMapper.insert(company);
                } else {
                    dtos.add(new ErrorExcelDTO(i, "*商铺名称", "名称已存在"));
                }
            }

            /*Company companyByName = companyMapper.selectOne(Wrappers.<Company>lambdaQuery().eq(Company::getProjectId, projectId).eq(Company::getName, companyExcelDTO.getName()));
            Company company = companyExcelDTO.convert();
            if (companyByName != null) {
                company.setId(companyByName.getId());
                companyMapper.updateById(company);
            } else {
                company.setProjectId(projectId);
                company.setCode(BizCodeUtils.generate("SP"));
                companyMapper.insert(company);
            }*/
            this.saveCompanyIdToSpace(companyExcelDTO.getSpaceNames(), company.getId(), company.getName());
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
        if (ObjectUtil.isEmpty(spaceNames)) {
            return;
        }
        String replace = spaceNames.replace("\n\t", "");
        String[] names = replace.split(",");
        for (String name : names) {
            Space space = spaceService.lambdaQuery().eq(Space::getName, name).one();
            if (space == null) {
                break;
            }
            if (ObjectUtil.isNotEmpty(space.getCompanyId())) {
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

        if (dto.getSecurityFileIds() != null && dto.getSecurityFileIds().size() > 0) {
            //            for (String fileId : dto.getSecurityFileIds()) {
            //
            //            }
        }
        if (dto.getTrainingPhotoFileIds() != null && dto.getTrainingPhotoFileIds().size() > 0) {
            //            for (String fileId : dto.getTrainingPhotoFileIds()) {
            //                fileApi.mark(fileId, BUSINESS_TYPE_PHOTO, dto.getId());
            //            }
        }
        if (dto.getCompanyPhotoFileIds() != null && dto.getCompanyPhotoFileIds().size() > 0) {
            //            for (String fileId : dto.getCompanyPhotoFileIds()) {
            //                fileApi.mark(fileId, BUSINESS_TYPE_LOGO, dto.getId());
            //            }
        }
    }

    private void updateFiles(CompanyDTO dto) {
        //        fileApi.updateFiles(dto.getSecurityFileIds(), BUSINESS_TYPE_SECURITY, dto.getId());
        //        fileApi.updateFiles(dto.getTrainingPhotoFileIds(), BUSINESS_TYPE_PHOTO, dto.getId());
        //        fileApi.updateFiles(dto.getCompanyPhotoFileIds(), BUSINESS_TYPE_LOGO, dto.getId());
    }

}
