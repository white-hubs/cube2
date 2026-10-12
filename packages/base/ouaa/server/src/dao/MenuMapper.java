package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Menu;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单数据操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    ApplicationVO getRoleMenus(@Param("userId") String userId, @Param("path") String path, @Param("tenant") Integer tenant);
}
