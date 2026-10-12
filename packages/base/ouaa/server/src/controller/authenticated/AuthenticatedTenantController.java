package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.TenantCategory;
import com.guhecloud.cube.biz.ouaa.server.dto.TenantDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.param.TenantModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TenantVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 租户管理接口
 *
 * @author lzy
 * @date 2021/12/3
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class AuthenticatedTenantController {

    private final TenantService tenantService;
    private final UserTenantService userTenantService;
    private final TenantApplicationService tenantApplicationService;
    private final ApplicationService applicationService;

    private final FileClient fileClient;

    /**
     * 新增租户
     *
     * @param param 新增参数
     *
     * @return 租户信息
     */
    @PostMapping("/authenticated/tenant")
    public TenantDTO addTenant(@Valid @RequestBody TenantModifyParam param) {
        param.setCategory(param.getCategory() != null ? param.getCategory() : TenantCategory.SUPPLIER);
        Tenant tenant = this.tenantService.addTenant(param);
        return ObjectUtils.copying(tenant, TenantDTO.class);
    }

    @PutMapping("/authenticated/tenant/{code}")
    public TenantDTO updateTenant(@PathVariable("code") Integer code, @Valid @RequestBody TenantModifyParam param) {
        Tenant tenant = this.tenantService.getByCode(code);
        ObjectUtils.copying(param, tenant);
        this.tenantService.updateById(tenant);
        return ObjectUtils.copying(tenant, TenantDTO.class);
    }

    @GetMapping("/authenticated/tenant/{code}/code")
    public TenantDTO getByCode(@PathVariable("code") Integer code) {
        Tenant tenant = this.tenantService.getByCode(code);
        TenantDTO tenantDTO = ObjectUtils.copying(tenant, TenantDTO.class);
        if (StringUtils.isEmpty(tenant.getFile())) {
            return tenantDTO;
        }
        // 获取文件信息
        List<Long> fileIds = Arrays.stream(tenant.getFile().split("-")).map(Long::parseLong).collect(Collectors.toList());
        fileIds.add(tenant.getLogoId());
        List<FileVO> files = this.fileClient.getByIds(fileIds);
        Map<Long, List<FileVO>> fileMap = files.stream().collect(Collectors.groupingBy(FileVO::getId));

        // 构建logo与宣传图
        if (fileMap.containsKey(tenant.getLogoId())) {
            FileVO logo = fileMap.get(tenant.getLogoId()).get(0);
            tenantDTO.setLogo(logo);
        }
        // 获取宣传图信息
        List<FileVO> fileVOS = Arrays.stream(tenant.getFile().split("-")).map(fileId -> {
            if (fileMap.containsKey(Long.parseLong(fileId))) {
                FileVO fileVO = fileMap.get(Long.parseLong(fileId)).get(0);
                return fileVO;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        tenantDTO.setFiles(fileVOS);

        return tenantDTO;
    }

    @GetMapping("/authenticated/tenants")
    public Pagination<TenantVO> pageTenant(@RequestParam("name") String name, Paging paging) {
        Page<Tenant> page = this.tenantService.lambdaQuery()
                .like(StringUtils.isNotEmpty(name), Tenant::getName, name)
                .eq(Tenant::getEnabled, true)
                .page(PagingUtils.convert(paging));
        return PagingUtils.convert(page, TenantVO.class);
    }

    @GetMapping("/authenticated/tenant/mine")
    public List<TenantDTO> listMyTenants() {
        Principal principal = SecurityContextHolder.getPrincipal();
        List<UserTenant> userTenants = this.userTenantService.listUserTenants(principal.getId());
        //过滤该用户被禁用的租户
        userTenants = userTenants.stream().filter(userTenant -> Objects.isNull(userTenant.getEnabled()) ||
                                                                Objects.equals(userTenant.getEnabled(), Boolean.TRUE))
                .collect(Collectors.toList());

        Map<Integer, List<UserTenant>> userTenantMap = userTenants.stream().collect(Collectors.groupingBy(UserTenant::getTenant));
        List<Integer> tenantCodes = userTenants.stream().map(UserTenant::getTenant).collect(Collectors.toList());
        List<Integer> mainEnterpriseTenants = userTenants.stream().filter(UserTenant::getMainEnterprise).map(UserTenant::getTenant)
                .collect(Collectors.toList());
        List<Integer> recentLoginTenants = userTenants.stream().filter(UserTenant::getRecentLogin).map(UserTenant::getTenant)
                .collect(Collectors.toList());
        List<Tenant> tenants = this.tenantService.listTenantsByCode(tenantCodes);
        return tenants.stream().map(tenant -> {
            TenantDTO tenantDTO = ObjectUtils.copying(tenant, TenantDTO.class);
            if (userTenantMap.containsKey(tenant.getCode())) {
                UserTenant userTenant = userTenantMap.get(tenant.getCode()).get(0);
                tenantDTO.setAdmin(userTenant.getAdmin());
                tenantDTO.setRecentLogin(recentLoginTenants.contains(tenantDTO.getCode()));
                tenantDTO.setMainEnterprise(mainEnterpriseTenants.contains(tenantDTO.getCode()));
            }
            return tenantDTO;
        }).collect(Collectors.toList());
    }

    @GetMapping("/authenticated/tenant/list")
    public List<TenantDTO> listTenants(@RequestParam(value = "keyword", required = false) String keyword) {
        List<Tenant> tenants = this.tenantService.listTenants(keyword, null);
        return ObjectUtils.copying(tenants, TenantDTO.class);
    }

    @GetMapping("/public/tenant/list")
    public List<TenantDTO> publicListTenants(@RequestParam("keyword") String keyword) {
        List<Tenant> tenants = this.tenantService.listTenants(keyword, null);
        return ObjectUtils.copying(tenants, TenantDTO.class);
    }

    @GetMapping("/authenticated/tenant/page")
    public Pagination<TenantDTO> listTenants(@SpringQueryMap TenantQueryParam queryParam) {
        Page<Tenant> page = this.tenantService.listTenantsWithMarket(queryParam);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page, TenantDTO.class);
    }

    @GetMapping("/authenticated/tenant/codes")
    public List<TenantDTO> listByCodes(@RequestParam("codes") Collection<Integer> codes) {
        List<Tenant> tenants = this.tenantService.listByCodes(codes);
        return ObjectUtils.copying(tenants, TenantDTO.class);
    }

    @GetMapping("/authenticated/tenant-applications/{code}")
    public List<ApplicationVO> getTenantApplications(@PathVariable Integer code) {
        //获取租户下的应用
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenant(code);
        if (CollectionUtils.isEmpty(tenantApplications)) {
            return Collections.emptyList();
        }
        Set<Long> applicationIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        List<Application> applications = this.applicationService.listByIds(applicationIds);
        return ObjectUtils.copying(applications, ApplicationVO.class);
    }

    @GetMapping("/authenticated/tenant/category/list")
    public List<TenantDTO> listTenantsByCategory(@RequestParam(value = "category") String category) {
        List<Tenant> tenants = this.tenantService.listTenants(null, category);
        return ObjectUtils.copying(tenants, TenantDTO.class);
    }

}
