package com.guhecloud.cube.base.visualization.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.visualization.server.entity.InstanceManage;
import com.guhecloud.cube.base.visualization.server.param.InstanceManagePageParam;
import com.guhecloud.cube.base.visualization.server.vo.InstanceManageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 发布实例数据库访问层
 *
 * @author : lijie
 * @date : 2023-8-21
 */
@Mapper
public interface InstanceManageMapper extends BaseMapper<InstanceManage> {
    Page<InstanceManageVO> queryInstancePage(@Param("page") Page page, @Param("param") InstanceManagePageParam param);
}
