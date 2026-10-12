package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Product;
import com.guhecloud.cube.biz.ouaa.server.entity.ProductApplication;
import com.guhecloud.cube.biz.ouaa.server.param.ProductModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ProductQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.ProductService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ProductPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ProductVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
@Deprecated
public class ProductController {

    private final ApplicationService applicationService;
    private final ProductService productService;
    private final ProductApplicationService productApplicationService;

    @PostMapping("/product")
    public Long save(@Valid @RequestBody ProductModifyParam param) {
        return productService.save(param);
    }

    @PutMapping("/product/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ProductModifyParam param) {
        productService.update(id, param);
    }

    @DeleteMapping("/product/{id}")
    public void remove(@PathVariable("id") Long id) {
        productService.remove(id);
    }

    @GetMapping("/product/{id}")
    public ProductVO getById(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        Assert.isNotFound(product, "不存在产品信息");
        return ObjectUtils.copying(product, ProductVO.class);
    }

    @GetMapping("/products")
    public Pagination<ProductPageVO> listByPage(@Valid ProductQueryParam param) {
        Page<Product> page = this.productService.listByPage(param);
        List<ProductPageVO> products = ObjectUtils.copying(page.getRecords(), ProductPageVO.class);
        if (CollectionUtils.isEmpty(products)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, products);
    }

    @GetMapping("/product/{id}/applications")
    public List<ApplicationPageVO> listProductApplications(@PathVariable("id") Long productId) {
        List<ProductApplication> productApplications = this.productApplicationService.listByProduct(productId);
        List<Long> applicationIds = productApplications.stream().map(item -> item.getApplicationId()).collect(Collectors.toList());
        List<Application> applications = this.applicationService.listByIds(applicationIds);
        if (CollectionUtils.isEmpty(applications)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(applications, ApplicationPageVO.class);
    }

    @PostMapping("/product/{id}/application-mapping")
    public void saveProductApplicationMapping(@PathVariable("id") Long productId, @RequestParam("applicationId") Long applicationId) {
        productApplicationService.save(productId, applicationId);
    }

    @DeleteMapping("/product/{id}/application-mapping")
    public void removeProductApplicationMapping(@PathVariable("id") Long productId, @RequestParam("applicationId") Long applicationId) {
        productApplicationService.remove(productId, applicationId);
    }

}
