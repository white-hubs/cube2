package com.guhecloud.cube.base.iot.product.model.v3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductMethod;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductMethodVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品方法Mapper v3版本(新增功能)
 * 操作iot_md_product_method_v3表
 */
@Mapper
public interface V3MdProductMethodMapper extends BaseMapper<MdProductMethod> {

    /**
     * 根据产品ID查询方法列表
     *
     * @param prodId 产品ID
     * @return 方法VO列表
     */
    List<ProductMethodVO> selectByProdId(@Param("prodId") Long prodId);

    /**
     * 根据产品ID和方法Code查询方法
     *
     * @param prodId 产品ID
     * @param methodCode 方法Code
     * @return 方法实体
     */
    MdProductMethod selectByProdIdAndCode(@Param("prodId") Long prodId, @Param("methodCode") String methodCode);
}
