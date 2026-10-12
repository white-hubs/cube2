package com.guhecloud.cube.base.device.center.std.devcenter;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.ProductCategoryQry;
import com.guhecloud.cube.base.device.center.metadata.std.vo.ProductCategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

/**
 * @author pengjian
 * @date 2024/1/10 14:47
 * @description 产品品类
 */
@FeignClient(CLIENT_NAME)
public interface ProductClient {

    /**
     * 拉取产品品类树
     *
     * @param productCategoryQry 拉取参数
     *
     * @return 品类树
     */
    @GetMapping("/iot/product/category-tree")
    List<ProductCategoryDTO> getProductCategoryTree(@SpringQueryMap ProductCategoryQry productCategoryQry);

    /**
     * 通过产品品类拉取产品信息
     *
     * @param categoryCode 产品品类编号
     *
     * @return 产品信息
     */
    @GetMapping("/category/{categoryCode}/product/list")
    List<DeviceProductInfoDTO> getProductListByProductCategoryId(@PathVariable("categoryCode") String categoryCode);

    /**
     * 批量获取产品信息
     *
     * @param productIds 产品主键集合
     *
     * @return
     */
    @GetMapping("/iot/product/list")
    List<DeviceProductInfoDTO> getProductList(@RequestParam(value = "productIds") Set<Long> productIds);

}
