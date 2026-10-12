package com.guhecloud.platform.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author pengjian
 * @date 2022/6/23 11:18
 * @description
 */
@Mapper
public interface BatchSoftDeleteMapper {

    @Update("${sql }")
    void softDelete(String sql);
}
