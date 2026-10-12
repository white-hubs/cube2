package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.guhecloud.cube.base.iot.product.model.v3.param.PropSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductPropService;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductPropVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 属性管理Controller v3版本
 * 路径前缀: /v3/things/product-prop
 */
@Slf4j
@RestController
@RequestMapping("/v3/things/product-prop")
@RequiredArgsConstructor
public class V3ProductPropController {

    private final V3MdProductPropService propService;

    /**
     * 创建属性
     *
     * @param param 属性保存参数
     * @return 属性ID
     */
    @PostMapping
    public Long createProp(@Validated @RequestBody PropSaveParam param) {
        log.info("创建属性接口调用, prodId={}, propCode={}", param.getProdId(), param.getPropCode());
        return propService.saveProp(param);
    }

    /**
     * 更新属性
     *
     * @param id 属性ID
     * @param param 属性更新参数
     */
    @PutMapping("/{id}")
    public void updateProp(@PathVariable Long id, @Validated @RequestBody PropSaveParam param) {
        log.info("更新属性接口调用, propId={}", id);
        propService.updateProp(id, param);
    }

    /**
     * 删除属性
     *
     * @param id 属性ID
     */
    @DeleteMapping("/{id}")
    public void deleteProp(@PathVariable Long id) {
        log.info("删除属性接口调用, propId={}", id);
        propService.deleteProp(id);
    }

    /**
     * 查询属性详情
     *
     * @param id 属性ID
     * @return 属性VO
     */
    @GetMapping("/{id}")
    public ProductPropVO getProp(@PathVariable Long id) {
        log.info("查询属性详情接口调用, propId={}", id);
        return propService.getPropDetail(id);
    }

    /**
     * 查询属性列表(按产品ID)
     *
     * @param prodId 产品ID
     * @return 属性列表
     */
    @GetMapping("/list")
    public List<ProductPropVO> listByProduct(@RequestParam Long prodId) {
        log.info("查询产品属性列表接口调用, prodId={}", prodId);
        return propService.listByProdId(prodId);
    }

    /**
     * 批量创建属性
     *
     * @param prodId 产品ID
     * @param params 属性保存参数列表
     */
    @PostMapping("/batch")
    public void batchCreate(@RequestParam Long prodId, @Validated @RequestBody List<PropSaveParam> params) {
        log.info("批量创建属性接口调用, prodId={}, count={}", prodId, params.size());
        propService.batchSave(prodId, params);
    }
}
