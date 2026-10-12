package com.guhecloud.cube.srv.gen.official.management.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkCategoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author appet
 * @date 2023/02/21
 */
@Mapper
public interface FocusWorkCategoryMapper extends BaseMapper<FocusWorkCategoryPO> {

    @InterceptorIgnore(tenantLine = "true")
    @Select("select * from focus_work_category where id = #{id}")
    FocusWorkCategoryPO getByIdInAll(@Param("id") Long id);
}
