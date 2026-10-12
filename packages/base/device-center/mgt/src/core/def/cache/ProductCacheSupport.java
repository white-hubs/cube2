package com.guhecloud.cube.iot.mgt.core.def.cache;

import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;

import java.util.List;
import java.util.Optional;

/**
 * 产品缓存支持(做二级缓存实现)<br/>
 * <p>
 * 产品摘要(boj): iot:prod:{product-id}:<br/>
 * 产品属性摘要(hash-map): iot:prod:{product-id}:prop:schema<br/>
 * 产品事件摘要(hash-map): iot:prod:{product-id}:event:schema<br/>
 */
public interface ProductCacheSupport {

    /**
     * 查询产品属性摘要缓存(注意: 并没有进行缓存)
     *
     * @param productId 产品id
     *
     * @return 产品属性摘要
     */
    List<ProductPropSchema> queryProductPropSchemaCache(Long productId);

    /**
     * 查询产品属性摘要缓存
     *
     * @param productId 产品id
     * @param propCode  属性编码
     *
     * @return 产品属性摘要
     */
    Optional<ProductPropSchema> queryProductPropSchemaCache(Long productId, String propCode);

}