package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.BookIntervalRecordMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceStatisticsParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceStatisticsService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookIntervalStatisticsVo;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceStatisticsServiceImpl implements ConferenceStatisticsService {

    private final BookIntervalRecordMapper bookIntervalRecordMapper;

    @Override
    public Map<String, List<BookIntervalStatisticsVo>> meetingAvailabilityNextHours() {
        LocalDateTime date = DateUtils.now();
        List<String> period = new ArrayList<>();
        List<String> appointmentDates = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            LocalDateTime offset = DateUtils.offset(date, ChronoUnit.HOURS, i);
            int hour = offset.getHour();
            if (hour < 9) {
                period.add("0" + hour + ":00-" + "0" + hour + ":30");
                int endHour = hour + 1;
                period.add("0" + hour + ":30-" + "0" + endHour + ":00");
            } else {
                period.add(hour + ":00-" + hour + ":30");
                int endHour = hour + 1;
                if (endHour == 24) {
                    period.add(hour + ":30-00:00");
                } else {
                    period.add(hour + ":30-" + endHour + ":00");
                }
            }
            String s = DateUtils.formatDate(offset);
            appointmentDates.add(s);
        }
        List<String> collect = appointmentDates.stream().distinct().collect(Collectors.toList());
        //可预约时段
        List<BookIntervalStatisticsVo> bookIntervalStatisticsPeriod = bookIntervalRecordMapper.getBookIntervalStatisticsPeriod(
                period);
        //已预约时段
        List<BookIntervalStatisticsVo> reservedStatistics = bookIntervalRecordMapper.getReservedStatistics(collect, period);
        Map<String, BookIntervalStatisticsVo> reservedMap = reservedStatistics.stream().collect(
                Collectors.toMap(s -> s.getName() + s.getPeriodTime(), s -> s));
        for (BookIntervalStatisticsVo bookIntervalStatisticsVo : bookIntervalStatisticsPeriod) {
            String key = bookIntervalStatisticsVo.getName() + bookIntervalStatisticsVo.getPeriodTime();
            BookIntervalStatisticsVo bookIntervalStatisticsVo1 = reservedMap.get(key);
            if (Objects.nonNull(bookIntervalStatisticsVo1)) {
                bookIntervalStatisticsVo.setBookRoomEnum(bookIntervalStatisticsVo1.getBookRoomEnum());
            }
        }
        return bookIntervalStatisticsPeriod.stream().collect(
                Collectors.groupingBy(BookIntervalStatisticsVo::getName));
    }

    @Override
    public Map<Integer, Object> analysisScheduledTimePeriods() {
        HashMap<Integer, Object> remap = new HashMap<>();
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        int g = 0;
        int h = 0;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        String[] timePeriod1 = { "00:00-00:30", "00:30-01:00", "01:00-01:30", "01:30-02:00" };
        List<String> time1 = Arrays.asList(timePeriod1);
        String[] timePeriod2 = { "02:00-02:30", "02:30-03:00", "03:00-03:30", "03:30-04:00" };
        List<String> time2 = Arrays.asList(timePeriod2);
        String[] timePeriod3 = { "04:00-04:30", "04:30-05:00", "05:00-05:30", "05:30-06:00" };
        List<String> time3 = Arrays.asList(timePeriod3);
        String[] timePeriod4 = { "06:00-06:30", "06:30-07:00", "07:00-07:30", "07:30-08:00" };
        List<String> time4 = Arrays.asList(timePeriod4);
        String[] timePeriod5 = { "08:00-08:30", "08:30-09:00", "09:00-09:30", "09:30-10:00" };
        List<String> time5 = Arrays.asList(timePeriod5);
        String[] timePeriod6 = { "10:00-10:30", "10:30-11:00", "11:00-11:30", "11:30-12:00" };
        List<String> time6 = Arrays.asList(timePeriod6);
        String[] timePeriod7 = { "12:00-12:30", "12:30-13:00", "13:00-13:30", "13:30-14:00" };
        List<String> time7 = Arrays.asList(timePeriod7);
        String[] timePeriod8 = { "14:00-14:30", "14:30-15:00", "15:00-15:30", "15:30-16:00" };
        List<String> time8 = Arrays.asList(timePeriod8);
        String[] timePeriod9 = { "16:00-16:30", "16:30-17:00", "17:00-17:30", "17:30-18:00" };
        List<String> time9 = Arrays.asList(timePeriod9);
        String[] timePeriod10 = { "18:00-18:30", "18:30-19:00", "19:00-19:30", "19:30-20:00" };
        List<String> time10 = Arrays.asList(timePeriod10);
        String[] timePeriod11 = { "20:00-20:30", "20:30-21:00", "21:00-21:30", "21:30-22:00" };
        List<String> time11 = Arrays.asList(timePeriod11);
        String[] timePeriod12 = { "22:00-22:30", "22:30-23:00", "23:00-23:30", "23:30-00:00" };
        List<String> time12 = Arrays.asList(timePeriod12);
        List<BookIntervalStatisticsVo> periodCount = bookIntervalRecordMapper.getPeriodCount();
        if (CollectionUtils.isNotEmpty(periodCount)) {
            for (BookIntervalStatisticsVo bookIntervalStatisticsVo : periodCount) {
                if (time1.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    a += bookIntervalStatisticsVo.getNum();
                } else if (time2.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    b += bookIntervalStatisticsVo.getNum();
                } else if (time3.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    c += bookIntervalStatisticsVo.getNum();
                } else if (time4.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    d += bookIntervalStatisticsVo.getNum();
                } else if (time5.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    e += bookIntervalStatisticsVo.getNum();
                } else if (time6.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    f += bookIntervalStatisticsVo.getNum();
                } else if (time7.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    g += bookIntervalStatisticsVo.getNum();
                } else if (time8.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    h += bookIntervalStatisticsVo.getNum();
                } else if (time9.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    i += bookIntervalStatisticsVo.getNum();
                } else if (time10.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    j += bookIntervalStatisticsVo.getNum();
                } else if (time11.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    k += bookIntervalStatisticsVo.getNum();
                } else if (time12.contains(bookIntervalStatisticsVo.getPeriodTime())) {
                    l += bookIntervalStatisticsVo.getNum();
                }
            }

        }
        remap.put(2, a);
        remap.put(4, b);
        remap.put(6, c);
        remap.put(8, d);
        remap.put(10, e);
        remap.put(12, f);
        remap.put(14, g);
        remap.put(16, h);
        remap.put(18, i);
        remap.put(20, j);
        remap.put(22, k);
        remap.put(24, l);

        return remap;
    }

    @Override
    public List<BookIntervalStatisticsVo> analysisConferenceRoomUtilizationRate(ConferenceStatisticsParam param) {
        //所有会议室
        List<BookIntervalStatisticsVo> allInterval = bookIntervalRecordMapper.getAllInterval();
        //预约会议室
        List<BookIntervalStatisticsVo> allIntervalUsage = bookIntervalRecordMapper.getAllIntervalUsage(
                param.getPeriod().getBegin(), param.getPeriod().getEnd());
        Map<Long, BookIntervalStatisticsVo> collect = allIntervalUsage.stream().collect(
                Collectors.toMap(BookIntervalStatisticsVo::getConferenceId, s -> s));
        for (BookIntervalStatisticsVo bookIntervalStatisticsVo : allInterval) {
            BookIntervalStatisticsVo bookIntervalStatisticsVo1 = collect.get(bookIntervalStatisticsVo.getConferenceId());
            if (Objects.nonNull(bookIntervalStatisticsVo1)) {
                int unUseNum = bookIntervalStatisticsVo.getNum() - bookIntervalStatisticsVo1.getNum();
                bookIntervalStatisticsVo.setUsageRate(new BigDecimal(bookIntervalStatisticsVo1.getNum())
                                                              .divide(new BigDecimal(bookIntervalStatisticsVo.getNum()), 2,
                                                                      RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
                bookIntervalStatisticsVo.setUnusedRate(new BigDecimal(unUseNum)
                                                               .divide(new BigDecimal(bookIntervalStatisticsVo.getNum()), 2,
                                                                       RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            }
        }
        return allInterval;
    }
}
