package com.guhecloud.cube.base.device.center.proxy.iot;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.ProductCategoryInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.ProductCategoryPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.ProductCategoryQry;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceTagVO;
import com.guhecloud.cube.base.device.center.open.entity.ProductCategoryInfoEntity;
import com.guhecloud.cube.base.device.center.open.mapper.ProductInfoMapper;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductCategoryDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代理一些IOT对外暴露的接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductIotStdController {

    private final IotProductClient iotProductClient;
    private final ProductInfoMapper productInfoMapper;

    /**
     * 拉取产品品类树
     *
     * @param productCategoryQry 拉取参数
     *
     * @return 品类树
     */
    @GetMapping("/iot/product/category-tree")
    public List<ProductCategoryDTO> getProductCategoryTree(@SpringQueryMap ProductCategoryQry productCategoryQry) {
        //后面添加productCategoryQry参数时，这里进行填充
        return iotProductClient.productCategoryTree(new com.guhecloud.cube.iot.north.bridge.client.pj.param.ProductCategoryQry());
    }

    /**
     * 通过产品品类拉取产品信息
     *
     * @param categoryCodeConcat 产品品类编号
     *
     * @return 产品信息
     */
    @GetMapping("/category/{categoryCodeConcat}/product/list")
    public List<DeviceProductInfoDTO> getProductListByProductCategoryId(@PathVariable("categoryCodeConcat") String categoryCodeConcat) {
        return productInfoMapper.selectListByProdCategoryCodeConcatLeftLike(categoryCodeConcat, SessionContextHolder.getRequester()
                .getTenant());
    }

    @GetMapping("/iot/product/list")
    public List<DeviceProductInfoDTO> getProductList(@RequestParam Set<Long> productIds) {
        return productInfoMapper.selectList(productIds);
    }

    @GetMapping("/iot/product/category/list")
    public List<ProductCategoryInfoDTO> list(@RequestParam("codes") Set<String> codes) {
        return productInfoMapper.selectProdCategoryListByCode(codes, SessionContextHolder.getRequester().getTenant()).stream()
                .map(entity -> {
                    ProductCategoryInfoDTO dto = new ProductCategoryInfoDTO();
                    dto.setCode(entity.getCategoryCode());
                    dto.setName(entity.getCategoryName());
                    dto.setPathCode(entity.getCategoryCodeConcat());
                    return dto;
                }).collect(Collectors.toList());
    }

    /**
     * 产品品类分页列表
     *
     * @param pageParam 分页参数
     *
     * @return 产品品类
     */
    @GetMapping("/iot/product/category/page")
    Pagination<ProductCategoryInfoDTO> categoryPage(ProductCategoryPageParam pageParam) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Set<String> set = new HashSet<>();
        if (StringUtils.isNotBlank(pageParam.getCodes())) {
            set.addAll(Arrays.asList(pageParam.getCodes().split(",")));
        }

        Page<ProductCategoryInfoEntity> page = productInfoMapper.categoryPage(PagingUtils.convert(pageParam), set, tenant);
        List<ProductCategoryInfoDTO> productCategoryInfoDTOS = page.getRecords().stream().map(entity -> {
            ProductCategoryInfoDTO dto = new ProductCategoryInfoDTO();
            dto.setCode(entity.getCategoryCode());
            dto.setName(entity.getCategoryName());
            dto.setPathCode(entity.getCategoryCodeConcat());
            return dto;
        }).collect(Collectors.toList());

        Page<ProductCategoryInfoDTO> productCategoryInfoDTOPage = new Page<>();
        productCategoryInfoDTOPage.setRecords(productCategoryInfoDTOS);
        productCategoryInfoDTOPage.setCurrent(page.getCurrent());
        productCategoryInfoDTOPage.setSize(page.getSize());
        productCategoryInfoDTOPage.setTotal(page.getTotal());
        productCategoryInfoDTOPage.setPages(page.getPages());

        return PagingUtils.convert(productCategoryInfoDTOPage);
    }

    /**
     * 产品下挂载的设备标签列表
     *
     * @param productId
     *
     * @return
     */
    @GetMapping("/iot/product/{productId}/device-tags")
    public Set<DeviceTagVO> getProductDeviceTags(@PathVariable("productId") Long productId) {
        return this.productInfoMapper.selectDeviceTagsByProductId(productId);
    }
}
