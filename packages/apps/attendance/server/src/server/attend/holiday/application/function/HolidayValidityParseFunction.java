package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.function;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.vo.HolidayQuotaValidityVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayQuotaValidityEnum;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-29  11:12
 * @Description:
 * @Version: 1.0
 */
@Component
public class HolidayValidityParseFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "HOLIDAY_VALIDITY";
    }

    @Override
    public String apply(Object value) {
        String name = "";
        if (ObjectUtils.isNotEmpty(value)) {
            HolidayQuotaValidityVO quotaValidityVO = JsonUtils.deserialize(JsonUtils.serialize(value), HolidayQuotaValidityVO.class);
            if (quotaValidityVO.getLongTerm()) {
                name = "长期有效";
            } else if (ObjectUtils.isNotEmpty(quotaValidityVO.getValidityNumber())) {
                name = quotaValidityVO.getValidityNumber() +
                       AttHolidayQuotaValidityEnum.valueOf(quotaValidityVO.getValidityUnit()).getValue() + "内有效";
            } else {
                name = quotaValidityVO.getNatureYear() + "个自然年有效";
            }
        }
        return name;
    }
}
