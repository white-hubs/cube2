package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.TenantCategory;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantProduct;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.param.TenantModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserTenantAddParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserTenantQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserTenantUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantProductService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ProductPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TenantVO;
import com.guhecloud.cube.biz.ouaa.server.vo.UserTenantVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
public class TenantController {

    private final TenantService tenantService;
    private final UserTenantService userTenantService;
    private final UserService userRepository;
    private final FileClient fileClient;
    private final TenantProductService tenantProductService;
    private final TenantApplicationService tenantApplicationService;
    private final ProductService productService;
    private final ApplicationService applicationService;

    @PostMapping("/tenant")
    public void addTenant(@RequestBody @Valid TenantModifyParam param) {
        param.setCategory(param.getCategory() != null ? param.getCategory() : TenantCategory.MARKET);
        this.tenantService.addTenant(param);
    }

    @PutMapping("/tenant/{id}")
    public void updateTenant(@PathVariable("id") @Min(value = 1, message = "租户主键不能小于1") Long id,
                             @RequestBody @Valid TenantModifyParam param
                            ) {
        param.setCategory(param.getCategory() != null ? param.getCategory() : TenantCategory.MARKET);
        this.tenantService.updateTenant(id, param);
    }

    @PutMapping("/tenant/{id}/status/{status}")
    public void updateTenant(@PathVariable("id") Long id, @PathVariable("status") Boolean status) {
        Tenant tenant = this.tenantService.getById(id);
        Assert.isNotFound(tenant, "未查询到租户");
        tenant.setEnabled(status);
        this.tenantService.updateById(tenant);
    }

    @DeleteMapping("/tenant/{id}")
    public void deleteTenant(@PathVariable("id") Long id) {
        this.tenantService.deleteTenant(id);
    }

