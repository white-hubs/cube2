package com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.entity.EmergencyPhoneNumber;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.param.EmergencyPhoneNumberParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.vo.EmergencyPhoneNumberVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;

public interface EmergencyPhoneNumberService extends IService<EmergencyPhoneNumber> {
    Pagination<EmergencyPhoneNumberVo> getEmergencyPhoneNumberPage(EmergencyPhoneNumberParam param);

    Map<String, Object> getTelephoneUsageTrends(EmergencyPhoneNumberParam emergencyPhoneNumberParam);
}
