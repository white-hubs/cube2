package com.guhecloud.cube.biz.ouaa.server.permission.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.permission.common.enumeration.SubjectTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.SubjectPermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.SubjectPermissionVO;
import com.guhecloud.platform.security.permission.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SubjectPermissionMapper extends BaseMapper<SubjectPermissionPO> {

    Set<Permission> selectPermissionsBySubjectIds(@Param("subjectIds") Set<String> subjectIds,
                                                  @Param("subjectType") SubjectTypeEnum subjectType
                                                 );

    List<SubjectPermissionVO> selectAllPermissionsBySubjectIds(@Param("subjectIds") Set<String> subjectIds,
                                                               @Param("subjectType") SubjectTypeEnum subjectType
                                                              );
}
