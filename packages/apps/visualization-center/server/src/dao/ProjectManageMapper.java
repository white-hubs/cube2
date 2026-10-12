package com.guhecloud.cube.base.visualization.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.visualization.server.entity.ProjectManage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目管理数据库访问层
 *
 * @author : lijie
 * @date : 2023-8-21
 */
@Mapper
public interface ProjectManageMapper extends BaseMapper<ProjectManage> {
}
