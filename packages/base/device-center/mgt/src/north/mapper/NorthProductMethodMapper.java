package com.guhecloud.cube.iot.mgt.north.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.things.model.MdProductMethod;
import com.guhecloud.cube.iot.mgt.north.vo.ProductMethodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品方法北向Mapper
 */
@Mapper
public interface NorthProductMethodMapper extends BaseMapper<MdProductMethod> {
    /**
     * 查询产品方法列表（仅返回已启用的方法）
     *
     * @param prodId 产品ID
     * @return 产品方法列表
     */
    List<ProductMethodVo> queryProductMethod(@Param("prodId") Long prodId);
}
