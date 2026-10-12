package com.guhecloud.cube.biz.mdata.server.grid.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridRelationMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网格关联成员表 Mapper
 */
@Mapper
public interface GridRelationMemberMapper extends BaseMapper<GridRelationMember> {
}

