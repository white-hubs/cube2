package com.guhecloud.cube.srv.electron.guide.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.electron.guide.entity.LayerManage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  13:39
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface LayerManageMapper extends BaseMapper<LayerManage> {
    @Update("update layer_manage set state='NOT_USED' where state='IN_USE'")
    Integer updateLayerManageByState();

}
