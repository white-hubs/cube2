package com.guhecloud.cube.base.iot.product.model.v3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProduct;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 产品Mapper v3版本
 * 操作iot_md_product_v3表
 */
@Mapper
public interface V3MdProductMapper extends BaseMapper<MdProduct> {

    /**
     * 根据产品编号查询产品
     *
     * @param prodNumber 产品编号
     * @return 产品实体
     */
    MdProduct selectByProdNumber(@Param("prodNumber") String prodNumber);

    /**
     * 分页查询产品
     *
     * @param page 分页对象
     * @param param 查询参数
     * @return 产品VO列表
     */
    IPage<ProductVO> selectProductPage(IPage<ProductVO> page, @Param("param") ProductPageParam param);

    /**
     * 查询产品详情(包含属性、事件、方法)
     *
     * @param prodId 产品ID
     * @return 产品VO(包含完整信息)
     */
    ProductVO selectProductDetail(@Param("prodId") Long prodId);
}
