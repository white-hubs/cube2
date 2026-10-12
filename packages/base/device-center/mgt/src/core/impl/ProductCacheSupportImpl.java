package com.guhecloud.cube.iot.mgt.core.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;
import com.guhecloud.cube.iot.mgt.core.def.cache.ProductCacheSupport;
import com.guhecloud.cube.iot.mgt.north.mapper.NorthProductMapper;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class ProductCacheSupportImpl implements ProductCacheSupport {
    /**
     * 产品属性摘要信息<br/>
     * 用于产品ID+属性Code
     */
    private final Cache<String, ProductPropSchema> productPropSingleCache = CacheBuilder.newBuilder()
            .initialCapacity(50)
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

    /**
     * 产品属性摘要信息<br/>
     * 用于批量获取属性信息场景
     * 产品ID -> 产品属性列表
     */
    private final Cache<String, List<ProductPropSchema>> productPropsCache = CacheBuilder.newBuilder()
            .initialCapacity(50)
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

    @Resource
    private NorthProductMapper northProductMapper;

    @Override
    public Optional<ProductPropSchema> queryProductPropSchemaCache(Long productId, String propCode) {
        if (Objects.isNull(productId) || StringUtils.isBlank(propCode)) {
            return Optional.empty();
        }

        // 先从缓存中获取
        ProductPropSchema prodProp = productPropSingleCache.getIfPresent(productId + propCode);
        if (prodProp != null) {
            return Optional.of(prodProp);
        }

        // 如果缓存中没有，则从数据库查询
        prodProp = querySingleProductPropSchemaInDb(productId, propCode);
        if (Objects.isNull(prodProp)) {
            return Optional.empty();
        }

        // 查找指定属性编码的属性
        productPropSingleCache.put(productId + propCode, prodProp);
        return Optional.of(prodProp);
    }

    @Override
    public List<ProductPropSchema> queryProductPropSchemaCache(Long productId) {
        if (Objects.isNull(productId)) {
            return Collections.emptyList();
        }

        // 先从缓存中获取
        List<ProductPropSchema> productPropSchemas = productPropsCache.getIfPresent(productId.toString());
        if (productPropSchemas != null) {
            return productPropSchemas;
        }

        // 如果缓存中没有，则从数据库查询
        productPropSchemas = queryProductPropSchemaInDb(productId);
        if (Objects.isNull(productPropSchemas) || productPropSchemas.isEmpty()) {
            return Collections.emptyList();
        }

        // 将查询到的属性列表放入缓存
        productPropsCache.put(productId.toString(), productPropSchemas);

        // 返回查询到的属性列表
        return productPropSchemas;
    }

    /**
     * 查询产品属性摘要缓存
     *
     * @param productId 产品id
     *
     * @return 产品属性摘要
     */
    private List<ProductPropSchema> queryProductPropSchemaInDb(Long productId) {
        List<ProductPropSchema> temp = northProductMapper.queryProductPropCacheAll(productId);
        if (Objects.isNull(temp)) {
            return Collections.emptyList();
        }
        return temp;
    }

    /**
     * 查询单个产品属性摘要
     *
     * @param productId 产品id
     * @param propCode  属性编码
     *
     * @return 产品属性摘要
     */
    private ProductPropSchema querySingleProductPropSchemaInDb(Long productId, String propCode) {
        return northProductMapper.querySingleProductPropSchema(productId, propCode);
    }
}
