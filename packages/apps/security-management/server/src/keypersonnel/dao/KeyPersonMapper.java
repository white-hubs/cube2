package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.entity.KeyPerson;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonAreaCountVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonRiskCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KeyPersonMapper extends BaseMapper<KeyPerson> {
    String selectMaxPersonCodeByPrefix(@Param("prefix") String prefix);

    List<KeyPersonRiskCountVO> countByRiskLevel(@Param("status") String status);

    List<KeyPersonAreaCountVO> countAreaSupervisors(@Param("status") String status, @Param("limit") Integer limit);

    List<KeyPersonAreaCountVO> countAreaHighRisk(@Param("status") String status, @Param("limit") Integer limit);
}
