package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.controller;

import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.iot.north.bridge.client.IotProductClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.ProductPropDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ProductPageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.ProductPropertyVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 瑞浩产品相关接口
 */
@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class MedicalProductController {
    private final static Integer MaxSize = 9999;
    private final IotProductClient iotProductClient;
    private final TenantClient tenantClient;

    /**
     * 获取所有租户的产品
     */
    @GetMapping("/product/all-tenant/products")
    public List<ProductPropertyVO> getAllTenantProductsAsync() {
        List<TenantDTO> tenantDTOList = tenantClient.listTenants(null);
        Map<Integer, TenantDTO> tenantDTOMap = MapUtils.listToMap(tenantDTOList, TenantDTO::getCode, t -> t);
        Requester requester = SessionContextHolder.getRequester();

        // 异步获取所有租户的产品数据
        List<CompletableFuture<List<ProductPropertyVO>>> futures = tenantDTOMap.keySet().stream()
                .map(tenantDTO -> CompletableFuture.supplyAsync(() -> fetchProductsForTenant(tenantDTO, requester)))
                .collect(Collectors.toList());

        // 合并所有异步任务的结果
        List<ProductPropertyVO> productPropertyVOList = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        //根据属性code去重
        Map<String, List<ProductPropertyVO>> productProperttyMap = MapUtils.listGroupToMap(productPropertyVOList, ProductPropertyVO::getPropertyCode);
        return productProperttyMap.values().stream().map(list -> list.get(0)).collect(Collectors.toList());
    }

    private static List<ProductPropertyVO> convertToProductProperties(Integer tenantId, ProductDTO productDTO) {
        List<ProductPropDTO> productPropVoList = productDTO.getProductPropVoList();
        if (ListUtils.isEmpty(productPropVoList)) {
            return Collections.emptyList();
        }
        List<ProductPropertyVO> productPropertyVOList = productPropVoList
                .stream()
                .map(productPropDTO -> {
                    ProductPropertyVO productPropertyVO = new ProductPropertyVO();
                    productPropertyVO.setProductId(Long.valueOf(productDTO.getProdId()));
                    productPropertyVO.setProductName(productDTO.getProdName());
                    productPropertyVO.setTenantId(tenantId);
                    productPropertyVO.setPropertyCode(productPropDTO.getPropCode());
                    productPropertyVO.setPropertyName(productPropDTO.getPropName());
                    productPropertyVO.setPropertyUnit(productPropDTO.getUnitName());
                    return productPropertyVO;
                }).collect(Collectors.toList());

        log.info("当前查询的租户产品信息查询结束，租户id：{}", tenantId);
        return productPropertyVOList;
    }

    // 每个租户的数据拉取逻辑封装成一个独立的方法
    private List<ProductPropertyVO> fetchProductsForTenant(Integer tenantId, Requester requester) {
        log.info("租户产品信息查询，当前正在查询租户的租户id：{}", tenantId);
        ProductPageParam productPageParam = new ProductPageParam();
        productPageParam.setWithPropertySpec(true);
        productPageParam.setPageSize(MaxSize);
        productPageParam.setPageNumber(1);

        SessionContextHolder.setRequester(requester);
        SessionContextHolder.requestTenant(tenantId); // 设置上下文租户
        Pagination<ProductDTO> page = iotProductClient.page(productPageParam);
        List<ProductDTO> records = page.getRecords();
        if (ListUtils.isEmpty(records)) {
            log.info("租户：{}，查询的产品信息为空", tenantId);
            return Collections.emptyList();
        }

        return records
                .stream()
                .map(productDTO -> convertToProductProperties(tenantId, productDTO))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}