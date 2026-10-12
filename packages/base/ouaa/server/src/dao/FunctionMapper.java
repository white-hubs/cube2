package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Function;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 功能数据操作接口
 *
 * @author Woody
 * @date 2021/6/30
 */
@Mapper
public interface FunctionMapper extends BaseMapper<Function> {

    @Select("select mf.`menu_id` as `menuId`,f.`id` as `functionId`,f.`name` as `functionName`,f.`code` as `functionCode`,f.`enabled`\n" +
            "from `menu_function` mf\n" +
            "left join `ouaa_function` f on mf.`function_id` = f.`id`"
    )
    List<MenuFunctionVO> getAll();
}
