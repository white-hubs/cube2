package com.guhecloud.cube.iot.mgt.things.controller;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.cube.iot.mgt.things.server.MdProductMethodService;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodBatchVo;
import com.guhecloud.cube.iot.mgt.things.vo.ProductMethodMgtVo;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

/**
 * 产品方法管理接口
 */
@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/things/product-method")
public class ProductMethodController {

    @Resource
    private MdProductMethodService mdProductMethodService;

    /**
     * 创建产品方法
     */
    @PostMapping("")
    public void saveProdMethod(@RequestBody @NonNull ProductMethodMgtVo productMethodMgtVo) {
        Assert.isParameterError(productMethodMgtVo.getProdId() == null, "产品ID不能为空");
        Assert.isParameterError(productMethodMgtVo.getName() == null || productMethodMgtVo.getName().isBlank(), "方法名称不能为空");
        Assert.isParameterError(productMethodMgtVo.getMethodCode() == null || productMethodMgtVo.getMethodCode().isBlank(), "方法编码不能为空");
        mdProductMethodService.saveProdMethod(productMethodMgtVo);
    }

    /**
     * 更新产品方法
     */
    @PutMapping("/{methodId}")
    public void updateProdMethod(@RequestBody @NonNull ProductMethodMgtVo productMethodMgtVo, @PathVariable Long methodId) {
        productMethodMgtVo.setId(methodId);
        Assert.isParameterError(productMethodMgtVo.getProdId() == null, "产品ID不能为空");
        Assert.isParameterError(productMethodMgtVo.getName() == null || productMethodMgtVo.getName().isBlank(), "方法名称不能为空");
        Assert.isParameterError(productMethodMgtVo.getMethodCode() == null || productMethodMgtVo.getMethodCode().isBlank(), "方法编码不能为空");
        mdProductMethodService.updateProdMethod(productMethodMgtVo);
    }

    /**
     * 查询产品方法详情
     */
    @GetMapping("/{methodId}")
    public ProductMethodMgtVo prodMethodDetail(@PathVariable Long methodId) {
        return mdProductMethodService.prodMethodDetail(methodId);
    }

    /**
     * 删除产品方法
     */
    @DeleteMapping("/{methodId}")
    public void deleteProdMethod(@PathVariable Long methodId) {
        mdProductMethodService.deleteProdMethod(methodId);
    }

    /**
     * 查询产品方法列表
     */
    @GetMapping("/list")
    public List<ProductMethodMgtVo> listProdMethod(@RequestParam Long prodId) {
        return mdProductMethodService.listProdMethod(prodId);
    }

    /**
     * 批量保存或更新产品方法（覆盖模式）
     * 前端传递的方法列表将完全覆盖产品下的现有方法
     */
    @PostMapping("/batch")
    public void saveOrUpdateProdMethods(@RequestBody @NonNull ProductMethodBatchVo batchVo) {
        Assert.isParameterError(batchVo.getProdId() == null, "产品ID不能为空");
        Assert.isParameterError(batchVo.getMethods() == null, "方法列表不能为null");
        mdProductMethodService.saveOrUpdateProdMethods(batchVo);
    }
}
