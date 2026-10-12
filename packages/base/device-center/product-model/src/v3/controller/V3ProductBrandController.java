package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductBrandModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductBrandService;
import com.guhecloud.cube.base.iot.product.model.v3.util.PageAdapter;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 产品品牌管理Controller
 * 提供品牌CRUD接口,支持文件关联
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/v3/product-brand")
@RequiredArgsConstructor
public class V3ProductBrandController {

    private final V3ProductBrandService productBrandService;

    /**
     * 分页查询品牌(包含文件信息)
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Pagination<DictItemVO> page(DictItemPageParam param) {
        log.info("分页查询品牌, param={}", param);
        // 自动加载关联文件
        param.setWithAssociatedItem(true);
        return PageAdapter.toStandard(productBrandService.page(param));
    }

    /**
     * 查询品牌详情(包含文件信息)
     *
     * @param id 品牌ID
     * @return 品牌详情
     */
    @GetMapping("/{id}")
    public DictItemVO getById(@PathVariable Long id) {
        log.info("查询品牌详情, id={}", id);
        return productBrandService.getById(id);
    }

    /**
     * 新增品牌
     *
     * @param param 新增参数
     */
    @PostMapping
    public void save(@Valid @RequestBody ProductBrandModifyParam param) {
        log.info("新增品牌, param={}", param);
        productBrandService.save(param);
    }

    /**
     * 修改品牌
     *
     * @param id 品牌ID
     * @param param 修改参数
     */
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody ProductBrandModifyParam param) {
        log.info("修改品牌, id={}, param={}", id, param);
        productBrandService.update(id, param);
    }

    /**
     * 删除品牌
     *
     * @param id 品牌ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("删除品牌, id={}", id);
        productBrandService.delete(id);
    }
}