    @GetMapping("/tenant/{code}")
    public TenantVO getByCode(@PathVariable("code") Integer code) {
        Tenant tenant = this.tenantService.getByCode(code);
        TenantVO tenantVO = ObjectUtils.copying(tenant, TenantVO.class);
        if (StringUtils.isNotEmpty(tenant.getLocation())) {
            tenantVO.setLocations(Arrays.stream(tenant.getLocation().split("-")).collect(Collectors.toList()));
        }
        UserTenant userTenant = this.userTenantService.getUserTenantAdmin(tenant.getCode());
        if (userTenant != null) {
            User user = this.userRepository.getById(userTenant.getUserId());
            tenantVO.setUserAdminId(user == null ? 0 : user.getId());
            tenantVO.setUsername(user == null ? "" : user.getName());
        }

        // 获取文件信息
        if (StringUtils.isEmpty(tenant.getFile())) {
            return tenantVO;
        }
        List<Long> fileIds = Arrays.stream(tenant.getFile().split("-")).map(Long::parseLong).collect(Collectors.toList());
        fileIds.add(tenant.getLogoId());
        List<FileVO> files = this.fileClient.getByIds(fileIds);
        Map<Long, List<FileVO>> fileMap = files.stream().collect(Collectors.groupingBy(FileVO::getId));

        if (fileMap.containsKey(tenant.getLogoId())) {
            FileVO fileVO = fileMap.get(tenant.getLogoId()).get(0);
            tenantVO.setLogoId(tenant.getLogoId());
            tenantVO.setLogo(fileVO);
        }
        // 获取宣传图信息
        tenantVO.setFileIds(Arrays.stream(tenant.getFile().split("-")).collect(Collectors.toList()));
        List<FileVO> fileVOS = tenantVO.getFileIds().stream().map(fileId -> {
            if (fileMap.containsKey(Long.parseLong(fileId))) {
                FileVO fileVO = fileMap.get(Long.parseLong(fileId)).get(0);
                return fileVO;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        tenantVO.setFiles(fileVOS);

        return tenantVO;
    }

    /**
     * 查询租户
     *
     * @param param 查询参数
     *
     * @return 租户列表分页
     */
    @GetMapping("/tenant/page")
    public Pagination<TenantVO> listByPage(@Valid TenantQueryParam param) {
        Page<Tenant> page = this.tenantService.listTenants(param);
        List<TenantVO> list = ObjectUtils.copying(page.getRecords(), TenantVO.class);
        if (CollectionUtils.isEmpty(list)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        list.stream().forEach(tenantVO -> {
            UserTenant userTenant = this.userTenantService.getUserTenantAdmin(tenantVO.getCode());
            if (userTenant != null) {
                User user = this.userRepository.getById(userTenant.getUserId());
                tenantVO.setUserAdminId(user == null ? 0 : user.getId());
                tenantVO.setUsername(user == null ? "" : user.getName());
            }
        });
        return PagingUtils.convert(page, list);
    }

    @GetMapping("/tenant/{tenant}/products")
    @Deprecated
    public List<ProductPageVO> listTenantProducts(@PathVariable("tenant") Integer tenant) {
        List<TenantProduct> tenantProducts = this.tenantProductService.listByTenant(tenant);
        List<Long> productIds = tenantProducts.stream().map(item -> item.getProductId()).collect(Collectors.toList());
        List<Product> products = this.productService.listByIds(productIds);
        if (CollectionUtils.isEmpty(products)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(products, ProductPageVO.class);
    }

    @PostMapping("/tenant/{tenant}/product-mapping")
    @Deprecated
    public void saveTenantProductMapping(@PathVariable("tenant") Integer tenant, @RequestParam("productId") Long productId) {
        this.tenantProductService.save(tenant, productId);
    }

    @DeleteMapping("/tenant/{tenant}/product-mapping")
    @Deprecated
    public void removeTenantProductMapping(@PathVariable("tenant") Integer tenant, @RequestParam("productId") Long productId) {
        this.tenantProductService.remove(tenant, productId);
    }

    @GetMapping("/tenant/{tenant}/product-applications")
    public List<ApplicationPageVO> listTenantApplications(@PathVariable("tenant") Integer tenant
                                                         ) {
        List<TenantApplication> tenantProductApplications = this.tenantApplicationService.listByTenantProduct(tenant);
        List<Long> applicationIds = tenantProductApplications.stream().map(item -> item.getApplicationId()).collect(Collectors.toList());
        List<Application> applications = this.applicationService.listByIds(applicationIds);
        if (CollectionUtils.isEmpty(applications)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(applications, ApplicationPageVO.class);
    }

    @PostMapping("/tenant/{tenant}/product-application-mapping")
    public void saveTenantApplicationMapping(@PathVariable("tenant") Integer tenant,
                                             @RequestParam(value = "productId", required = false) Long productId,
                                             @RequestParam("applicationId") Long applicationId
                                            ) {
        this.tenantApplicationService.save(tenant, productId, applicationId);
    }

    @DeleteMapping("/tenant/{tenant}/product-application-mapping")
    public void removeTenantApplicationMapping(@PathVariable("tenant") Integer tenant,
                                               @RequestParam(value = "productId", required = false) Long productId,
                                               @RequestParam("applicationId") Long applicationId
                                              ) {
        this.tenantApplicationService.remove(tenant, productId, applicationId);
    }

    @GetMapping("/application/{applicationCode}/tenants")
    public List<TenantVO> getApplicationTenant(@PathVariable("applicationCode") String applicationCode) {
        Application applicationServiceByCode = this.applicationService.getByCode(applicationCode);
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByApplication(applicationServiceByCode.getId());
        if (CollectionUtils.isEmpty(tenantApplications)) {
            return Collections.emptyList();
        }
        List<Tenant> tenants = this.tenantService.listByCodes(tenantApplications.stream().map(TenantApplication::getTenant)
                                                                      .collect(Collectors.toList()));
        return ObjectUtils.copying(tenants, TenantVO.class);
    }

    @GetMapping("/tenants/{tenant}/users")
    public Pagination<UserTenantVO> listTenantUsers(@PathVariable("tenant") Integer tenant, @SpringQueryMap UserTenantQueryParam param) {
        var tenantUsers = userTenantService.pageUserTenants(tenant, param);
        // TODO: 穿层问题
        var users = userRepository
                .lambdaQuery()
                .in(User::getId, tenantUsers.getRecords().stream().map(UserTenant::getUserId).toList())
                .list();
        var userTenantVOs = tenantUsers.getRecords().stream()
                .map((ut) -> {
                    var user = users.stream().filter(u -> Objects.equals(u.getId(), ut.getUserId())).findFirst().orElse(null);
                    return new UserTenantVO(ut.getId(), ut.getTenant(), ut.getUserId(), Optional.ofNullable(ut.getEnabled())
                            .orElse(Boolean.TRUE), ut.getAdmin(), user);
                })
                .toList();
        return new Pagination<>(tenantUsers.getTotal(), tenantUsers.getPages(), userTenantVOs);
    }

    @PutMapping("/tenants/{tenant}/users/{userId}")
    public void updateTenantUser(@PathVariable("tenant") Integer tenant,
                                 @PathVariable("userId") Long userId,
                                 @RequestBody UserTenantUpdateParam param
                                ) {
        // TODO: 暂时只支持部分更新功能，后续需要的时候再扩展
        this.userTenantService.lambdaUpdate()
                .eq(UserTenant::getUserId, userId)
                .eq(UserTenant::getTenant, tenant)
                .set(UserTenant::getEnabled, param.enabled())
                .set(UserTenant::getAdmin, param.admin())
                .update();
    }

    @PostMapping("/tenants/{tenant}/users")
    public void addTenantUsers(@PathVariable("tenant") Integer tenant,
                               @RequestBody UserTenantAddParam userTenantAddParam
                              ) {
        this.userTenantService.addUserTenantBatch(tenant, userTenantAddParam.userIds());
    }

    @DeleteMapping("/tenants/{tenant}/users/{userId}")
    public void removeTenantUser(@PathVariable("tenant") Integer tenant,
                                 @PathVariable("userId") Long userId
                                ) {
        this.userTenantService.removeUserTenant(tenant, userId);
    }

    @GetMapping("/tenants/{tenant}/users/list-no-exist")
    public Pagination<User> listUsersNotInTenant(@PathVariable("tenant") Integer tenant,
                                                 @SpringQueryMap UserQueryParam param
                                                ) {
        var existingUserTenants = this.userTenantService.listUserTenants(tenant);
        param.setFilterUserIds(existingUserTenants.stream().map(UserTenant::getUserId).collect(Collectors.toList()));
        return PagingUtils.convert(userRepository.getByPage(param));
    }
}
