package com.guhecloud.cube.deliver.project.merchant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.UserQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.UserDeliver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDeliverMapper extends BaseMapper<UserDeliver> {
   Integer insertUserTenant(@Param("id")Long id,@Param("userId")String userId,@Param("tenant")Integer tenant);
}
