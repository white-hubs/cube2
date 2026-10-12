package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductCategoryModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductCategoryService;
import com.guhecloud.cube.base.iot.product.model.v3.util.PageAdapter;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemDetailWithPathVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemTreeVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品品类管理Controller
 * 提供品类CRUD接口,支持树形结构查询
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/v3/product-category")
@RequiredArgsConstructor
public class V3ProductCategoryController {

    private final V3ProductCategoryService productCategoryService;

    /**
     * 分页查询品类
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Pagination<DictItemVO> page(DictItemPageParam param) {
        log.info("分页查询品类, param={}", param);
        return PageAdapter.toStandard(productCategoryService.page(param));
    }

    /**
     * 查询品类详情
     *
     * @param id 品类ID
     * @return 品类详情
     */
    @GetMapping("/{id}")
    public DictItemVO getById(@PathVariable Long id) {
        log.info("查询品类详情, id={}", id);
        return productCategoryService.getById(id);
    }

    /**
     * 查询品类完整详情(包含从根节点到当前节点的路径)
     *
     * @param id 品类ID
     * @param includeChildren 是否包含当前节点的子节点，默认false
     * @return 包含路径和详情的对象
     */
    @GetMapping("/{id}/full-detail")
    public DictItemDetailWithPathVO getFullDetail(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") Boolean includeChildren) {
        log.info("查询品类完整详情, id={}, includeChildren={}", id, includeChildren);
        return productCategoryService.getDetailWithPath(id, includeChildren);
    }

    /**
     * 查询树形品类
     *
     * @param dictItemId 指定节点ID(可选),为null时查询整棵树
     * @return 树形品类列表
     */
    @GetMapping("/tree")
    public List<DictItemTreeVO> listTree(
            @RequestParam(required = false) Long dictItemId) {
        log.info("查询树形品类, dictItemId={}", dictItemId);
        return productCategoryService.listTree(dictItemId);
    }

    /**
     * 新增品类
     *
     * @param param 新增参数
     */
    @PostMapping
    public void save(@Valid @RequestBody ProductCategoryModifyParam param) {
        log.info("新增品类, param={}", param);
        productCategoryService.save(param);
    }

    /**
     * 修改品类
     *
     * @param id 品类ID
     * @param param 修改参数
     */
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody ProductCategoryModifyParam param) {
        log.info("修改品类, id={}, param={}", id, param);
        productCategoryService.update(id, param);
    }

    /**
     * 删除品类
     *
     * @param id 品类ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("删除品类, id={}", id);
        productCategoryService.delete(id);
    }
}
