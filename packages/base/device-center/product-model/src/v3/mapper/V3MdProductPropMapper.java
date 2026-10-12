package com.guhecloud.cube.base.iot.product.model.v3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductProp;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductPropVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品属性Mapper v3版本
 * 操作iot_md_product_prop_v3表
 */
@Mapper
public interface V3MdProductPropMapper extends BaseMapper<MdProductProp> {

    /**
     * 根据产品ID查询属性列表
     *
     * @param prodId 产品ID
     * @return 属性VO列表
     */
    List<ProductPropVO> selectByProdId(@Param("prodId") Long prodId);

    /**
     * 根据产品ID和属性Code查询属性
     *
     * @param prodId 产品ID
     * @param propCode 属性Code
     * @return 属性实体
     */
    MdProductProp selectByProdIdAndCode(@Param("prodId") Long prodId, @Param("propCode") String propCode);
}
