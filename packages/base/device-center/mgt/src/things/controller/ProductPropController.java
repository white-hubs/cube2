package com.guhecloud.cube.iot.mgt.things.controller;

import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.things.server.MdProductPropService;
import com.guhecloud.cube.iot.mgt.things.vo.ProductPropVo;
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

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/things/product-prop")
public class ProductPropController {

    @Resource
    private MdProductPropService mdProductPropService;

    @PostMapping("")
    public void saveProdProp(@RequestBody @NonNull ProductPropVo productPropVo) {
        Assert.notNull(productPropVo, "产品不能为空");
        Assert.notNull(productPropVo.getProdId(), "产品ID不能为空");
        Assert.notNull(productPropVo.getName(), "属性名称不能为空");
        Assert.notNull(productPropVo.getPropCode(), "属性Code不能为空");
        //将unit_id已废弃，改为存到unit_code中
        productPropVo.setUnitCode(productPropVo.getUnitId());
        mdProductPropService.saveProdProp(productPropVo, true);
    }

    @PutMapping("/{propId}")
    public void updateProdProp(@RequestBody @NonNull ProductPropVo productPropVo, @PathVariable Long propId) {
        Assert.notNull(productPropVo, "产品不能为空");
        Assert.notNull(productPropVo.getProdId(), "产品ID不能为空");
        Assert.notNull(productPropVo.getName(), "属性名称不能为空");
        Assert.notNull(productPropVo.getPropCode(), "属性Code不能为空");

        productPropVo.setId(propId);
        //将unit_id已废弃，改为存到unit_code中
        productPropVo.setUnitCode(productPropVo.getUnitId());
        mdProductPropService.updateProdProp(productPropVo);
    }

    @GetMapping("/{propId}")
    public ProductPropVo prodPropDetail(@PathVariable Long propId) {
        return mdProductPropService.prodPropDetail(propId);
    }

    @DeleteMapping("/{propId}")
    public void deleteProdProp(@PathVariable Long propId) {
        mdProductPropService.deleteProdProp(propId);
    }

    @GetMapping("/list")
    public List<ProductPropVo> listProdProp(@RequestParam Long prodId) {
        return mdProductPropService.listProdProp(prodId);
    }
}
