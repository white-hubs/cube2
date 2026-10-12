package com.guhecloud.cube.iot.mgt.north.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;
import com.guhecloud.cube.iot.mgt.north.model.MdProduct;
import com.guhecloud.cube.iot.mgt.north.param.ItemQueryParam;
import com.guhecloud.cube.iot.mgt.north.param.ProductPageParam;
import com.guhecloud.cube.iot.mgt.north.vo.ItemVo;
import com.guhecloud.cube.iot.mgt.north.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface NorthProductMapper extends BaseMapper<MdProduct> {

    Page<ProductVo> queryPage(Page<Object> convert, @Param("param") ProductPageParam param, @Param("productId") Long productId);

    List<ItemVo> listProductCategory(@Param("itemQueryParam") ItemQueryParam itemQueryParam, @Param("dictTypeId") long id);

    List<ProductVo> queryDetailByProdIds(@Param("prodIds") Set<Long> prodIds);

    List<ProductPropSchema> queryProductPropCacheAll(@Param("productId") Long productId);

    ProductPropSchema querySingleProductPropSchema(@Param("productId") Long productId, @Param("propCode") String propCode);
}