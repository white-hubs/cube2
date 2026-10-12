package com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dto.InfoScreenScheduleAssocDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dto.InformationDeviceCountDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.InfoScreenScheduleAssocEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InfoScreenScheduleAssocMapper extends BaseMapper<InfoScreenScheduleAssocEntity> {
    void saveAll(@Param("list") List<InfoScreenScheduleAssocEntity> list);

    void deleteByInformationPlanId(@Param("informationPlanId") Long informationPlanId);

    List<InfoScreenScheduleAssocDTO> getByInformationPlanId(@Param("informationPlanId") Long id);

    List<InformationDeviceCountDTO> countByInfoPlanIds(@Param("infoPlanIds") List<Long> infoPlanIds);

    void deleteInfoScreenScheduleAssoc(@Param("securityDeviceId") Long securityDeviceId);
}
