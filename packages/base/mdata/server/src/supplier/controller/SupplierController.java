package com.guhecloud.cube.biz.mdata.server.supplier.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.goods.entity.GoodsCategory;
import com.guhecloud.cube.biz.mdata.server.goods.service.GoodsCategoryService;
import com.guhecloud.cube.biz.mdata.server.supplier.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierAuthentication;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.SupplierUser;
import com.guhecloud.cube.biz.mdata.server.supplier.enumeration.SupplierUserEnum;
import com.guhecloud.cube.biz.mdata.server.supplier.param.AccountQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierAuthenticationQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierModifyMpParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierModifyParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.param.SupplierTemplate;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierUserService;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierAccountVO;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierPageVO;
import com.guhecloud.cube.biz.mdata.server.supplier.vo.SupplierVO;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.TenantQueryParam;
import com.guhecloud.cube.biz.wallet.client.AccountClient;
import com.guhecloud.cube.biz.wallet.client.dto.WalletAccountDTO;
import com.guhecloud.cube.biz.wallet.client.param.WalletAccountQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.LegacyResponse;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
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
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 批发商管理接口
 *
 * @author lzy
 * @date 2021/12/3
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplierController {

    private final SupplierService supplierService;
    private final GoodsCategoryService goodsCategoryService;
    private final SupplierUserService supplierUserService;
    private final SupplierAuthenticationService supplierAuthenticationService;
    private final AccountClient accountClient;
    private final UserClient userClient;
    private final TenantClient tenantClient;
    private final FileClient fileClient;

    /**
     * 新增批发商
     *
     * @param param 新增参数
     */
    @PostMapping("/supplier")
    public void addSupplier(@RequestBody SupplierModifyParam param) {
        this.supplierService.addSupplier(param);
    }

    /**
     * @Description 批量导入批发商
     * @Author lrj
     */
    @PostMapping("/import-supplier")
    public void importSupplierTemplate(@RequestPart("file") MultipartFile file) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            List<Map<String, Object>> readAll = reader.readAll();
            if (CollectionUtils.isNotEmpty(readAll)) {
                List<SupplierModifyParam> objects = new ArrayList<>();
                for (Map<String, Object> map : readAll) {
                    SupplierModifyParam supplierModifyParam = new SupplierModifyParam();
                    for (String s : map.keySet()) {
                        Object o = map.get(s);
                        if (s.equals("商家名称")) {
                            supplierModifyParam.setName(o.toString());
                        } else if (s.equals("管理员")) {
                            supplierModifyParam.setAdminPhone(o.toString());
                        } else if (s.equals("联系人")) {
                            supplierModifyParam.setLiaison(o.toString());
                        } else if (s.equals("联系人手机号")) {
                            supplierModifyParam.setPhone(o.toString());
                        } else if (s.equals("详细地址")) {
                            supplierModifyParam.setAddress(o.toString());
                        } else if (s.equals("批发商档主")) {
                            supplierModifyParam.setShiftMaster(o.toString());
                        } else if (s.equals("档主电话")) {
                            supplierModifyParam.setShiftMasterPhone(o.toString());
                        } else if (s.equals("档主身份证")) {
                            supplierModifyParam.setShiftMasterCard(o.toString());
                        } else if (s.equals("其他联系方人")) {
                            supplierModifyParam.setOtherContacts(o.toString());
                        }

                    }
                    objects.add(supplierModifyParam);
                }
                supplierService.importSupplierTemplate(objects);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 批发商导模板
     */
    @GetMapping("/supplier-template")
    public void supplierTemplate(HttpServletResponse response) throws UnsupportedEncodingException {
        SupplierTemplate excelTemplate = new SupplierTemplate();
        List<SupplierTemplate> excelList = new ArrayList<>();
        excelList.add(excelTemplate);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "商家名称");
        writer.addHeaderAlias("adminPhone", "管理员");
        writer.addHeaderAlias("liaison", "联系人");
        writer.addHeaderAlias("phone", "联系人手机号");
        writer.addHeaderAlias("shiftMaster", "批发商档主");
        writer.addHeaderAlias("shiftMasterPhone", "档主电话");
        writer.addHeaderAlias("shiftMasterCard", "档主身份证");
        writer.addHeaderAlias("otherContacts", "其他联系方人");
        writer.addHeaderAlias("address", "详细地址");
        writer.write(excelList, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");

        String name = "批发商导表模板";
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (ServletOutputStream out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增批发商并认证
     *
     * @param param
     */
    @PostMapping("/supplier/inner")
    public Long addSupplierAndAuthentication(@Valid @RequestBody SupplierModifyParam param) {
        return this.supplierService.addSupplierAndAuthentication(param);
    }

    /**
     * 新增批发商（小程序）
     */
    @PostMapping("/supplier-mp")
    public SupplierVO addSupplierByMp(@Valid @RequestBody SupplierModifyMpParam param) {
        Supplier supplier = this.supplierService.addSupplierByMp(param);
        SupplierVO supplierVO = ObjectUtils.copying(supplier, SupplierVO.class);
        supplierVO.setAreaIds(Arrays.asList(supplier.getArea().split("-")));
        return supplierVO;
    }

    /**
     * 编辑批发商
     *
     * @param id    批发商主键
     * @param param 编辑参数
     */
    @PutMapping("/supplier/{id}")
    public void updateSupplier(@PathVariable("id") Long id, @Valid @RequestBody SupplierModifyParam param) {
        this.supplierService.updateSupplier(id, param);
    }

    /**
     * 获取当前用户所有的批发商租户
     *
     * @return 批发商租户信息
     */
    @GetMapping("/supplier/mine/tenant")
    public List<SupplierVO> listMySupplierTenants() {
        Principal principal = SecurityContextHolder.getPrincipal();
        List<SupplierUser> supplierUsers = this.supplierUserService.listSupplierUser(principal.getId());
        if (CollectionUtils.isEmpty(supplierUsers)) {
            return Collections.emptyList();
        }
        Map<Long, List<SupplierUser>> supplierUserMap =
                supplierUsers.stream().collect(Collectors.groupingBy(SupplierUser::getSupplierId));
        Set<Long> supplierIds = supplierUsers.stream().map(SupplierUser::getSupplierId).collect(Collectors.toSet());
        List<Supplier> suppliers = this.supplierService.listByIds(supplierIds);
        Set<Integer> tenantCodes = suppliers.stream().map(Supplier::getTenant).collect(Collectors.toSet());
        List<TenantDTO> tenants = this.tenantClient.listByCodes(tenantCodes);
        Map<Integer, List<TenantDTO>> tenantMap = tenants.stream().collect(Collectors.groupingBy(TenantDTO::getCode));
        return suppliers.stream().map(supplier -> {
            SupplierVO supplierVO = ObjectUtils.copying(supplier, SupplierVO.class);
            supplierVO.setSupplierTenant(supplier.getSupplierTenant());
            supplierVO.setMarketTenant(supplier.getTenant());
            if (tenantMap.containsKey(supplier.getTenant())) {
                TenantDTO tenantDTO = tenantMap.get(supplier.getTenant()).get(0);
                supplierVO.setMarketTenantName(tenantDTO.getName());
                List<FileVO> logo = this.fileClient.getByBusinessKey(supplier.getId());
                supplierVO.setLogoId(CollectionUtils.isEmpty(logo) ? null : logo.get(0).getId());
                supplierVO.setLogo(CollectionUtils.isEmpty(logo) ? null : logo.get(0));
            }
            if (supplierUserMap.containsKey(supplier.getId())) {
                List<SupplierUser> sus = supplierUserMap.get(supplier.getId());
                List<SupplierUserEnum> supplierCategories = sus.stream().map(SupplierUser::getSupplierCategory)
                        .collect(Collectors.toList());
                SupplierUser supplierUser = new SupplierUser();
                for (SupplierUser user : sus) {
                    if (supplierCategories.contains(user.getSupplierCategory()) && user.getSupplierCategory() == SupplierUserEnum.ADMIN) {
                        supplierUser = user;
                        break;
                    } else if (supplierCategories.contains(user.getSupplierCategory()) &&
                               user.getSupplierCategory() == SupplierUserEnum.MANAGER) {
                        supplierUser = user;
                        break;
                    }
                    supplierUser = user;
                }
                supplierVO.setSupplierCategory(supplierUser.getSupplierCategory());
                supplierVO.setAuthWalletPay(supplierUser.getAuthWalletPay());
                supplierVO.setAuthPurchaseReport(supplierUser.getAuthPurchaseReport());
            }
            return supplierVO;
        }).collect(Collectors.toList());
    }

    /**
     * 批发商资料详细信息
     *
     * @param id 批发商主键
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/{id}")
    public SupplierVO getById(@NotNull(message = "主键不能为空") @PathVariable("id") Long id) {
        // 获取批发商信息
        Supplier supplier = this.supplierService.getById(id);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        SupplierVO supplierVO = ObjectUtils.copying(supplier, SupplierVO.class);

        // 获取企业管理员信息
        UserDTO admin = this.userClient.getTenantAdminByTenant(supplier.getSupplierTenant());
        supplierVO.setAdminUserId(Objects.isNull(admin) ? 0 : admin.getId());
        supplierVO.setAdmin(Objects.isNull(admin) ? "" : admin.getMobile() + "/" + admin.getName());
        supplierVO.setAdminPhone(supplier.getAdminPhone());
        supplierVO.setAdminName(Objects.isNull(admin) ? "" : admin.getName());

        // 构建主营品种
        if (Objects.nonNull(supplier.getCategory())) {
            List<String> categoryIds = Arrays.asList(supplier.getCategory().split("-"));
            supplierVO.setCategoryIds(categoryIds);
            List<GoodsCategory> categories = this.goodsCategoryService.getByIds(categoryIds);
            supplierVO.setCategoryName(categories.stream().map(GoodsCategory::getName).collect(Collectors.toList()));
            supplierVO.setCategories(categories);
        }
        if (StringUtils.isNotBlank(supplier.getArea())) {
            // 构建地区信息
            supplierVO.setAreaIds(Arrays.asList(supplier.getArea().split("-")));
        }
        // 构建logo信息
        List<FileVO> logoInfo = this.fileClient.getByBusinessKey(supplier.getId());
        supplierVO.setLogoId(CollectionUtils.isEmpty(logoInfo) ? null : logoInfo.get(0).getId());
        supplierVO.setFileId(CollectionUtils.isEmpty(logoInfo) ? null : logoInfo.get(0).getId());
        supplierVO.setLogo(CollectionUtils.isEmpty(logoInfo) ? null : logoInfo.get(0));

        return supplierVO;
    }

    /**
     * 批发商资料分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/page")
    public Pagination<SupplierPageVO> pageSupplier(@Valid SupplierQueryParam queryParam) {
        // 获取批发商资料信息
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        queryParam.setTenant(tenant);

        Page<SupplierPageVO> page = this.supplierService.pageSupplierDetail(queryParam);
        return PagingUtils.convert(page);
    }

    /**
     * 小程序在根据市场获取对应租户集合
     *
     * @return 批发商列表
     */
    @GetMapping("/supplier/tenants")
    public List<Supplier> listSuppliersTenants() {
        return this.supplierService.listSuppliersTenants();
    }

    /**
     * 获取我的批发商列表
     *
     * @param tenant 租户列表
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/mine")
    public List<SupplierVO> listSuppliers(@NotNull(message = "市场租户编号不能为空") @RequestParam("tenant") Integer tenant) {
        // 获取当前用户信息
        Principal principal = SecurityContextHolder.getPrincipal();
        // todo 目前只获取管理员手机号是当前用户的商户，还需补充加入其他商户的商户
        List<Supplier> suppliers = this.supplierService.listSuppliersByUserAdminId(principal.getId(), tenant);
        // 获取用户信息
        Set<Long> userAdminIds = suppliers.stream().map(Supplier::getAdminUserId).collect(Collectors.toSet());
        List<UserDTO> users = this.userClient.listUserByIds(userAdminIds);
        Map<Long, List<UserDTO>> userMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));
        List<SupplierVO> supplierVOS = suppliers.stream().map(supplier -> {
            SupplierVO supplierVO = ObjectUtils.copying(supplier, SupplierVO.class);
            if (userMap.containsKey(supplier.getAdminUserId())) {
                UserDTO user = userMap.get(supplier.getAdminUserId()).get(0);
                supplierVO.setAdminName(user.getName());
                supplierVO.setAdminPhone(user.getMobile());
                return supplierVO;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return supplierVOS;
    }

    /**
     * 批发商pc端获取我的批发商列表
     *
     * @return 批发商列表信息
     */
    @GetMapping("/supplier/mine/web")
    public List<SupplierDTO> getMineSuppliers() {
        List<Supplier> suppliers = this.supplierService.listMySupplierTenants(SecurityContextHolder.getPrincipal().getId());
        return ObjectUtils.copying(suppliers, SupplierDTO.class);
    }

    /**
     * 小程序搜索市场
     *
     * @return 租户信息
     */
    @GetMapping("/tenant/market-mp")
    public Pagination<TenantDTO> getMarketTenants(TenantQueryParam queryParam) {
        if (StringUtils.isEmpty(queryParam.getName())) {
            return Pagination.empty();
        }
        return this.tenantClient.pageTenants(queryParam);
    }

    /**
     * pc端获取我的市场
     *
     * @return 租户信息
     */
    @GetMapping("/tenant/mine")
    @Deprecated
    public LegacyResponse<List<TenantDTO>> getMineTenantsLegacy() {
        return LegacyResponse.success(this.getMineTenants());
    }

    @GetMapping("/tenants")
    public List<TenantDTO> getMineTenants() {
        List<TenantDTO> tenants = this.tenantClient.listMyTenants();
        // 获取批发商信息
        List<SupplierUser> supplierUsers = this.supplierUserService.listSupplierUser(SecurityContextHolder.getPrincipal().getId());
        Set<Long> supplierIds = supplierUsers.stream().map(SupplierUser::getSupplierId).collect(Collectors.toSet());
        List<Supplier> suppliers = this.supplierService.listByIds(supplierIds);
        Set<Integer> supplierTenants = suppliers.stream().map(Supplier::getSupplierTenant).collect(Collectors.toSet());

        tenants = tenants.stream().map(tenantDTO -> {
            if (supplierTenants.contains(tenantDTO.getCode())) {
                return null;
            }
            return tenantDTO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return ObjectUtils.copying(tenants, TenantDTO.class);
    }

    /**
     * 更新信息（内部调用）
     *
     * @param id    主键
     * @param param 更新参数
     */
    @PutMapping("/supplier/inner/{id}")
    public void updateInnerSupplier(@PathVariable("id") Long id, @RequestBody SupplierModifyParam param) {
        this.supplierService.updateInnerSupplier(id, param);
    }

    /**
     * 根据id获取批发商信息(内部调用)
     *
     * @param id 批发商主键
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/inner/{id}")
    public SupplierDTO getSupplierById(@PathVariable("id") Long id) {
        Supplier supplier = this.supplierService.getById(id);
        return ObjectUtils.copying(supplier, SupplierDTO.class);
    }

    /**
     * 获取批发商列表（内部调用）
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/inner/list")
    public List<SupplierDTO> listSuppliers(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                           @RequestParam("tenant") Integer tenant
                                          ) {
        List<Supplier> suppliers = this.supplierService.listSupplier(keyword, tenant);
        return ObjectUtils.copying(suppliers, SupplierDTO.class);
    }

    /**
     * 获取批发商列表（外部调用）
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/out/list")
    public List<SupplierVO> listOutSuppliers(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        List<Supplier> suppliers = this.supplierService.listOutSupplier(keyword, SessionContextHolder.getRequester().getTenant());
        return ObjectUtils.copying(suppliers, SupplierVO.class);
    }

    @GetMapping("/supplier/inner/user-ids")
    public List<SupplierDTO> listByUserIds(@RequestParam("userIds") Collection<? extends Serializable> userIds,
                                           @RequestParam("tenant") Integer tenant
                                          ) {
        List<Supplier> suppliers = this.supplierService.listByUserIds(userIds, tenant);
        return ObjectUtils.copying(suppliers, SupplierDTO.class);
    }

    /**
     * 根据批发商租户编号获取批发商（内部调用）
     *
     * @param tenant 批发商租户编号
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/inner/{tenant}/supplier-tenant")
    public SupplierDTO getBySupplierTenant(@PathVariable("tenant") Integer tenant) {
        Supplier supplier = this.supplierService.getBySupplierTenant(tenant);
        return ObjectUtils.copying(supplier, SupplierDTO.class);
    }

    /**
     * 根据主键查询（内部调用）
     *
     * @param ids 主键查询
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/inner/ids")
    public List<SupplierDTO> listByIds(@RequestParam("ids") Collection<? extends Serializable> ids) {
        List<Supplier> suppliers = this.supplierService.listByIds(ids);
        return ObjectUtils.copying(suppliers, SupplierDTO.class);
    }

    /**
     * 分页查询批发商（内部调用）
     *
     * @param queryParam 查询参数
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/inner/page")
    public Pagination<SupplierDTO> pageInnerSupplier(@SpringQueryMap SupplierQueryParam queryParam) {
        Page<Supplier> page = this.supplierService.pageSupplier(queryParam);
        return PagingUtils.convert(page, SupplierDTO.class);
    }

    /**
     * 获取认证批发商信息
     *
     * @param queryParam 查询参数
     *
     * @return 批发商信息
     */
    @GetMapping("/supplier/inner/authenticated/page")
    public Pagination<SupplierDTO> pageInnerAuthenticatedSupplier(@SpringQueryMap SupplierQueryParam queryParam) {
        SupplierAuthenticationQueryParam authenticationQueryParam = new SupplierAuthenticationQueryParam();
        authenticationQueryParam.setStatus(queryParam.getStatus());
        authenticationQueryParam.setPageNumber(queryParam.getPageNumber());
        authenticationQueryParam.setPageSize(queryParam.getPageSize());
        authenticationQueryParam.setTenant(queryParam.getTenant());
        Page<SupplierAuthentication> page = this.supplierAuthenticationService.pageSupplierAuthentication(authenticationQueryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        Set<Long> supplierIds = page.getRecords().stream().map(SupplierAuthentication::getSupplierId).collect(Collectors.toSet());
        List<Supplier> suppliers = this.supplierService.listByIds(supplierIds);
        return PagingUtils.convert(page, ObjectUtils.copying(suppliers, SupplierDTO.class));
    }

    /**
     * 获取当前市场客户信息
     *
     * @param queryParam 查询参数
     *
     * @return 用户主键集合
     */
    @GetMapping("/supplier/inner/customers")
    public Pagination<UserDTO> getCustomers(@Valid SupplierQueryParam queryParam) {
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        Supplier supplier = this.supplierService.getBySupplierTenant(supplierTenant);
        queryParam.setTenant(supplier.getTenant());
        return PagingUtils.convert(this.supplierService.getCustomers(queryParam), UserDTO.class);
    }

    @GetMapping("/supplier/customers")
    public Pagination<SupplierVO> getCustomersV2(@Valid SupplierQueryParam queryParam) {
        if (StringUtils.isEmpty(queryParam.getKeyword())) {
            return Pagination.empty();
        }
        queryParam.setTenant(SessionContextHolder.getRequester().getTenant());
        return PagingUtils.convert(this.supplierService.getCustomersV2(queryParam));
    }

    /**
     * 获取认证商户信息
     *
     * @param queryParam 查询参数
     *
     * @return 认证信息
     */
    @GetMapping("/supplier/inner/authenticated/list")
    public List<SupplierDTO> listInnerAuthenticateSupplier(@SpringQueryMap SupplierQueryParam queryParam) {
        SupplierAuthenticationQueryParam authenticationQueryParam = new SupplierAuthenticationQueryParam();
        authenticationQueryParam.setStatus(queryParam.getStatus());
        authenticationQueryParam.setTenant(queryParam.getTenant());
        List<SupplierAuthentication> supplierAuthentications = this.supplierAuthenticationService.listSupplierAuthentication(authenticationQueryParam, queryParam.getTenant());
        if (CollectionUtils.isEmpty(supplierAuthentications)) {
            return Collections.emptyList();
        }
        Set<Long> supplierIds = supplierAuthentications.stream().map(SupplierAuthentication::getSupplierId).collect(Collectors.toSet());
        List<Supplier> suppliers = this.supplierService.listByIds(supplierIds);
        return ObjectUtils.copying(suppliers, SupplierDTO.class);
    }

    /**
     * 理货机获取指定商户
     *
     * @param midType  商户标识类型
     * @param midValue 商户标识值
     *
     * @return 商户信息
     */
    @GetMapping("/supplier/inner/mid-type")
    public List<SupplierDTO> getByMidType(@NotNull(message = "商户标识类型不能为空") @RequestParam("midType") String midType,
                                          @NotNull(message = "商户标识值不能为空") @RequestParam("midValue") String midValue
                                         ) {
        List<Supplier> suppliers = this.supplierService.getByMidType(midType, midValue, SessionContextHolder.getRequester().getTenant());
        return ObjectUtils.copying(suppliers, SupplierDTO.class);
    }

    /**
     * 获取市场列表信息
     *
     * @return
     */
    @GetMapping("/public/market/list")
    public List<TenantDTO> listMarketTenant(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        List<TenantDTO> tenants = this.tenantClient.publicListTenants(keyword);
        Set<Integer> supplierTenants =
                this.supplierService.list().stream().map(Supplier::getSupplierTenant).collect(Collectors.toSet());
        return tenants.stream().map(tenant -> {
            if (supplierTenants.contains(tenant.getCode())) {
                return null;
            }
            return tenant;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @GetMapping("/supplier/accounts/v2")
    public Pagination<SupplierAccountVO> listMerchantAccounts(@Valid AccountQueryParam param) {
        // 根据关键字查找商户
        Set<Long> userIds = null;
        List<SupplierVO> suppliers = null;
        if (StringUtils.isNotEmpty(param.getKeyword())) {
            suppliers = supplierService.listCustomersV2(SupplierQueryParam.builder().tenant(param.getTenant()).keyword(param.getKeyword())
                                                                .build());
            if (CollectionUtils.isEmpty(suppliers)) {
                return Pagination.empty();
            } else {
                userIds = suppliers.stream().map(SupplierVO::getAdminUserId).collect(Collectors.toSet());
            }
        }
        // 查找商户对应的钱包账户
        WalletAccountQueryParam query = ObjectUtils.copying(param, WalletAccountQueryParam.class);
        query.setUserIds(userIds);
        Pagination<WalletAccountDTO> pagination = this.accountClient.listWalletAccounts(query);
        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return Pagination.empty();
        }

        Map<Long, SupplierVO> supplierMap = (suppliers == null ? new HashMap<>() : suppliers.stream()
                .collect(Collectors.toMap(SupplierVO::getAdminUserId, m -> m, (o, n) -> n)));

        // 构建商户账户分页信息
        List<SupplierAccountVO> accounts = pagination.getRecords().stream().map(record -> {
            SupplierAccountVO account = ObjectUtils.copying(record, SupplierAccountVO.class);
            account.setId(record.getUserId());//设置用户ID，通过用户ID 查询账户流水
            //是否免密支付
            WalletAccountDTO.QuickPayment quickPayment = record.getQuickPayment();
            account.setPaymentQuickly(ObjectUtils.ifNull(quickPayment, WalletAccountDTO.QuickPayment::getEnabled));
            //处理商家信息
            SupplierVO supplier = supplierMap.get(record.getUserId());
            if (supplier != null) {
                account.setName(supplier.getName());
                account.setLiaison(supplier.getLiaison());
                account.setPhone(supplier.getPhone());
                account.setAdminUserName(supplier.getAdminName());
                account.setAdminPhone(supplier.getAdminPhone());
            } else {
                UserDTO user = userClient.getUser(record.getUserId());
                if (user != null) {
                    account.setAdminUserName(user.getName());
                    account.setAdminPhone(user.getMobile());
                }
            }
            return account;
        }).collect(Collectors.toList());
        return PagingUtils.convert(pagination, accounts);
    }
}
