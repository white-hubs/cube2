package com.guhecloud.cube.iot.mgt.north.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.north.model.MdProductProp;
import com.guhecloud.cube.iot.mgt.north.vo.ProductPropVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NorthProductPropMapper extends BaseMapper<MdProductProp> {
    List<ProductPropVo> queryProductProp(@Param("prodId") Long prodId);
}