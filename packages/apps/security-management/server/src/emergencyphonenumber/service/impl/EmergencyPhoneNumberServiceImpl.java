package com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.dao.EmergencyPhoneNumberMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.entity.EmergencyPhoneNumber;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.param.EmergencyPhoneNumberParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.service.EmergencyPhoneNumberService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.emergencyphonenumber.vo.EmergencyPhoneNumberVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyPhoneNumberServiceImpl extends ServiceImpl<EmergencyPhoneNumberMapper, EmergencyPhoneNumber> implements EmergencyPhoneNumberService {
    private final EmergencyPhoneNumberMapper emergencyPhoneNumberMapper;

    @Override
    public Pagination<EmergencyPhoneNumberVo> getEmergencyPhoneNumberPage(EmergencyPhoneNumberParam param) {
        var st = (LocalDateTime) null;
        var et = (LocalDateTime) null;
        if (Objects.nonNull(param.getPeriod())) {
            st = param.getPeriod().getBegin();
            et = param.getPeriod().getEnd();
        }
        Page<EmergencyPhoneNumberVo> emergencyPhoneNumberPage = emergencyPhoneNumberMapper.getEmergencyPhoneNumberPage(PagingUtils.convert(param), st, et);
        return PagingUtils.convert(emergencyPhoneNumberPage);
    }

    @Override
    public Map<String, Object> getTelephoneUsageTrends(EmergencyPhoneNumberParam emergencyPhoneNumberParam) {
        Map<String, Object> remap = new HashMap<>();
        Map<String, Object> dateFormat = SecurityManagementCommonUtil.getDateFormat(emergencyPhoneNumberParam.getType(), emergencyPhoneNumberParam
                .getPeriod().getBegin(), emergencyPhoneNumberParam.getPeriod().getEnd());
        var st = (LocalDateTime) dateFormat.get("st");
        var et = (LocalDateTime) dateFormat.get("et");
        String timeFlag = (String) dateFormat.get("timeFlag");
        List<String> xtitle = (List<String>) dateFormat.get("xtitle");
        List<EmergencyPhoneNumberVo> telephoneUsageTrends = emergencyPhoneNumberMapper.getTelephoneUsageTrends(timeFlag, st, et);
        remap.put("xtitle", xtitle);
        remap.put("telephoneUsageTrends", telephoneUsageTrends);
        return remap;
    }
}
