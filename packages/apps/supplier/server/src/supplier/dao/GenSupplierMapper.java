package com.guhecloud.cube.srv.gen.supplier.server.supplier.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.entity.GenSupplier;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.UpdateDisableParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzy
 * @date 2021/12/3
 */
@Mapper
public interface GenSupplierMapper extends BaseMapper<GenSupplier> {
    void updateDisable(@Param("list") List<UpdateDisableParam> list);

}
