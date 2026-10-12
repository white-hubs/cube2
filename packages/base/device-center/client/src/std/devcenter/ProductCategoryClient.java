package com.guhecloud.cube.base.device.center.std.devcenter;

import com.guhecloud.cube.base.device.center.metadata.dto.ProductCategoryInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.ProductCategoryPageParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME;

/**
 * @author pengjian
 * @date 2024/1/18 19:29
 * @description
 */
@FeignClient(CLIENT_NAME)
public interface ProductCategoryClient {

    @GetMapping("/iot/product/category/list")
    List<ProductCategoryInfoDTO> list(@RequestParam("codes") Set<String> codes);

    @GetMapping("/iot/product/category/page")
    Pagination<ProductCategoryInfoDTO> categoryPage(@SpringQueryMap ProductCategoryPageParam pageParam);
}
