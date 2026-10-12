package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.north.param.ItemQueryParam;
import com.guhecloud.cube.iot.mgt.north.param.ProductCategoryParam;
import com.guhecloud.cube.iot.mgt.north.param.ProductPageParam;
import com.guhecloud.cube.iot.mgt.north.server.impl.NorthProductService;
import com.guhecloud.cube.iot.mgt.north.vo.DeviceStatusRuleEnumVo;
import com.guhecloud.cube.iot.mgt.north.vo.ItemVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductCategoryVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductVo;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/north/product")
public class NorthProductController {

    @Resource
    private NorthProductService northProductService;

    /**
     * 产品类别查询筛选
     *
     * @param itemQueryParam 查询参数
     *
     * @return 产品树
     */
    @GetMapping("/category/tree")
    @Deprecated
    public List<ItemVo> category(@SpringQueryMap ItemQueryParam itemQueryParam) {
        return northProductService.category(itemQueryParam);
    }

    /**
     * 产品分页查询
     *
     * @param param 查询参数
     *
     * @return 产品分页
     */
    @GetMapping("/page")
    public Pagination<ProductVo> page(@SpringQueryMap ProductPageParam param) {
        return northProductService.page(param);
    }

    /**
     * 获取设备状态规则枚举
     *
     * @return DeviceStatusRuleEnumVo
     */
    @GetMapping("/deviceStatusRuleEnum")
    public DeviceStatusRuleEnumVo deviceStatusRuleEnum() {
        return DeviceStatusRuleEnumVo.buildFull();
    }

    /**
     * 获取多个产品详情
     *
     * @param prodIds prodIds
     *
     * @return List<ProductVo>
     */
    @GetMapping("/prod-list-detail")
    public List<ProductVo> prodListDetail(@RequestParam Set<String> prodIds) {
        if (Objects.isNull(prodIds) || prodIds.isEmpty()) {
            return Collections.emptyList();
        }
        return northProductService.prodListDetail(prodIds.stream().map(Long::parseLong).collect(Collectors.toSet()));
    }

    /**
     * 拉取产品品类树
     *
     * @param productCategoryParam 拉取参数
     *
     * @return 品类树
     */
    @GetMapping("/iot/category-tree")
    public List<ProductCategoryVo> getProductCategoryTree(@SpringQueryMap ProductCategoryParam productCategoryParam) {
        return northProductService.getProductCategoryTree(productCategoryParam);
    }
}
