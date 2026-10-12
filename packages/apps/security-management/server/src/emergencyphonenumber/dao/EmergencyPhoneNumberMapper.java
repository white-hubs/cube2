package com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.entity.EmergencyPhoneNumber;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.vo.EmergencyPhoneNumberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EmergencyPhoneNumberMapper extends BaseMapper<EmergencyPhoneNumber> {
    List<EmergencyPhoneNumberVo> getTelephoneUsageTrends(@Param("dateFormat") String dateFormat, @Param("st") LocalDateTime st,
                                                         @Param("et") LocalDateTime et
                                                        );

    Page<EmergencyPhoneNumberVo> getEmergencyPhoneNumberPage(Page page, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et);
}
