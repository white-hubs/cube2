package com.guhecloud.cube.iot.mgt.north.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.north.model.MdProductEvent;
import com.guhecloud.cube.iot.mgt.north.vo.ProductEventVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NorthProductEventMapper extends BaseMapper<MdProductEvent> {
    List<ProductEventVo> queryProductEvent(@Param("prodId") Long prodId);

    MdProductEvent queryProductEventDesignatedCode(@Param("prodId") Long prodId, @Param("eventCode") String eventCode);
}