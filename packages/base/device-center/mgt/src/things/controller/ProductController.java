package com.guhecloud.cube.iot.mgt.things.controller;

import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import com.guhecloud.cube.iot.mgt.things.param.ProdFetchFullParam;
import com.guhecloud.cube.iot.mgt.things.param.ProductPageParam;
import com.guhecloud.cube.iot.mgt.things.server.MdProductService;
import com.guhecloud.cube.iot.mgt.things.vo.ProductVo;
import com.guhecloud.platform.base.model.Pagination;
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
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;
import static com.guhecloud.cube.iot.mgt.comm.util.GetRequestDecode.decodeUrlIfNeed;

@Slf4j
@RestController("iotMgtProductController")
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/things/product")
public class ProductController {
    @Resource
    private MdProductService mdProductService;

    @GetMapping("/page")
    public Pagination<ProductVo> page(ProductPageParam paging) {
        return mdProductService.listByPage(paging);
    }

    @GetMapping("/list/all")
    public List<ProductVo> listAll() {
        return mdProductService.listAll();
    }

    @PostMapping("")
    public void saveProduct(@RequestBody @NonNull ProductVo param) {
        mdProductService.saveProduct(param);
    }

    @PutMapping("/{prodId}")
    public void updateProduct(@RequestBody @NonNull ProductVo param, @PathVariable String prodId) {
        param.setId(Long.valueOf(prodId));
        mdProductService.updateProduct(param);
    }

    @GetMapping("/{prodId}")
    public ProductVo productDetail(@PathVariable("prodId") Long prodId) {
        return mdProductService.productDetail(prodId);
    }

    @DeleteMapping("/{prodId}")
    public void deleteProduct(@PathVariable("prodId") Long prodId) {
        mdProductService.deleteProduct(prodId);
    }

    /**
     * 根据产品节点类型获取可用的接入模式
     *
     * @return 接入模式列表
     */
    @GetMapping("/{prodNodeType}/acc-model")
    public List<DictItemVo> accModel(@PathVariable Integer prodNodeType) {
        return mdProductService.mdProductService(prodNodeType);
    }

    @GetMapping("/fetch-full/prod")
    public Pagination<ProductVo> fetchFullProd(ProdFetchFullParam fetchFullParam) throws UnsupportedEncodingException {
        if (Objects.nonNull(fetchFullParam)) {
            fetchFullParam.setProdName(decodeUrlIfNeed(fetchFullParam.getProdName()));
        }

        return mdProductService.fetchFullProd(fetchFullParam);
    }
}
