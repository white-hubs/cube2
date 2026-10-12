package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.function;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.vo.HolidayQuotaConfigDetailsVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.vo.HolidayQuotaConfigVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.enums.AttHolidayQuotaTypeEnum;
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
public class HolidayQuotaCnfigParseFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "HOLIDAY_QUOTA_CONFIG";
    }

    @Override
    public String apply(Object value) {
        StringBuilder name = new StringBuilder();
        AttHolidayQuotaTypeEnum holidayEnum = null;
        HolidayQuotaConfigVO quotaConfigVO = null;
        if (ObjectUtils.isNotEmpty(value)) {
            quotaConfigVO = JsonUtils.deserialize(JsonUtils.serialize(value), HolidayQuotaConfigVO.class);
            holidayEnum = AttHolidayQuotaTypeEnum.valueOf(String.valueOf(quotaConfigVO.getQuotaType()));
        }
        if (ObjectUtils.isNotEmpty(holidayEnum)) {
            if (holidayEnum.name().equals(AttHolidayQuotaTypeEnum.FIXED.name())) {
                name.append("固定额度");
                name.append(quotaConfigVO.getFixedNumber());
                name.append(AttHolidayQuotaValidityEnum.valueOf(quotaConfigVO.getFixedUnit()).getValue());
            } else {
                if (holidayEnum.name().equals(AttHolidayQuotaTypeEnum.SOCIETY_AGE.name())) {
                    name.append("按社会工龄");
                } else {
                    name.append("按公司司龄");
                }
                for (HolidayQuotaConfigDetailsVO ex : quotaConfigVO.getQuotaLevelList()) {
                    name.append(ex.getAgeYear());
                    name.append("年,享有");
                    name.append(ex.getHolidayNumber());
                    name.append(AttHolidayQuotaValidityEnum.valueOf(ex.getUnit()).getValue());
                    name.append(",");
                }
            }
        }
        return name.toString();
    }
}
