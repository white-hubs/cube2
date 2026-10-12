package com.guhecloud.cube.base.iot.product.model.v3.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProductEvent;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品事件Mapper v3版本
 * 操作iot_md_product_event_v3表
 */
@Mapper
public interface V3MdProductEventMapper extends BaseMapper<MdProductEvent> {

    /**
     * 根据产品ID查询事件列表
     *
     * @param prodId 产品ID
     * @return 事件VO列表
     */
    List<ProductEventVO> selectByProdIdNoCategoryName(@Param("prodId") Long prodId);

    /**
     * 根据产品ID和事件Code查询事件
     *
     * @param prodId 产品ID
     * @param eventCode 事件Code
     * @return 事件实体
     */
    MdProductEvent selectByProdIdAndCode(@Param("prodId") Long prodId, @Param("eventCode") String eventCode);
}
