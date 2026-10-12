package com.guhecloud.cube.biz.todo.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.todo.server.entity.TodoPerson;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoPersonMapper extends BaseMapper<TodoPerson> {
}
