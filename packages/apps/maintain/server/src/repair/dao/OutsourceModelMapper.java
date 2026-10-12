package com.guhecloud.cube.srv.gen.maintain.server.repair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceModelPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/5/18 15:10
 * @description
 */
@Mapper
public interface OutsourceModelMapper extends BaseMapper<OutsourceModelPO> {
    List<OutsourceModelPO> listByCompany(@Param("companyId") Long companyId);
}
