package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.MenuFunction;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单功能关联数据操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
@Mapper
public interface MenuFunctionMapper extends BaseMapper<MenuFunction> {

    @Select("select mf.`menu_id`,mf.`function_id`,f.`name` as `functionName`,f.`code` as `functionCode`,f.`enabled` from `menu_function` mf \n" +
            "inner join `ouaa_function` f on mf.`function_id` = f.`id`\n" +
            "where mf.`menu_id` = #{menuId}"
    )
    List<MenuFunctionVO> getFunctionUnderMenu(@Param("menuId") Long menuId);
}
