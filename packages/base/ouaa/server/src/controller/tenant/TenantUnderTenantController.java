package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantProduct;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantUpdateParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantProductService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.cube.biz.ouaa.server.utils.CustomConfigUtil;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ProductPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TenantVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantUnderTenantController {

    private final TenantService tenantService;
    private final UserTenantService userTenantService;
    private final UserService userService;
    private final FileClient fileClient;
    private final TenantProductService tenantProductService;
    private final TenantApplicationService tenantApplicationService;
    private final ProductService productService;
    private final ApplicationService applicationService;
    private final CustomConfigUtil customConfigUtil;

    @PutMapping("/tenant-under-tenant")
    public void update(@RequestBody TenantUpdateParam param) {
        this.tenantService.update(param);
    }

    @GetMapping("/tenant-under-tenant/{code}")
    public TenantVO getByCode(@PathVariable("code") Integer code) {
        Tenant tenant = this.tenantService.getByCode(code);
        TenantVO tenantVO = ObjectUtils.copying(tenant, TenantVO.class);
        UserTenant userTenant = this.userTenantService.getUserTenantAdmin(tenant.getCode());
        if (userTenant != null) {
            User user = this.userService.getById(userTenant.getUserId());
            tenantVO.setUserAdminId(user == null ? 0 : user.getId());
            tenantVO.setUsername(user == null ? "" : user.getName());
        }
        if (StringUtils.isNotEmpty(tenant.getLocation())) {
            tenantVO.setLocations(Arrays.stream(tenant.getLocation().split("-")).collect(Collectors.toList()));
        }
        if (ObjectUtils.isNotNull(tenant.getLogoId())) {
            // 获取logo图片信息
            FileVO logo = this.fileClient.getById(tenant.getLogoId());
            tenantVO.setLogo(logo);
        }
        // 获取宣传图信息
        if (StringUtils.isNotEmpty(tenant.getFile())) {
            List<Long> fileIds = Arrays.stream(tenant.getFile().split("-")).map(Long::parseLong).collect(Collectors.toList());
            List<FileVO> byIds = fileClient.getByIds(fileIds);
            tenantVO.setFiles(byIds);
        }

        return tenantVO;
    }

    @GetMapping("/tenant-under-tenant/{tenant}/products")
    public List<ProductPageVO> listTenantProducts(@PathVariable("tenant") Integer tenant) {
        List<TenantProduct> tenantProducts = this.tenantProductService.listByTenant(tenant);
        List<Long> productIds = tenantProducts.stream().map(item -> item.getProductId()).collect(Collectors.toList());
        List<Product> products = this.productService.listByIds(productIds);
        if (CollectionUtils.isEmpty(products)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(products, ProductPageVO.class);
    }

    /**
     * @param tenant
     * @param alisaFlag 自定义配置覆盖标识、ture覆盖、false不覆盖
     *
     * @return
     */
    @GetMapping("/tenant-under-tenant/{tenant}/product-applications")
    public List<ApplicationPageVO> listTenantProductApplications(@PathVariable("tenant") Integer tenant,
                                                                 @RequestParam(value = "alisaFlag", defaultValue = "true") boolean alisaFlag
                                                                ) {
        List<TenantApplication> tenantProductApplications = this.tenantApplicationService.listByTenantProduct(tenant);
        List<Long> applicationIds = tenantProductApplications.stream().map(item -> item.getApplicationId()).collect(Collectors.toList());
        List<Application> applications = this.applicationService.listByIds(applicationIds);
        if (CollectionUtils.isEmpty(applications)) {
            return Collections.emptyList();
        }
        List<ApplicationPageVO> copying = ObjectUtils.copying(applications, ApplicationPageVO.class);
        customConfigUtil.applicationCustomConfigResolving(copying, alisaFlag);
        return copying;
    }

    /**
     * 查询tenant下的所有应用
     *
     * @return
     */
    @GetMapping("/tenant-under-tenant/applications")
    public Pagination<ApplicationVO> listTenantApplications(ApplicationQueryParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        //查询租户下的所有应用
        List<TenantApplication> tenantApplications = this.tenantApplicationService.lambdaQuery().eq(TenantApplication::getTenant, tenant)
                .list();
        if (!CollectionUtils.isEmpty(tenantApplications)) {
            List<Long> collect = tenantApplications.stream().map(TenantApplication::getApplicationId).distinct()
                    .collect(Collectors.toList());
            Page<Application> page = this.applicationService.lambdaQuery()
                    .in(Application::getId, collect)
                    .like(StringUtils.isNotEmpty(param.getKeyword()), Application::getName, param.getKeyword())
                    .page(PagingUtils.convert(param));
            if (!CollectionUtils.isEmpty(page.getRecords())) {
                return PagingUtils.convert(page, ObjectUtils.copying(page.getRecords(), ApplicationVO.class));
            }
        }
        return Pagination.empty();
    }

    /**
     * 获取当前租户的投诉电话
     */
    @GetMapping("/public/tenant-under-tenant/complaint-phone")
    public String getComplaintPhone() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        return this.tenantService.getComplaintPhone(tenant);
    }
}
