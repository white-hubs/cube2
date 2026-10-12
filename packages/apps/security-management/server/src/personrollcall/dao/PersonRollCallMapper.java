package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.entity.PersonRollCallEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallListParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.param.PersonRollCallPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo.PersonRollCallPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonRollCallMapper extends BaseMapper<PersonRollCallEntity> {

    Page<PersonRollCallPageVO> getPage(Page page, @Param("param") PersonRollCallPageParam param);

    void insertOrUpdatePersonRollCall(List<PersonRollCallEntity> dataList);

    List<PersonRollCallPageVO> getList(PersonRollCallListParam param);
}
