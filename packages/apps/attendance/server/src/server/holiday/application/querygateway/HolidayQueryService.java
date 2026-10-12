package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.convert.HolidayDetailConvert;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayDetailDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.HolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool.StatutoryHolidayResponse;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool.StatutoryHolidays;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/11/21 10:19
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayQueryService {
    private final HolidayRepository holidayRepository;

    /**
     * 根据类型获取假期
     *
     * @param type 假期类型
     *
     * @return 假期
     */
    public Holiday getByType(Holiday.HolidayType type) {
        return holidayRepository.getByType(type);
    }

    public HolidayDetailDTO getById(Long id) {
        Optional<Holiday> optional = holidayRepository.getById(id);
        // 数据结构转换
        if (optional.isPresent()) {
            Holiday holiday = optional.get();
            return HolidayDetailConvert.convert(holiday);
        }
        return null;
    }

    /**
     * 某一天是否是法定节假日
     *
     * @param date 日期
     *
     * @return true：是法定节假日 false不是
     */
    public boolean isStatutoryHoliday(LocalDateTime date) {
        List<StatutoryHolidayResponse> statutoryHolidayResponses = StatutoryHolidays.getThisYear();
        String dateStr = DateUtils.format(date, "yyyy-MM-dd");
        List<StatutoryHolidayResponse> existStatutoryHoliday = statutoryHolidayResponses.stream()
                .filter(statutoryHolidayResponse -> statutoryHolidayResponse.getDate().equals(dateStr))
                .toList();
        return CollectionUtils.isNotEmpty(existStatutoryHoliday);
    }

    /*
     * 返回时间段的节假日列表
     */
    public List<StatutoryHolidayResponse> getStatutoryHolidaysByPeriod(Period period) {
        return StatutoryHolidays.getByPeriod(period);
    }
}
