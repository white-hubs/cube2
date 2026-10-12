package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceStatusRuleEnumDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ItemDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductCategoryDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ItemQueryParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ProductCategoryQry;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ProductPageParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

@FeignClient(name = CLIENT_NAME)
public interface IotProductClient {

    /**
     * 获取产品品类树
     *
     * @param itemQueryParam 查询参数
     * @return 产品品类树
     * @deprecated 请使用 {@link #productCategoryTree(ProductCategoryQry)}
     */
    @Deprecated
    @GetMapping("/north/product/category/tree")
    List<ItemDTO> category(@SpringQueryMap ItemQueryParam itemQueryParam);

    /**
     * 拉取产品品类树
     *
     * @param productCategoryQry 拉取参数
     * @return 品类树
     */
    @GetMapping("/north/product/iot/category-tree")
    List<ProductCategoryDTO> productCategoryTree(@SpringQueryMap ProductCategoryQry productCategoryQry);

    /**
     * 获取产品对象
     *
     * @param param param
     * @return page ProductDto
     */
    @GetMapping("/north/product/page")
    Pagination<ProductDTO> page(@SpringQueryMap ProductPageParam param);

    /**
     * 一次性拉取多个产品详情
     *
     * @param prodIds prodIds
     * @return List ProductDto
     */
    @GetMapping("/north/product/prod-list-detail")
    List<ProductDTO> prodListDetail(@RequestParam("prodIds") List<String> prodIds);

    @GetMapping("/north/product/deviceStatusRuleEnum")
    DeviceStatusRuleEnumDTO deviceStatusRuleEnum();

}
