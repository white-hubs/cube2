package com.guhecloud.cube.deliver.project.merchant.controller;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.CompanyDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SpaceQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AreaDeliver;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.enums.ProjectMerchantAreaEnum;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantAreaService;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantCompanyService;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceController {

    private final SpaceService spaceService;
    private final MerchantAreaService merchantAreaService;
    //    private final BimInfoService bBimInfoService;
    private final MerchantCompanyService merchantCompanyService;
    //    private final SysDictDataApi sysDictDataApi;

    @GetMapping("/spaces/{id}")
    public SpaceDTO getById(@PathVariable("id") String id) {
        SpaceDTO spaceDTO = new SpaceDTO(spaceService.getById(id));
        this.extend(spaceDTO);
        return spaceDTO;
    }

    @PostMapping("/spaces/getByIds")
    public List<SpaceDTO> getByIds(@RequestBody List<String> ids) {
        List<SpaceDTO> dtos = null;
        if (ids != null && ids.size() > 0) {
            List<Space> spaces = spaceService.lambdaQuery().in(Space::getId, ids).list();
            if (spaces.size() > 0) {
                dtos = new SpaceDTO(spaces).list();
                this.extend(dtos);
            }
        }
        return dtos;
    }

    @GetMapping("/spaces")
    public SpaceDTO getByName(@RequestParam("projectId") String projectId, @RequestParam("name") String name) {
        List<Space> spaces = spaceService.lambdaQuery().eq(Space::getProjectId, projectId).like(Space::getName, name).list();
        if (spaces.size() > 0) {
            Space space = spaces.get(0);
            for (Space temp : spaces) {
                if (name.equals(temp.getName())) {
                    space = temp;
                    break;
                }
            }
            SpaceDTO spaceDTO = new SpaceDTO(space);
            this.extend(spaceDTO);
            return spaceDTO;
        }
        return null;
    }

    @GetMapping("/public/spaces/page")
    public PageResponse<SpaceDTO> getPagePublic(SpaceQueryDTO spaceQueryDTO, PageRequest pageRequest) {
        spaceQueryDTO.setAttribute(SpaceService.ATTR_BOOTH);
        spaceQueryDTO.setHasCompanyId(true);
        List<Space> spaces = this.getSpaceLambdaQueryChainWrapper(spaceQueryDTO, pageRequest)
                .list();
        List<SpaceDTO> spaceDTOS = new SpaceDTO(spaces).list();
        spaceDTOS = this.extendCompanyDTO(spaceDTOS);
        return PageResponse.doPage(spaceDTOS, pageRequest);
    }

    @GetMapping("/spaces/page")
    public PageResponse<SpaceDTO> getPage(SpaceQueryDTO spaceQueryDTO, PageRequest pageRequest) {
        Page<Space> page = this.getSpaceLambdaQueryChainWrapper(spaceQueryDTO, pageRequest)
                .page(pageRequest.page());
        List<SpaceDTO> spaceDTOS = new SpaceDTO(page.getRecords()).list();
        this.extend(spaceDTOS);
        return new PageResponse<>(spaceDTOS, page.getTotal(), page.getPages());
    }

    @GetMapping("/spaces/list")
    public List<SpaceDTO> getList(SpaceQueryDTO spaceQueryDTO) {
        List<Space> spaces = this.getSpaceLambdaQueryChainWrapper(spaceQueryDTO, null)
                .list();
        return new SpaceDTO(spaces).list();
    }

    @GetMapping("/public/spaces/list")
    public List<SpaceDTO> getListPublic(SpaceQueryDTO spaceQueryDTO) {
        List<Space> spaces = this.getSpaceLambdaQueryChainWrapper(spaceQueryDTO, null)
                .list();
        return new SpaceDTO(spaces).list();
    }

    @GetMapping("/spaces/count")
    public long getCount(SpaceQueryDTO queryDTO) {
        return this.getSpaceLambdaQueryChainWrapper(queryDTO, null).count();
    }

    @GetMapping("/spaces/company/storeId")
    public List<String> getStoreIds(@RequestParam("projectId") String projectId, @RequestParam("companyId") String companyId) {
        List<Space> list = spaceService.lambdaQuery()
                .eq(Space::getProjectId, projectId)
                .eq(Space::getCompanyId, companyId)
                .list();
        List<String> ids = list.stream().map(Space::getId).collect(Collectors.toList());
        return ids;
    }

    @GetMapping("/spaces/company/areaId")
    public List<String> getCompanyIds(
            @RequestParam(name = "projectId") String projectId,
            @RequestParam(name = "areaId", defaultValue = "") String areaId
                                     ) {
        SpaceQueryDTO queryDTO = new SpaceQueryDTO();
        queryDTO.setProjectId(projectId);
        queryDTO.setAreaId(areaId);
        queryDTO.setHasCompanyId(true);
        List<Space> spaces = this.getSpaceLambdaQueryChainWrapper(queryDTO, null).list();
        return spaces.stream().map(Space::getCompanyId).distinct().collect(Collectors.toList());
    }

    @GetMapping("/spaces/company/page")
    public PageResponse<SpaceDTO> getNotCompanyIdPage(SpaceQueryDTO spaceQueryDTO, PageRequest pageRequest) {
        Page<Space> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        page = spaceService.lambdaQuery()
                .and(q -> q.isNull(Space::getCompanyId).or().eq(Space::getCompanyId, ""))
                .eq(Space::getProjectId, spaceQueryDTO.getProjectId())
                .and(StringUtils.isNotEmpty(spaceQueryDTO.getSearch()), q -> q
                        .like(Space::getCode, spaceQueryDTO.getSearch())
                        .or()
                        .like(Space::getName, spaceQueryDTO.getSearch())
                    )
                .and(StringUtils.isNotEmpty(spaceQueryDTO.getAreaId()), q -> q
                        .eq(Space::getAreaId1, spaceQueryDTO.getAreaId())
                        .or()
                        .eq(Space::getAreaId2, spaceQueryDTO.getAreaId())
                        .or()
                        .eq(Space::getAreaId3, spaceQueryDTO.getAreaId())
                    )
                .eq(Space::getAttribute, SpaceService.ATTR_BOOTH)
                .eq(Space::getTradeModel, SpaceService.TRADE_MODEL_OPERATION)
                .page(page);
        //        List<SpaceDTO> spaceDTOS = this.getNoCompanySpaces(spaces);
        List<SpaceDTO> spaceList = Convert.toList(SpaceDTO.class, page.getRecords());
        this.extend(spaceList);
        return new PageResponse(spaceList, Integer.parseInt(String.valueOf(page.getTotal())), Integer.parseInt(String.valueOf(page.getSize())));
    }

    @PostMapping("/spaces")
    public void save(@Valid @RequestBody SpaceDTO spaceDTO) {
        spaceService.save(spaceDTO);
    }

    @PutMapping("/spaces")
    public void update(@Valid @RequestBody SpaceDTO spaceDTO) {
        spaceService.update(spaceDTO);
    }

    @DeleteMapping("/spaces/{id}")
    public void remove(@PathVariable("id") String id) {
        spaceService.remove(id);
    }

    @DeleteMapping("/spaces")
    public void remove(@RequestParam("ids") String[] ids) {
        spaceService.removeBatch(ids);
    }

    @GetMapping("/spaces/data-export")
    public void exportData(@Valid SpaceQueryDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        List<Space> list = this.getSpaceLambdaQueryChainWrapper(queryDTO, null).list();
        List<SpaceExcelDTO> dtos = new SpaceExcelDTO(list).list();
        this.extendExcelSpace(queryDTO.getProjectId(), dtos);
        ExcelUtils<SpaceExcelDTO> utils = new ExcelUtils<>(SpaceExcelDTO.class);
        utils.createWorkbook();
        try {
            utils.createSheet("空间信息", dtos);
        } catch (Exception e) {
            throw new ServiceException("导出数据失败", e);
        }
        utils.downLoadExcel("空间信息", request, response);
    }

    @GetMapping("/spaces/template-download")
    public void exportTemplate(@RequestParam(value = "projectId") String projectId, HttpServletRequest request,
                               HttpServletResponse response
                              ) {
        Map<String, List<String>> maps = new HashMap<>();
        List<AreaDeliver> all = merchantAreaService.lambdaQuery().eq(AreaDeliver::getProjectId, projectId).list();
        List<AreaDeliver> root = all.stream().filter(area -> StringUtils.isEmpty(area.getPid())).collect(Collectors.toList());
        List<String> collect = root.stream().map(area1 -> this.getFromatNameId(area1.getName(), area1.getId()))
                .collect(Collectors.toList());
        root.forEach(area -> this.getChildAreas(area, all, maps));

        List<String> attrs = new ArrayList<>();
        List<String> tradeModels = new ArrayList<>();
        List<String> storeTypes = new ArrayList<>();
        //        this.getDictName().forEach((k, v) -> {
        //            if (k.contains(ATTR)) attrs.add(this.getFromatNameId(v, k));
        //            if (k.contains(TRADE_MODEL)) tradeModels.add(this.getFromatNameId(v, k));
        //            if (k.contains(STORE_TYPE)) storeTypes.add(this.getFromatNameId(v, k));
        //        });

        ExcelUtils<SpaceExcelDTO> utils = new ExcelUtils<>(SpaceExcelDTO.class);
        utils.createWorkbook();
        XSSFSheet sheet = utils.createSheetTemplate("空间导入模板");
        utils.setCascade(sheet, collect, maps, 1);
        utils.setXSSFValidation(sheet, "attrs", attrs, 1, 7);
        utils.setXSSFValidation(sheet, "tradeModels", tradeModels, 1, 8);
        utils.setXSSFValidation(sheet, "storeTypes", storeTypes, 1, 6);
        utils.downLoadExcel("空间模板", request, response);
    }

    @PostMapping("/spaces/data-import")
    public List<ErrorExcelDTO> importData(@RequestParam(value = "projectId") String projectId, @RequestParam("file") MultipartFile file) {
        ExcelUtils<SpaceExcelDTO> utils = new ExcelUtils<>(SpaceExcelDTO.class);
        List<SpaceExcelDTO> dtoSpaces;
        try {
            dtoSpaces = utils.getDataByExcel(file.getInputStream(), "空间导入模板");
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
        if (dtoSpaces == null) {
            throw new ServiceException("excel没有数据");
        }
        try {
            spaceService.importData(projectId, dtoSpaces);
        } catch (Exception e) {
            throw new ServiceException("文件解析异常！");
        }
        return null;
    }

    private LambdaQueryChainWrapper<Space> getSpaceLambdaQueryChainWrapper(SpaceQueryDTO spaceQueryDTO, PageRequest pageRequest) {
        return spaceService.lambdaQuery()
                .eq(Space::getDeleted, false)
                .eq(spaceQueryDTO.getIds() == null, Space::getProjectId, spaceQueryDTO.getProjectId())
                .and(StringUtils.isNotEmpty(spaceQueryDTO.getSearch()), q -> q
                        .like(Space::getCode, spaceQueryDTO.getSearch())
                        .or()
                        .like(Space::getName, spaceQueryDTO.getSearch())
                        .or()
                        .like(Space::getCompanyName, spaceQueryDTO.getSearch())
                    )
                .and(StringUtils.isNotEmpty(spaceQueryDTO.getAreaId()), q -> q
                        .eq(Space::getAreaId1, spaceQueryDTO.getAreaId())
                        .or()
                        .eq(Space::getAreaId2, spaceQueryDTO.getAreaId())
                        .or()
                        .eq(Space::getAreaId3, spaceQueryDTO.getAreaId())
                    )
                .in(spaceQueryDTO.getIds() != null && spaceQueryDTO.getIds().size() > 0, Space::getId, spaceQueryDTO.getIds())
                .eq(StringUtils.isNotEmpty(spaceQueryDTO.getType()), Space::getType, spaceQueryDTO.getType())
                .eq(StringUtils.isNotEmpty(spaceQueryDTO.getTradeModel()), Space::getTradeModel, spaceQueryDTO.getTradeModel())
                .eq(StringUtils.isNotEmpty(spaceQueryDTO.getAttribute()), Space::getAttribute, spaceQueryDTO.getAttribute())
                .eq(StringUtils.isNotEmpty(spaceQueryDTO.getDeptId()), Space::getDeptId, spaceQueryDTO.getDeptId())
                .ne(spaceQueryDTO.getHasCompanyId() != null && spaceQueryDTO.getHasCompanyId(), Space::getCompanyId, "")
                .orderByDesc((pageRequest != null && pageRequest.getOrderItems().isEmpty()), Space::getCreateTime);
    }

    private void extend(List<SpaceDTO> spaceDTOS) {
        spaceDTOS.parallelStream().forEach(this::extend);
    }

    private void extend(SpaceDTO spaceDTO) {
        List<String> areaIds = Arrays.asList(spaceDTO.getAreaId1(), spaceDTO.getAreaId2(), spaceDTO.getAreaId3());
        spaceDTO.setAreaIds(areaIds);
        spaceDTO.setAreaNames(merchantAreaService.parseNames(areaIds));
        spaceDTO.setTypeName("档位");
        spaceDTO.setAttributeName("商户档位");
        spaceDTO.setTradeModelName(ProjectMerchantAreaEnum.getCodeByName(spaceDTO.getTradeModel()));
    }

    private List<SpaceDTO> extendCompanyDTO(List<SpaceDTO> spaceDTOS) {
        return spaceDTOS.parallelStream().map(dto -> {
            Company company = merchantCompanyService.getById(dto.getCompanyId());
            if (company != null) {
                dto.setCompanyDTO(new CompanyDTO(company));
                return dto;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void extendExcelSpace(String projectId, List<SpaceExcelDTO> dtos) {
        List<AreaDeliver> areaList = merchantAreaService.lambdaQuery().eq(AreaDeliver::getProjectId, projectId).list();
        Map<String, String> areaMap = areaList.stream().collect(Collectors.toMap(AreaDeliver::getId, AreaDeliver::getName));
        //        List<BimInfo> bimInfos = bBimInfoService.lambdaQuery().eq(BimInfo::getBusinessType, SpaceService.BUSINESS_TYPE).list();
        //        Map<String, String> bimInfoMap = bimInfos.stream().collect(Collectors.toMap(BimInfo::getBusinessKey, BimInfo::getEcode));
        List<Company> companies = merchantCompanyService.lambdaQuery().eq(Company::getProjectId, projectId).list();
        Map<String, String> companyMap = companies.stream().collect(Collectors.toMap(Company::getId, Company::getName));
        //        Map<String, String> dictName = this.getDictName();

        Map<String, String> userNickNameMap = new HashMap<>();

        dtos.parallelStream().forEach(dto -> {
            if (StringUtils.isNotEmpty(dto.getAreaId1())) {
                String areaName = areaMap.get(dto.getAreaId1());
                if (StringUtils.isNotEmpty(areaName)) {
                    dto.setAreaId1(this.getFromatNameId(areaName, dto.getAreaId1()));
                } else {
                    dto.setAreaId1("");
                }
            }
            if (StringUtils.isNotEmpty(dto.getAreaId2())) {
                String areaName = areaMap.get(dto.getAreaId2());
                if (StringUtils.isNotEmpty(areaName)) {
                    dto.setAreaId2(this.getFromatNameId(areaName, dto.getAreaId2()));
                } else {
                    dto.setAreaId2("");
                }
            }
            if (StringUtils.isNotEmpty(dto.getAreaId3())) {
                String areaName = areaMap.get(dto.getAreaId3());
                if (StringUtils.isNotEmpty(areaName)) {
                    dto.setAreaId3(this.getFromatNameId(areaName, dto.getAreaId3()));
                } else {
                    dto.setAreaId3("");
                }
            }

            //            String attributeName = dictName.getOrDefault(dto.getAttribute(), null);
            //            String tradeModelName = dictName.getOrDefault(dto.getTradeModel(), null);
            //            String typeName = dictName.getOrDefault(dto.getType(), null);
            //            if (StringUtils.isNotEmpty(attributeName))
            //                dto.setAttribute(this.getFromatNameId(attributeName, dto.getAttribute()));
            //            if (StringUtils.isNotEmpty(tradeModelName))
            //                dto.setTradeModel(this.getFromatNameId(tradeModelName, dto.getTradeModel()));
            //            if (StringUtils.isNotEmpty(typeName))
            //                dto.setType(this.getFromatNameId(typeName, dto.getType()));

            dto.setCompanyId(companyMap.getOrDefault(dto.getCompanyId(), ""));

            /*if (StringUtils.isNotEmpty(dto.getCreateBy())) {
                if (!userNickNameMap.containsKey(dto.getCreateBy())) {
                    UserInfoDTO userInfo = sysUserApi.getById(dto.getCreateBy());
                    userNickNameMap.put(dto.getCreateBy(), userInfo == null ? "" : userInfo.getNickname());
                }
                dto.setCreateBy(userNickNameMap.get(dto.getCreateBy()));
            }
            if (StringUtils.isNotEmpty(dto.getUpdateBy())) {
                if (!userNickNameMap.containsKey(dto.getUpdateBy())) {
                    UserInfoDTO userInfo = sysUserApi.getById(dto.getUpdateBy());
                    userNickNameMap.put(dto.getUpdateBy(), userInfo == null ? "" : userInfo.getNickname());
                }
                dto.setUpdateBy(userNickNameMap.get(dto.getUpdateBy()));
            }*/

            //            dto.setEcode(bimInfoMap.getOrDefault(dto.getId(), ""));
        });
    }

    private void getChildAreas(AreaDeliver area, List<AreaDeliver> all, Map<String, List<String>> maps) {

        List<AreaDeliver> childAreas = all.stream().filter(area1 -> area1.getPid().equals(area.getId())).collect(Collectors.toList());
        List<String> collect = childAreas.stream().map(area1 -> this.getFromatNameId(area1.getName(), area1.getId()))
                .collect(Collectors.toList());
        if (childAreas.size() > 0) {
            String format = this.getFromatNameId(area.getName(), area.getId());
            maps.put(format, collect);
        }
        childAreas.forEach(area1 -> getChildAreas(area1, all, maps));
    }

    private String getFromatNameId(String name, String id) {
        return MessageFormat.format("_{0}_{1}", name, id);
    }

    private List<SpaceDTO> getNoCompanySpaces(List<Space> spaces) {
        List<Space> collect = spaces.stream().map(space -> {
            if (StringUtils.isEmpty(space.getCompanyId())) {
                return space;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return new SpaceDTO(collect).list();
    }

}
