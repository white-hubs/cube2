package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.guhecloud.cube.base.iot.product.model.v3.param.MethodSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductMethodService;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductMethodVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 方法管理Controller v3版本(新增功能)
 * 路径前缀: /v3/things/product-method
 */
@Slf4j
@RestController
@RequestMapping("/v3/things/product-method")
@RequiredArgsConstructor
public class V3ProductMethodController {

    private final V3MdProductMethodService methodService;

    /**
     * 创建方法
     *
     * @param param 方法保存参数
     * @return 方法ID
     */
    @PostMapping
    public Long createMethod(@Validated @RequestBody MethodSaveParam param) {
        log.info("创建方法接口调用, prodId={}, methodCode={}", param.getProdId(), param.getMethodCode());
        return methodService.saveMethod(param);
    }

    /**
     * 更新方法
     *
     * @param id 方法ID
     * @param param 方法更新参数
     */
    @PutMapping("/{id}")
    public void updateMethod(@PathVariable Long id, @Validated @RequestBody MethodSaveParam param) {
        log.info("更新方法接口调用, methodId={}", id);
        methodService.updateMethod(id, param);
    }

    /**
     * 删除方法
     *
     * @param id 方法ID
     */
    @DeleteMapping("/{id}")
    public void deleteMethod(@PathVariable Long id) {
        log.info("删除方法接口调用, methodId={}", id);
        methodService.deleteMethod(id);
    }

    /**
     * 查询方法详情
     *
     * @param id 方法ID
     * @return 方法VO
     */
    @GetMapping("/{id}")
    public ProductMethodVO getMethod(@PathVariable Long id) {
        log.info("查询方法详情接口调用, methodId={}", id);
        return methodService.getMethodDetail(id);
    }

    /**
     * 查询方法列表(按产品ID)
     *
     * @param prodId 产品ID
     * @return 方法列表
     */
    @GetMapping("/list")
    public List<ProductMethodVO> listByProduct(@RequestParam Long prodId) {
        log.info("查询产品方法列表接口调用, prodId={}", prodId);
        return methodService.listByProdId(prodId);
    }

    /**
     * 批量创建方法
     *
     * @param prodId 产品ID
     * @param params 方法保存参数列表
     */
    @PostMapping("/batch")
    public void batchCreate(@RequestParam Long prodId, @Validated @RequestBody List<MethodSaveParam> params) {
        log.info("批量创建方法接口调用, prodId={}, count={}", prodId, params.size());
        methodService.batchSave(prodId, params);
    }
}
