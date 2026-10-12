package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity.PersonRollCallTaskEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallTaskPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTaskPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonRollCallTaskMapper extends BaseMapper<PersonRollCallTaskEntity> {

    Page<PersonRollCallTaskPageVO> getPage(Page page, @Param("param") PersonRollCallTaskPageParam param);

    void insertOrUpdateTask(List<PersonRollCallTaskEntity> dataList);

    List<PersonRollCallTaskVO> getList(@Param("param") PersonRollCallListParam param);
}
