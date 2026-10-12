package com.guhecloud.cube.deliver.project.merchant.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.util.SelectSheetWriteHandler;
import com.guhecloud.cube.biz.mdata.client.StallClient;
import com.guhecloud.cube.biz.mdata.client.dto.StallDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.util.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyExcelTemplateDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyUserDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import com.guhecloud.cube.deliver.project.merchant.service.CompanyUserService;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantCompanyService;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.service.UserDeliverService;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 商户管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantCompanyController {

    private final MerchantCompanyService merchantCompanyService;
    private final SpaceService spaceService;
    private final CompanyUserService companyUserService;
    private final FileClient fileClient;
    private final UserDeliverService userDeliverService;
    private final StallClient stallClient;

    @GetMapping("/user-stall-info")
    public List<CompanyUserDTO> getUserCompanys(LoginUser loginUser) {
        loginUser.setId(SecurityContextHolder.getPrincipal().getId());
        List<CompanyUserDTO> companyUserDTOS = new CompanyUserDTO(companyUserService.lambdaQuery()
                                                                          .eq(CompanyUser::getUserId, loginUser.getId()).list()).list();
        companyUserDTOS.parallelStream().forEach(dto -> {
            Company company = merchantCompanyService.getById(dto.getCompanyId());
            Space space = spaceService.getById(dto.getStallId());
            dto.setCompanyName(company == null ? "" : company.getName());
            dto.setStallName(space == null ? "" : space.getName());
        });
        return companyUserDTOS;
    }

    @GetMapping("/public/user-stall-info")
    public List<CompanyUserDTO> getUserCompanysByPublic(LoginUser loginUser) {
        List<CompanyUserDTO> companyUserDTOS = new CompanyUserDTO(companyUserService.lambdaQuery()
                                                                          .eq(CompanyUser::getUserId, loginUser.getId()).list()).list();
        companyUserDTOS.parallelStream().forEach(dto -> {
            Company company = merchantCompanyService.getById(dto.getCompanyId());
            Space space = spaceService.getById(dto.getStallId());
            dto.setCompanyName(company == null ? "" : company.getName());
            dto.setStallName(space == null ? "" : space.getName());
        });
        return companyUserDTOS;
    }

    @GetMapping("/companies/stallId")
    public CompanyDTO getByStallId(@RequestParam("stallId") String stallId) {
        Company company = merchantCompanyService.getOne(new LambdaQueryWrapper<Company>()
                                                                .eq(Company::getStallId, stallId)
                                                                .last("limit 1"));
        return ObjectUtil.isEmpty(company) ? new CompanyDTO() : this.getById(company.getId());
    }

    @GetMapping("/companies/{id}")
    public CompanyDTO getById(@PathVariable("id") String id) {
        Company company = merchantCompanyService.getById(id);
        if (company == null) {
            return null;
        }
        CompanyDTO companyDTO = new CompanyDTO(company);
        companyDTO.setSecurityFileIds(ObjectUtil.isNotEmpty(company.getSecurityFileId()) ?
                                      Arrays.stream(company.getSecurityFileId().split(","))
                                              .map(s -> Long.parseLong(s.trim()))
                                              .collect(Collectors.toList()) : null);
        companyDTO.setCompanyPhotoFileIds(ObjectUtil.isNotEmpty(company.getCompanyPhotoFileId()) ?
                                          Arrays.stream(company.getCompanyPhotoFileId().split(","))
                                                  .map(s -> Long.parseLong(s.trim()))
                                                  .collect(Collectors.toList()) : null);
        companyDTO.setTrainingPhotoFileIds(ObjectUtil.isNotEmpty(company.getTrainingPhotoFileId()) ?
                                           Arrays.stream(company.getTrainingPhotoFileId().split(","))
                                                   .map(s -> Long.parseLong(s.trim()))
                                                   .collect(Collectors.toList()) : null);
        companyDTO.setBusinessLicenseFileList(ObjectUtil.isNotEmpty(company.getBusinessLicenseFile()) ?
                                              Arrays.stream(company.getBusinessLicenseFile().split(","))
                                                      .map(s -> Long.parseLong(s.trim()))
                                                      .collect(Collectors.toList()) : null);
        companyDTO.setCompanyFileIds(ObjectUtil.isNotEmpty(company.getCompanyFileId()) ?
                                     Arrays.stream(company.getCompanyFileId().split(","))
                                             .map(s -> Long.parseLong(s.trim()))
                                             .collect(Collectors.toList()) : null);
        this.extend(company);
        return this.extend(company);
    }

    @GetMapping("/companies/page")
    public PageResponse<CompanyDTO> getPage(CompanyQueryDTO companyQueryDTO, PageRequest pageRequest) {
        return this.getCompanyByPage(companyQueryDTO, pageRequest);
    }

    @GetMapping("/public/companies/page")
    public PageResponse<CompanyDTO> getPublicPage(CompanyQueryDTO companyQueryDTO, PageRequest pageRequest) {
        return this.getCompanyByPage(companyQueryDTO, pageRequest);
    }

    @GetMapping("/companies/list")
    public List<CompanyDTO> getList(@RequestParam("projectId") String projectId) {
        return new CompanyDTO(merchantCompanyService.lambdaQuery().eq(Company::getProjectId, projectId).list()).list();
    }

    @PostMapping("/companies")
    public String save(@Valid @RequestBody CompanyDTO companyDTO) {
        return merchantCompanyService.save(companyDTO);
    }

    @PutMapping("/companies")
    public void update(@Valid @RequestBody CompanyDTO companyDTO) {
        merchantCompanyService.update(companyDTO);
    }

    @DeleteMapping("/companies/{id}")
    public void remove(@PathVariable("id") String id) {
        merchantCompanyService.remove(id);
    }

    @DeleteMapping("/companies/batch")
    @Deprecated
    public void removeBatch(@RequestParam("ids") List<String> ids) {
        merchantCompanyService.removeBatch(ids);
    }

    /**
     * 批量删除
     *
     * @param ids 商户集合
     */
    @PostMapping("/companies/batch-deleted")
    public void removeBatchByIds(@RequestBody List<String> ids) {
        merchantCompanyService.removeBatch(ids);
    }

    @PostMapping("/companies/batchUpdateFile")
    public String batchUpdateFile(@RequestBody CompanyDTO dto) {
        return merchantCompanyService.batchUpdateFile(dto);
    }

    @GetMapping("/companies/batchUpdateFile/progress")
    public Map<String, Object> getProgressInfo(@RequestParam("uuid") String uuid) {
        return merchantCompanyService.getProgressInfo(uuid);
    }

    @GetMapping("/companies/data-export")
    public void exportData(CompanyQueryDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        List<Company> companies = getCompanyLambdaQueryChainWrapper(queryDTO, null).list();
        List<CompanyExcelDTO> dtos = new CompanyExcelDTO(companies).list();
        this.extendExcelCompany(queryDTO.getProjectId(), dtos);

        ExcelUtils<CompanyExcelDTO> utils = new ExcelUtils<>(CompanyExcelDTO.class);
        utils.createWorkbook();
        try {
            utils.createSheet("商铺信息", dtos);
        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw new ServiceException("导出数据失败", e);
        }
        utils.downLoadExcel("商铺信息", request, response);
    }

    @GetMapping("/companies/template-download")
    public void exportTemplate(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String fileNameEncode = null;
        try {
            fileNameEncode = URLEncoder.encode("商铺信息模板", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameEncode + ExcelTypeEnum.XLSX.getValue());
        List<StallDTO> stallDTOList = stallClient.getStallList();
        Map<Integer, List<String>> dropdownData = new HashMap<>();
        if (ObjectUtil.isNotEmpty(stallDTOList)) {
            List<String> typeName = stallDTOList.stream().map(StallDTO::getName).collect(Collectors.toList());
            dropdownData.put(4, typeName);
        }
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .registerWriteHandler(new SelectSheetWriteHandler(dropdownData))
                    .excelType(ExcelTypeEnum.XLSX)
                    .head(CompanyExcelTemplateDTO.class)
                    .sheet("商铺信息模板")
                    .doWrite(new ArrayList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/companies/data-import")
    public List<ErrorExcelDTO> importData(@RequestParam(value = "projectId") String projectId, @RequestParam("file") MultipartFile file) {
        ExcelUtils<CompanyExcelTemplateDTO> utils = new ExcelUtils<>(CompanyExcelTemplateDTO.class);
        List<CompanyExcelTemplateDTO> companyExcelDTOS;
        try {
            companyExcelDTOS = utils.getDataByExcel(file.getInputStream(), "商铺信息");
        } catch (Exception e) {
            if (ExcelUtils.CHECKDATA_ERROR.equals(e.getMessage())) {
                List<ExcelUtils.ExcelMsg> excelMsgs = utils.getExcelMsgs();
                if (excelMsgs != null) {
                    List<ErrorExcelDTO> collect = excelMsgs.stream()
                            .map(excelMsg -> new ErrorExcelDTO(excelMsg.getRowId(), excelMsg.getColName(), excelMsg.getMsg()))
                            .collect(Collectors.toList());
                    return collect;
                }
            }
            log.error("文件解析异常！", e);
            throw new ServiceException("文件解析异常！");
        }
        if (companyExcelDTOS == null) {
            throw new ServiceException("excel没有数据");
        }
        merchantCompanyService.importData(projectId, companyExcelDTOS);
        return null;
    }

    private PageResponse<CompanyDTO> getCompanyByPage(CompanyQueryDTO companyQueryDTO, PageRequest pageRequest) {
        // 不筛选是否签订安全协议
        Boolean securitySignd = companyQueryDTO.getSecuritySignd();
        if (null == securitySignd) {
            Page<Company> pages = this.getCompanyLambdaQueryChainWrapper(companyQueryDTO, pageRequest)
                    .page(pageRequest.page());
            List<CompanyDTO> companyDTOList = this.extend(pages.getRecords());
            return new PageResponse<>(companyDTOList, pages.getTotal(), pages.getPages());
        } else {
            // 筛选签订了协议的
            List<Company> companyDTOS = this.getCompanyLambdaQueryChainWrapper(companyQueryDTO, pageRequest).list();
            List<CompanyDTO> companyDTOList = this.extend(companyDTOS);
            if (securitySignd) {
                companyDTOList = companyDTOList.parallelStream()
                        .filter(c -> c.getSecurityFileVOS() != null && c.getSecurityFileVOS().size() > 0).collect(Collectors.toList());
            } else {
                companyDTOList = companyDTOList.parallelStream()
                        .filter(c -> c.getSecurityFileVOS() == null || c.getSecurityFileVOS().size() < 1).collect(Collectors.toList());
            }
            return PageResponse.doPage(companyDTOList, pageRequest);
        }
    }

    private LambdaQueryChainWrapper<Company> getCompanyLambdaQueryChainWrapper(CompanyQueryDTO companyQueryDTO, PageRequest pageRequest) {
        return merchantCompanyService.lambdaQuery()
                .and(StringUtils.isNotEmpty(companyQueryDTO.getSearch()), q -> q
                        .like(Company::getName, companyQueryDTO.getSearch())
                        .or()
                        .like(Company::getCode, companyQueryDTO.getSearch())
                        .or()
                        .like(Company::getOwner, companyQueryDTO.getSearch())
                    )
                .eq(ObjectUtil.isNotEmpty(companyQueryDTO.getProjectId()), Company::getProjectId, companyQueryDTO.getProjectId())
                .orderByDesc(pageRequest != null && pageRequest.getOrderItems().isEmpty(), Company::getCreateTime);
    }

    private List<CompanyDTO> extend(List<Company> companyDTOS) {
        Map<String, Company> companyMap = companyDTOS.stream().collect(Collectors.toMap(Company::getId, Company -> Company));
        List<CompanyDTO> companys = Convert.toList(CompanyDTO.class, companyDTOS);
        companys.parallelStream().forEach(ex -> {
            this.extend(ex, companyMap.get(ex.getId()));
        });
        return companys;
    }

    private void extend(CompanyDTO companyDTO, Company company) {
        // 查询商铺关联的空间
        companyDTO.setSpaces(new SpaceDTO(spaceService.lambdaQuery().eq(Space::getCompanyId, companyDTO.getId()).list()).list());
        // 查询关联的照片和安全协议
        // 查询关联的照片和安全协议
        if (ObjectUtil.isNotEmpty(company.getSecurityFileId())) {
            List<Long> securityFileIds = Arrays.stream(company.getSecurityFileId().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setSecurityFileVOS(fileClient.getByIds(securityFileIds));
            companyDTO.setSecurityFileIds(securityFileIds);
        }
        if (ObjectUtil.isNotEmpty(company.getTrainingPhotoFileId())) {
            List<Long> trainingPhotoFileIds = Arrays.stream(company.getTrainingPhotoFileId().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setTrainingPhotoFileVOS(fileClient.getByIds(trainingPhotoFileIds));
            companyDTO.setTrainingPhotoFileIds(trainingPhotoFileIds);
        }
        if (ObjectUtil.isNotEmpty(company.getCompanyPhotoFileId())) {
            List<Long> companyPhotoFileIds = Arrays.stream(company.getCompanyPhotoFileId().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setCompanyPhotoFileVOS(fileClient.getByIds(companyPhotoFileIds));
            companyDTO.setCompanyPhotoFileIds(companyPhotoFileIds);
        }
        if (ObjectUtil.isNotEmpty(company.getBusinessLicenseFile())) {
            List<Long> businessLicenseFileIds = Arrays.stream(company.getBusinessLicenseFile().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setBusinessLicenseFileVOS(fileClient.getByIds(businessLicenseFileIds));
            companyDTO.setBusinessLicenseFileList(businessLicenseFileIds);
        }
        // 查询商户员工列表
        List<CompanyUserDTO> companyUserDTOS = new CompanyUserDTO(companyUserService.lambdaQuery()
                                                                          .eq(CompanyUser::getCompanyId, companyDTO.getId()).list()).list();
        // 去重（多档位的情况）
        companyUserDTOS = companyUserDTOS.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CompanyUserDTO::getUserId))), ArrayList::new));
        companyUserDTOS.parallelStream().forEach(dto -> {
            UserDeliver userInfoDTO = userDeliverService.getById(dto.getUserId());
            if (userInfoDTO != null) {
                dto.setUsername(userInfoDTO.getUsername());
                dto.setNickname(userInfoDTO.getNickname());
            }
        });
        companyDTO.setCompanyUserDTOS(companyUserDTOS);
    }

    private CompanyDTO extend(Company company) {
        CompanyDTO companyDTO = Convert.convert(CompanyDTO.class, company);
        // 查询商铺关联的空间
        companyDTO.setSpaces(new SpaceDTO(spaceService.lambdaQuery().eq(Space::getCompanyId, companyDTO.getId()).list()).list());
        // 查询关联的照片和安全协议
        // 查询关联的照片和安全协议
        if (ObjectUtil.isNotEmpty(company.getSecurityFileId())) {
            List<Long> securityFileIds = Arrays.stream(company.getSecurityFileId().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setSecurityFileVOS(fileClient.getByIds(securityFileIds));
            companyDTO.setSecurityFileIds(securityFileIds);
        }
        if (ObjectUtil.isNotEmpty(company.getTrainingPhotoFileId())) {
            List<Long> trainingPhotoFileIds = Arrays.stream(company.getTrainingPhotoFileId().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setTrainingPhotoFileVOS(fileClient.getByIds(trainingPhotoFileIds));
            companyDTO.setTrainingPhotoFileIds(trainingPhotoFileIds);
        }
        if (ObjectUtil.isNotEmpty(company.getCompanyPhotoFileId())) {
            List<Long> companyPhotoFileIds = Arrays.stream(company.getCompanyPhotoFileId().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setCompanyPhotoFileVOS(fileClient.getByIds(companyPhotoFileIds));
            companyDTO.setCompanyPhotoFileIds(companyPhotoFileIds);
        }
        if (ObjectUtil.isNotEmpty(company.getBusinessLicenseFile())) {
            List<Long> businessLicenseFileIds = Arrays.stream(company.getBusinessLicenseFile().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setBusinessLicenseFileVOS(fileClient.getByIds(businessLicenseFileIds));
            companyDTO.setBusinessLicenseFileList(businessLicenseFileIds);
        }
        if (ObjectUtil.isNotEmpty(company.getCompanyFileId())) {
            List<Long> companyFileIds = Arrays.stream(company.getCompanyFileId().split(","))
                    .map(s -> Long.parseLong(s.trim()))
                    .collect(Collectors.toList());
            companyDTO.setCompanyFileIdsFileVOS(fileClient.getByIds(companyFileIds));
            companyDTO.setCompanyFileIds(companyFileIds);
        }
        // 查询商户员工列表
        List<CompanyUserDTO> companyUserDTOS = new CompanyUserDTO(companyUserService.lambdaQuery()
                                                                          .eq(CompanyUser::getCompanyId, companyDTO.getId()).list()).list();
        // 去重（多档位的情况）
        companyUserDTOS = companyUserDTOS.stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CompanyUserDTO::getUserId))), ArrayList::new));
        companyUserDTOS.parallelStream().forEach(dto -> {
            UserDeliver userInfoDTO = userDeliverService.getById(dto.getUserId());
            if (userInfoDTO != null) {
                dto.setUsername(userInfoDTO.getUsername());
                dto.setNickname(userInfoDTO.getNickname());
            }
        });
        companyDTO.setCompanyUserDTOS(companyUserDTOS);
        return companyDTO;
    }

    private void extendExcelCompany(String projectId, List<CompanyExcelDTO> dtos) {
        List<Space> spaces = spaceService.lambdaQuery().eq(Space::getProjectId, projectId).list();
        dtos.parallelStream().forEach(dto -> {
            List<String> collect = spaces.stream().filter(space -> dto.getId().equals(space.getCompanyId())).map(Space::getName)
                    .collect(Collectors.toList());
            dto.setSpaceNames(String.join(",", collect));
        });
    }

}
