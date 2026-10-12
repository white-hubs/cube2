package com.guhecloud.cube.iot.mgt.things.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;
import com.guhecloud.cube.iot.mgt.things.model.MdProduct;
import com.guhecloud.cube.iot.mgt.things.vo.ProdNameAndNodeTypeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MdProductMapper extends BaseMapper<MdProduct> {

    ProdNameAndNodeTypeVo queryProdNameAndNodeType(@Param("productId") Long productId);

    Long countDeviceByProdId(@Param("prodId") Long prodId);

    List<ProductPropSchema> queryProductPropSchema(@Param("productId") Long productId);

    List<Long> queryProdIdByCategory(@Param("prodCategoryId") Long prodCategoryId);

    boolean isGateway(@Param("productId") Long productId);
}