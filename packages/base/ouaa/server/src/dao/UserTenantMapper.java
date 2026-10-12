package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.KeyValueDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户租户关联数据操作接口
 *
 * @author Woody
 * @date 2021/7/13
 */
@Mapper
public interface UserTenantMapper extends BaseMapper<UserTenant> {
    @Select("SELECT `work_status` as `name`,count(1) as `value` FROM `user_tenant` where `tenant`=#{tenant} GROUP BY `work_status`")
    List<KeyValueDTO> getStateTotal(@Param("tenant") Integer tenant);
}
