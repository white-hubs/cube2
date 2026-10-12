package com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.param.EmergencyPhoneNumberParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.service.EmergencyPhoneNumberService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.vo.EmergencyPhoneNumberVo;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyPhoneNumberController {

    private final EmergencyPhoneNumberService emergencyPhoneNumberService;

    /**
     * @Description 紧急电话列表
     * @Author lrj
     */
    @GetMapping("/emergency-phone-number-page")
    public Pagination<EmergencyPhoneNumberVo> getEmergencyPhoneNumberPage(EmergencyPhoneNumberParam param) {
        return emergencyPhoneNumberService.getEmergencyPhoneNumberPage(param);
    }

    /**
     * @Description 电话使用趋势
     * @Author lrj
     */
    @GetMapping("/telephone-usage-trends")
    public Map<String, Object> getTelephoneUsageTrends(EmergencyPhoneNumberParam emergencyPhoneNumberParam) {
        return emergencyPhoneNumberService.getTelephoneUsageTrends(emergencyPhoneNumberParam);
    }

}
