package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductService;
import com.guhecloud.cube.base.iot.product.model.v3.util.PageAdapter;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 产品管理Controller v3版本
 * 路径前缀: /v3/things/product
 */
@Slf4j
@RestController
@RequestMapping("/v3/things/product")
@RequiredArgsConstructor
public class V3ProductController {

    private final V3MdProductService productService;

    /**
     * 创建产品
     *
     * @param param 产品保存参数
     * @return 产品ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createProduct(@Validated @RequestBody ProductSaveParam param) {
        log.info("创建产品接口调用, prodName={}", param.getProdName());
        return productService.saveProduct(param);
    }

    /**
     * 更新产品, TODO: 使用productNumber 替代产品协议！！！！
     *
     * @param id 产品ID
     * @param param 产品更新参数
     */
    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @Validated @RequestBody ProductSaveParam param) {
        log.info("更新产品接口调用, prodId={}", id);
        productService.updateProduct(id, param);
    }

    /**
     * 删除产品
     *
     * @param id 产品ID
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        log.info("删除产品接口调用, prodId={}", id);
        productService.deleteProduct(id);
    }

    /**
     * 查询产品详情
     *
     * @param id 产品ID
     * @return 产品VO(包含属性、事件、方法)
     */
    @GetMapping("/{id}")
    public ProductVO getProduct(@PathVariable Long id) {
        log.info("查询产品详情接口调用, prodId={}", id);
        return productService.getProductDetail(id);
    }

    /**
     * 分页查询产品
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Pagination<ProductVO> queryProductPage(ProductPageParam param) {
        log.info("分页查询产品接口调用, param={}", param);
        return PageAdapter.toStandard(productService.queryProductPage(param));
    }

    /**
     * 查询所有产品列表
     *
     * @return 产品列表
     */
    @GetMapping("/list/all")
    public Pagination<ProductVO> listAll() {
        log.info("查询所有产品列表接口调用");
        ProductPageParam param = new ProductPageParam();
        param.setPageSize(1000); // 默认返回1000条
        return PageAdapter.toStandard(productService.queryProductPage(param));
    }
}
