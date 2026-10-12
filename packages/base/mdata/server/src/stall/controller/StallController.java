package com.guhecloud.cube.biz.mdata.server.stall.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelWriter;
import com.guhecloud.cube.biz.mdata.server.merchant.entity.MerchantStall;
import com.guhecloud.cube.biz.mdata.server.stall.dto.DbcStallDTO;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallDTO;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallIdTypeDTO;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallStatDTO;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallCategory;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallExpand;
import com.guhecloud.cube.biz.mdata.server.stall.param.ExcelTemplate;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCreateBathParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCreateParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallMasterOtherParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallPostQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallUpdateParam;
import com.guhecloud.cube.biz.mdata.server.stall.service.MerchantStallService;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallCategoryService;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallExpandService;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.stall.service.impl.StallBatchOptService;
import com.guhecloud.cube.biz.mdata.server.stall.vo.BatchOptResult;
import com.guhecloud.cube.biz.mdata.server.stall.vo.StallExpandVO;
import com.guhecloud.cube.biz.mdata.server.stall.vo.StallPageVO;
import com.guhecloud.cube.biz.mdata.server.stall.vo.StallVO;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Tree;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alibaba.nacos.common.http.param.MediaType.APPLICATION_OCTET_STREAM;

/**
 * 档位接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallController {

    private final StallService stallService;
    private final AreaService areaService;
    private final StallCategoryService stallCategoryService;
    private final OrganizationClient organizationClient;
    private final MerchantStallService merchantStallService;
    private final SupplierService supplierService;
    private final FileClient fileClient;
    private final StallBatchOptService stallBatchOptService;
    private final StallExpandService stallExpandService;

    /**
     * 档位新增
     *
     * @param param 档位新增参数
     */
    @PostMapping("/stall")
    public void save(@Valid @RequestBody StallCreateParam param) {
        this.stallService.save(param);
    }

    /**
     * 用户数据同步，减少参数的校验
     * 因为同步数据时，第三方可能没有相关的参数
     *
     * @param param
     */
    @PostMapping("/stall/sync")
    public void saveForSync(@RequestBody StallCreateParam param) {
        this.stallService.save(param);
    }

    /**
     * @Description 档位导表模板
     * @Author lrj
     */
    @GetMapping("/download-template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        List<ExcelTemplate> excelList = new ArrayList<>();
        //write to excel
        writeTemplate(excelList, response);
    }

    /**
     * 写sheet
     *
     * @param list     list
     * @param response res
     *
     * @throws IOException io
     */
    private void writeTemplate(List<ExcelTemplate> list, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_OCTET_STREAM);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=批量导入模板.xlsx");
        ExcelWriter<ExcelTemplate> excelWriter = new ExcelWriter<>();
        excelWriter.writeData(list, stallBatchOptService.buildExcelColumnSelectionProvider(), ExcelTemplate.class, response
                .getOutputStream(), false, 9999);
    }

    /**
     * 档位模板导入
     *
     * @param file
     *
     * @return
     *
     * @throws Exception
     */
    @PostMapping("/import")
    public BatchOptResult importTemplate(@RequestPart("file") MultipartFile file) throws Exception {
        return stallBatchOptService.importFromTemplate(file.getInputStream());
    }

    /**
     * 档位更新
     *
     * @param param 档位更新参数
     */
    @PutMapping("/stall")
    public void update(@Valid @RequestBody StallUpdateParam param) {
        this.stallService.update(param);
    }

    /**
     * 档位ID查询
     *
     * @param id 档位ID
     *
     * @return 档位
     */
    @GetMapping("/stall/{id}")
    public StallVO getById(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                           @PathVariable("id") Long id
                          ) {
        Stall stall = this.stallService.getById(id);
        Assert.isNotFound(stall == null, "档位信息不存在");
        return extendStallVO(stall);
    }

    /**
     * 公共档位ID查询
     *
     * @param id 档位ID
     *
     * @return 档位
     */
    @GetMapping("/public/stall/{id}")
    public StallVO publicGetById(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                                 @PathVariable("id") Long id
                                ) {
        return getById(id);
    }

    public StallVO extendStallVO(Stall stall) {
        StallVO stallVO = ObjectUtils.copying(stall, StallVO.class);
        // 扩展获取部门名称
        if (StringUtils.isNotBlank(stall.getDeptId())) {
            List<OrganizationDTO> organizations = this.organizationClient.getOrganizations(new OrganizationQueryParam());
            List<Long> deptIds = Arrays.stream(stall.getDeptId().split(",")).map(Long::parseLong).collect(Collectors.toList());
            stallVO.setDeptIds(deptIds);
            stallVO.setDeptNames(this.getDeptNames(organizations, deptIds));
        }

        // 扩展区域信息
        if (StringUtils.isNotBlank(stall.getArea())) {
            List<String> areaIds = Arrays.asList(stall.getArea().split(","));
            stallVO.setAreaIds(areaIds);
            List<Area> areas = this.areaService.getByIds(areaIds.stream().map(Long::parseLong).collect(Collectors.toList()));
            stallVO.setAreaNames(areas.stream().map(Area::getName).collect(Collectors.toList()));
        }

        //扩展档位类型信息
        if (StringUtils.isNotBlank(stall.getType())) {
            try {
                StallCategory stallCategory = this.stallCategoryService.getById(Long.valueOf(stall.getType()));
                if (Objects.nonNull(stallCategory)) {
                    stallVO.setTypeName(stallCategory.getName());
                }
            } catch (Exception e) {
                log.error("转换id异常", e);
            }

        }
        //扩展信息
        StallExpand stallExpand = this.stallExpandService.getByStallId(stall.getId());
        if (Objects.nonNull(stallExpand)) {
            stallVO.setStallExpand(ObjectUtils.copying(stallExpand, StallExpandVO.class));
        }
        //扩展附件信息
        List<FileVO> fileVOS = this.fileClient.getByBusinessKey(stall.getId());
        stallVO.setFiles(fileVOS);
        return stallVO;
    }

    /**
     * 根据模型编码获取档位详情
     *
     * @param visualizationCode 模型编码
     *
     * @return
     */
    @GetMapping("/stall/detail/by-visualization/{visualizationCode}")
    public StallVO getByVisualizationCode(@PathVariable("visualizationCode") String visualizationCode) {
        Stall stall = this.stallService.getByVisualizationCode(visualizationCode);
        Assert.isNotFound(stall == null, "档位信息不存在");
        return extendStallVO(stall);
    }

    /**
     * 档位分页查询
     *
     * @param queryParam 档位查询参数
     *
     * @return 档位分页集合
     */
    @GetMapping("/stall/page")
    public Pagination<StallPageVO> page(@Valid StallQueryParam queryParam) {
        Page<Stall> page = this.stallService.page(queryParam);
        List<StallPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), StallPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return Pagination.empty();
        }
        // 扩展区域信息
        List<Area> areas = this.areaService.list();
        Map<Long, List<Area>> areaMap = areas.stream().collect(Collectors.groupingBy(Area::getId));
        List<OrganizationDTO> organizations = organizationClient.getOrganizations(new OrganizationQueryParam());
        // 扩展档位类型名称
        List<StallCategory> stallCategories = this.stallCategoryService.list();
        Map<String, List<StallCategory>> stallCategoryMap = stallCategories.stream()
                .collect(Collectors.groupingBy(stallCategory -> String.valueOf(stallCategory.getId())));
        List<Long> stallIds = pageVOS.stream().map(StallPageVO::getId).collect(Collectors.toList());
        //扩展附件信息
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(stallIds);
        Map<Long, List<FileVO>> byBusinessKeys = fileClient.getByBusinessKeys(businessKeysParam);
        Map<Long, StallExpand> stallExpandMap = this.stallExpandService.getStallExpands(stallIds).stream()
                .collect(Collectors.toMap(StallExpand::getStallId, stallExpand -> stallExpand, (o, n) -> n));
        for (StallPageVO pageVO : pageVOS) {
            // 部门信息
            if (StringUtils.isNotEmpty(pageVO.getDeptId())) {
                List<Long> deptIds = Arrays.stream(pageVO.getDeptId().split(",")).map(Long::parseLong).collect(Collectors.toList());
                pageVO.setDeptIds(deptIds);
                pageVO.setDeptNames(this.getDeptNames(organizations, deptIds));
            }

            // 档位分类信息
            if (!MapUtils.isEmpty(stallCategoryMap) && Objects.nonNull(stallCategoryMap.get(pageVO.getType()))) {
                StallCategory stallCategory = stallCategoryMap.get(pageVO.getType()).get(0);
                pageVO.setTypeName(stallCategory.getName());
            }
            // 扩展档位区域信息
            if (StringUtils.isNotEmpty(pageVO.getArea())) {
                List<Long> areaIds = Arrays.stream(pageVO.getArea().split(",")).map(Long::parseLong).collect(Collectors.toList());
                pageVO.setAreaNames(this.areaService.getAndAppendAreaName(areaIds, areaMap));
            }
            //扩展档位附件信息
            if (byBusinessKeys.containsKey(pageVO.getId())) {
                pageVO.setFiles(byBusinessKeys.get(pageVO.getId()));
            }
            //档位扩展信息
            if (stallExpandMap.containsKey(pageVO.getId())) {
                pageVO.setStallExpand(ObjectUtils.copying(stallExpandMap.get(pageVO.getId()), StallExpandVO.class));
            }
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 获取我的租赁的档位信息
     *
     * @return 档位信息
     */
    @GetMapping("/stall/mp/mine")
    public List<StallPageVO> listMpMyStalls() {
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        Supplier supplier = this.supplierService.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        SessionContextHolder.requestTenant(supplier.getTenant());
        List<Stall> stalls = this.stallService.getMineStalls(supplier);
        return ObjectUtils.copying(stalls, StallPageVO.class);
    }

    /**
     * 获取对应批发商档位信息
     *
     * @param supplierId 批发商主键
     *
     * @return 档位信息
     */
    @GetMapping("/stall/{supplierId}/supplier")
    public List<StallPageVO> listMyStalls(@PathVariable("supplierId") Long supplierId) {
        Supplier supplier = this.supplierService.getById(supplierId);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        SessionContextHolder.requestTenant(supplier.getTenant());
        List<Stall> stalls = this.stallService.getMineStalls(supplier);
        return ObjectUtils.copying(stalls, StallPageVO.class);
    }

    /**
     * 获取当前商户的档位
     *
     * @param merchantId 商户主键
     *
     * @return 档位信息
     */
    @GetMapping("/stall/{merchantId}/merchant")
    public List<StallDTO> getMerchantStall(@PathVariable("merchantId") Long merchantId) {
        List<MerchantStall> merchantStalls = this.merchantStallService.getStallByMerchantId(merchantId);
        if (CollectionUtils.isEmpty(merchantStalls)) {
            return Collections.emptyList();
        }
        List<Long> stallIds = merchantStalls.stream().map(MerchantStall::getStallId).collect(Collectors.toList());
        List<Stall> stalls = this.stallService.getByIds(stallIds);
        return ObjectUtils.copying(stalls, StallDTO.class);
    }

    /**
     * 根据档位ID 查询商户下的其他档位
     *
     * @param id 档位ID
     *
     * @return 档位信息
     */
    @GetMapping("/stall/{id}/stalls")
    public List<StallDTO> getStallsInMerchantById(@PathVariable("id") Long id) {
        MerchantStall oneMerchant = this.merchantStallService
                .getOne(Wrappers.<MerchantStall>lambdaQuery().eq(MerchantStall::getStallId, id));
        if (oneMerchant != null) {
            List<MerchantStall> merchantStalls = this.merchantStallService.getStallByMerchantId(oneMerchant.getMerchantId());
            if (CollectionUtils.isEmpty(merchantStalls)) {
                return Collections.emptyList();
            }
            List<Long> stallIds = merchantStalls.stream().map(MerchantStall::getStallId).collect(Collectors.toList());
            List<Stall> stalls = this.stallService.getByIds(stallIds);
            return ObjectUtils.copying(stalls, StallDTO.class);
        }
        return Collections.emptyList();
    }

    @GetMapping("/stall/list")
    public List<StallDTO> getStallList(StallQueryParam queryParam) {
        List<Stall> stalls = this.stallService.list(queryParam);
        // 获取档位类型信息
        List<StallCategory> stallCategories = this.stallCategoryService.list();
        Map<String, List<StallCategory>> stallCategoryMap = stallCategories.stream()
                .collect(Collectors.groupingBy(stallCategory -> String.valueOf(stallCategory.getId())));

        // 构建信息
        List<StallDTO> records = ObjectUtils.copying(stalls, StallDTO.class);
        records.forEach(record -> {
            if (stallCategoryMap.containsKey(record.getType())) {
                StallCategory stallCategory = stallCategoryMap.get(record.getType()).get(0);
                record.setTypeName(stallCategory.getName());
            }
        });
        return records;
    }

    @GetMapping("/stall/id")
    public StallDTO getStallById(@RequestParam("id") Long id) {
        return ObjectUtils.copying(this.stallService.getById(id), StallDTO.class);
    }

    @GetMapping("/stall/ids")
    public List<StallDTO> getStallByIds(@RequestParam("ids") List<Long> ids) {
        return ObjectUtils.copying(this.stallService.getByIds(ids), StallDTO.class);
    }

    @GetMapping("/stall/list/ids")
    public List<StallDTO> getStallListByIds(@RequestParam("ids") List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<StallCategory> stallCategories = this.stallCategoryService.list();
            Map<String, List<StallCategory>> stallCategoryMap = stallCategories.stream()
                    .collect(Collectors.groupingBy(stallCategory -> String.valueOf(stallCategory.getId())));
            List<Stall> stallList = stallService.lambdaQuery().in(Stall::getId, ids).list();
            List<StallDTO> stallDTOS = new ArrayList<>();
            Map<Long, StallExpand> stallExpandMap = this.stallExpandService.getStallExpands(ids).stream()
                    .collect(Collectors.toMap(StallExpand::getStallId, stallExpand -> stallExpand, (o, n) -> n));
            Optional.ofNullable(stallList).orElse(Collections.emptyList()).forEach(stall -> {
                StallDTO dto = ObjectUtils.copying(stall, StallDTO.class);
                String areaIdString = stall.getArea();
                if (StringUtils.isNotBlank(areaIdString)) {
                    List<String> areaIds = Arrays.asList(stall.getArea().split(","));
                    List<Area> areas = this.areaService.getByIds(areaIds.stream().map(Long::parseLong).collect(Collectors.toList()));
                    dto.setAreaName(areas.stream().map(Area::getName).collect(Collectors.joining("/")));
                }
                List<StallCategory> categoryList = stallCategoryMap.get(dto.getType());
                dto.setTypeName(CollectionUtils.isEmpty(categoryList) ? "" : categoryList.get(0).getName());
                if (stallExpandMap.containsKey(stall.getId())) {
                    dto.setStallExpand(ObjectUtils.copying(stallExpandMap.get(stall.getId()), StallExpandVO.class));
                }
                stallDTOS.add(dto);
            });
            return stallDTOS;
        } else {
            return Collections.emptyList();
        }
    }

    @PostMapping("/stall/list/post/ids")
    public List<StallDTO> postStallListByIds(@RequestBody StallPostQueryParam param) {
        return this.getStallListByIds(param.getStallIdList());
    }

    /**
     * 档位删除
     *
     * @param id 档位ID
     */
    @DeleteMapping("/stall/{id}")
    public void remove(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                       @PathVariable("id") Long id
                      ) {
        this.stallService.remove(id);
    }

    /**
     * 档位分页查询（内部调用）
     *
     * @param queryParam 档位查询参数
     *
     * @return 档位分页集合
     */
    @GetMapping("/stall-client/page")
    public Pagination<StallDTO> getStallPage(StallQueryParam queryParam) {
        Page<Stall> page = this.stallService.page(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<StallDTO> stalls = ObjectUtils.copying(page.getRecords(), StallDTO.class);
        // 扩展档位类型名称
        List<StallCategory> stallCategories = this.stallCategoryService.list();
        Map<String, List<StallCategory>> stallCategoryMap = stallCategories.stream()
                .collect(Collectors.groupingBy(stallCategory -> String.valueOf(stallCategory.getId())));
        List<OrganizationDTO> organizations = organizationClient.getOrganizations(new OrganizationQueryParam());

        // 扩展区域信息
        List<Area> areas = this.areaService.list();
        Map<Long, List<Area>> areaMap = areas.stream().collect(Collectors.groupingBy(Area::getId));
        //扩展信息
        Set<Long> stallIds = stalls.stream().map(StallDTO::getId).collect(Collectors.toSet());
        Map<Long, StallExpand> stallExpandMap = this.stallExpandService.getStallExpands(stallIds).stream()
                .collect(Collectors.toMap(StallExpand::getStallId, stallExpand -> stallExpand, (o, n) -> n));
        for (StallDTO pageVO : stalls) {
            // 部门信息
            if (StringUtils.isNotEmpty(pageVO.getDeptId())) {
                List<Long> deptIds = Arrays.stream(pageVO.getDeptId().split(",")).map(Long::parseLong).collect(Collectors.toList());
                pageVO.setDeptIds(deptIds);
                pageVO.setDeptNames(this.getDeptNames(organizations, deptIds));
            }
            // 档位分类信息
            if (!MapUtils.isEmpty(stallCategoryMap) && Objects.nonNull(stallCategoryMap.get(pageVO.getType()))) {
                StallCategory stallCategory = stallCategoryMap.get(pageVO.getType()).get(0);
                pageVO.setTypeName(stallCategory.getName());
            }
            // 扩展档位区域信息
            if (StringUtils.isNotEmpty(pageVO.getArea())) {
                List<Long> areaIds = Arrays.stream(pageVO.getArea().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<String> areaNames = areaIds.stream().map(areaId -> {
                    if (areaMap.containsKey(areaId)) {
                        return areaMap.get(areaId).get(0).getName();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                pageVO.setAreaName(String.join("/", areaNames));
            }
            if (stallExpandMap.containsKey(pageVO.getId())) {
                pageVO.setStallExpand(ObjectUtils.copying(stallExpandMap.get(pageVO.getId()), StallExpandVO.class));
            }
        }
        return PagingUtils.convert(page, stalls);
    }

    @PostMapping("/post/stall/page")
    public Pagination<StallDTO> postStallPage(@RequestBody @Valid StallQueryParam queryParam) {
        return getStallPage(queryParam);
    }

    /**
     * 获取部门名称
     *
     * @param dtos    部门集合
     * @param deptIds 部门主键集合
     *
     * @return 部门名称
     */
    private List<String> getDeptNames(List<OrganizationDTO> dtos, List<Long> deptIds) {
        List<String> deptNames = new ArrayList<>();
        Map<Long, List<OrganizationDTO>> organizationMap = dtos.stream().collect(Collectors.groupingBy(Tree.Simple::getId));
        for (Long deptId : deptIds) {
            if (!MapUtils.isEmpty(organizationMap) && organizationMap.containsKey(deptId)) {
                deptNames.add(organizationMap.get(deptId).get(0).getName());
            }
        }
        return deptNames;
    }

    @GetMapping("/stall/area/stat")
    public List<StallStatDTO> getStallStatByArea() {
        return stallService.getStallStatByArea();
    }

    @GetMapping("/stall/code/{code}")
    public StallDTO getStallByCode(@PathVariable("code") String code) {
        Stall stall = stallService.getOne(Wrappers.<Stall>lambdaQuery()
                                                  .eq(Stall::getCode, code));
        return stall != null ? ObjectUtils.copying(stall, StallDTO.class) : null;
    }

    /**
     * 大白菜挡位批量新增
     * 因为同步数据时，第三方可能没有相关的参数
     *
     * @param param
     */
    @PostMapping("/stall/saveOrUpdateBatch")
    public void saveOrUpdateBatch(@RequestBody StallCreateBathParam param) {
        this.stallService.saveOrUpdateBatch(param);
    }

    /**
     * 获取挡位数据(完整PO信息)
     *
     * @return 挡位集合
     */
    @GetMapping("/stall/getAllStallEntity")
    List<DbcStallDTO> getAllStallEntity() {
        return this.stallService.getAllStallEntity();
    }

    @GetMapping("/stall/stallName")
    public StallDTO getStallByName(@RequestParam("name") String name) {
        Stall stall = stallService.getOne(Wrappers.<Stall>lambdaQuery()
                                                  .eq(Stall::getName, name)
                                                  .last("limit 1"));
        return stall != null ? ObjectUtils.copying(stall, StallDTO.class) : null;
    }

    /**
     * 获取档位ID列表
     *
     * @param queryParam 查询参数
     *
     * @return 档位ID列表
     */
    @PostMapping("/stall/idTypes")
    public List<StallIdTypeDTO> getStallIdTypes(@RequestBody @Valid StallQueryParam queryParam) {
        return this.stallService.getStallIdTypes(queryParam);
    }

    /**
     * 根据区域id列表批量获取档位数统计
     *
     * @param areaIds 区域id列表
     *
     * @return 区域id与档位数的映射，key为区域id，value为档位数
     */
    @PostMapping("/stall/count/by/areaIds")
    public Map<Long, Long> countByAreaIds(@RequestBody List<Long> areaIds) {
        return stallService.countByAreaIds(areaIds);
    }

    /**
     * 获取某档位档主的其他档位
     * 注意不包含自己
     *
     * @param param 条件
     *
     * @return 其他档位列表
     */
    @PostMapping("/stall/stallMasterOthers")
    public List<StallDTO> postStallMasterOthers(@RequestBody StallMasterOtherParam param) {
        if (param == null || param.getId() == null) {
            return CollectionUtils.emptyList();
        }
        var stall = this.stallService.getById(param.getId());
        if (stall == null || stall.getMasterPhone() == null) {
            return CollectionUtils.emptyList();
        }
        var otherStalls = this.stallService.list(Wrappers.<Stall>lambdaQuery()
                                                         .eq(Stall::getMasterPhone, stall.getMasterPhone())
                                                         .eq(Stall::getEnabled, true)
                                                         .ne(Stall::getId, param.getId())
                                                         .in(CollectionUtils.isNotEmpty(param.getOtherIdList()), Stall::getId, param.getOtherIdList()));
        if (CollectionUtils.isEmpty(otherStalls)) {
            return CollectionUtils.emptyList();
        }
        return otherStalls.stream().map(this::stallPoToDto).collect(Collectors.toList());
    }

    private StallDTO stallPoToDto(Stall stall) {
        var stallDTO = new StallDTO();
        stallDTO.setId(stall.getId());
        stallDTO.setCode(stall.getCode());
        stallDTO.setName(stall.getName());
        stallDTO.setType(stall.getType());
        stallDTO.setArea(stall.getArea());
        stallDTO.setLocation(stall.getLocation());
        stallDTO.setBuiltUpArea(stall.getBuiltUpArea());
        stallDTO.setPracticalArea(stall.getPracticalArea());
        stallDTO.setDeptId(stall.getDeptId());
        stallDTO.setMasterName(stall.getMasterName());
        stallDTO.setMasterPhone(stall.getMasterPhone());
        stallDTO.setLeaseType(stall.getLeaseType());
        stallDTO.setEnabled(stall.getEnabled());
        stallDTO.setRemark(stall.getRemark());
        stallDTO.setVisualizationCode(stall.getVisualizationCode());
        stallDTO.setCreateBy(stall.getCreateBy());
        stallDTO.setCreateTime(stall.getCreateTime());
        stallDTO.setUpdateBy(stall.getUpdateBy());
        stallDTO.setUpdateTime(stall.getUpdateTime());
        stallDTO.setDeleted(stall.getDeleted());
        return stallDTO;
    }
}
