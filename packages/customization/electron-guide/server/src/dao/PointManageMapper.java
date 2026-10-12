package com.guhecloud.cube.srv.electron.guide.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.electron.guide.entity.PointManage;
import com.guhecloud.cube.srv.electron.guide.param.PointManagePageParam;
import com.guhecloud.cube.srv.electron.guide.vo.PointManagePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:39
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface PointManageMapper extends BaseMapper<PointManage> {
    Page<PointManagePageVO> selectPointManagePage(@Param("page")Page<PointManagePageVO> page,@Param("param") PointManagePageParam param);
}
