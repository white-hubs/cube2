package com.guhecloud.cube.srv.tj.electronic.guide.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.tj.electronic.guide.entity.TjPointManage;
import com.guhecloud.cube.srv.tj.electronic.guide.param.PointManagePageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.PointManagePageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:39
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface TjPointManageMapper extends BaseMapper<TjPointManage> {
    Page<PointManagePageVO> selectPointManagePage(@Param("page") Page<PointManagePageVO> page, @Param("param") PointManagePageParam param);
}
