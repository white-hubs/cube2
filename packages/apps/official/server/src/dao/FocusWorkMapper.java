package com.guhecloud.cube.srv.gen.official.management.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author appet
 * @date 2023/02/21
 */
@Mapper
public interface FocusWorkMapper extends BaseMapper<FocusWorkPO> {

    @Select("select DISTINCT first_user_id as id,first_user_name as name from focus_work")
    List<Map> getFirstUsersInFocusWork();

}
