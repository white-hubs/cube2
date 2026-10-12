package com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttCalendarHolidayPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.repository.AttCalendarHolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto.AttLegalDayCountDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto.AttLegalHolidayMapDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto.WebAlmanacDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto.WebDateDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 法定节假日
 */
@Slf4j
public class AttLegalHolidayUtils {

    private AttLegalHolidayUtils() {
    }

    // 法定班休(只有法定班休)
    private static final Map<String, AttLegalHolidayMapDTO> monthHolidayMap = new HashMap<>();
    // 法定班休(包含全部日期)
    private static final Map<String, AttLegalDayCountDTO> monthLegalCountMap = new HashMap<>();
    // 前端使用的map
    private static final Map<String, Map<String, List<WebDateDTO>>> yearWebHolidayMap = new HashMap<>();

    /**
     * 获取某月的法定班休日(只有法定班休)
     *
     * @param year  年
     * @param month 月
     *
     * @return map型的法定班休日
     */
    public static AttLegalHolidayMapDTO getMonthLegalHolidayMap(Integer year, Integer month) {
        AttLegalHolidayMapDTO result = new AttLegalHolidayMapDTO();
        result.setRestDateMap(new HashMap<>());
        result.setWorkDateMap(new HashMap<>());
        if (year == null || month == null) {
            return result;
        }
        if (monthHolidayMap.get(String.valueOf(year) + month) != null) {
            return monthHolidayMap.get(String.valueOf(year) + month);
        }
        AttCalendarHolidayRepository attCalendarHolidayRepository = ApplicationContextHolder.getBean(AttCalendarHolidayRepository.class);
        List<AttCalendarHolidayPO> poList = attCalendarHolidayRepository.list(Wrappers.<AttCalendarHolidayPO>lambdaQuery()
                                                                                      .select(AttCalendarHolidayPO::getId)
                                                                                      .eq(AttCalendarHolidayPO::getYear, year)
                                                                             );
        // 尝试第三方入库
        if (CollectionUtils.isEmpty(poList)) {
            attCalendarHolidayRepository.getThirdYearHolidayInsert(year);
        }
        poList = attCalendarHolidayRepository.list(Wrappers.<AttCalendarHolidayPO>lambdaQuery()
                                                           .eq(AttCalendarHolidayPO::getYear, year)
                                                           .in(AttCalendarHolidayPO::getHolidayType, Arrays.asList(2, 3))
                                                  );

        if (CollectionUtils.isEmpty(poList)) {
            return result;
        }

        Map<Integer, List<AttCalendarHolidayPO>> monthMap = poList.stream().collect(Collectors.groupingBy(AttCalendarHolidayPO::getMonth));
        LocalDateTime nowTime = LocalDateTime.now();
        monthMap.forEach((m, pl) -> {
            AttLegalHolidayMapDTO yearMonthDTO = new AttLegalHolidayMapDTO();
            Map<String, String> workMonthMap = new HashMap<>();
            Map<String, String> restMonthMap = new HashMap<>();
            Map<String, AttCalendarHolidayPO> dayMap = pl.stream().collect(Collectors.toMap(AttCalendarHolidayPO::getDayFormat, v -> v));

            dayMap.forEach((k, po) -> {
                if (po.getHolidayType() == 2) {
                    restMonthMap.put(po.getDayFormat(), String.valueOf(po.getHolidayType()));
                } else if (Boolean.FALSE.equals(po.getHoliday())) {
                    workMonthMap.put(po.getDayFormat(), String.valueOf(po.getHolidayType()));
                }
            });
            yearMonthDTO.setWorkDateMap(workMonthMap);
            yearMonthDTO.setRestDateMap(restMonthMap);
            if (year <= nowTime.getYear() || (year == nowTime.getYear() + 1 && nowTime.getMonthValue() >= 12)) {
                // // 今年及以前的就缓存，或者当前是12月查询明年(如果是之后年的假期还没有更新没有意义）
                monthHolidayMap.put(String.valueOf(year) + m, yearMonthDTO);
            }
            if (Objects.equals(month, m)) {
                result.setRestDateMap(restMonthMap);
                result.setWorkDateMap(workMonthMap);
            }
        });

        return result;
    }

    /**
     * 获取某月应出勤天数(包含全部日期)
     *
     * @param year  年份
     * @param month 月份
     *
     * @return 上班和休息日期
     */
    public static AttLegalDayCountDTO getMonthLegalWorkDay(Integer year, Integer month) {
        AttLegalDayCountDTO result = new AttLegalDayCountDTO();
        result.setWorkList(new ArrayList<>());
        result.setRestList(new ArrayList<>());
        if (year == null || month == null) {
            return result;
        }
        // 获取缓存
        if (monthLegalCountMap.get(String.valueOf(year) + month) != null) {
            return monthLegalCountMap.get(String.valueOf(year) + month);
        }
        // 获取数据库
        AttCalendarHolidayRepository attCalendarHolidayRepository = ApplicationContextHolder.getBean(AttCalendarHolidayRepository.class);
        List<AttCalendarHolidayPO> monthList = attCalendarHolidayRepository.list(Wrappers.<AttCalendarHolidayPO>lambdaQuery()
                                                                                         .eq(AttCalendarHolidayPO::getYear, year)
                                                                                         .eq(AttCalendarHolidayPO::getMonth, month)
                                                                                );
        // 获取第三方数据，并入库
        if (CollectionUtils.isEmpty(monthList)) {
            List<AttCalendarHolidayPO> yearList = attCalendarHolidayRepository.getThirdYearHolidayInsert(year);
            Map<Integer, List<AttCalendarHolidayPO>> monthMap = yearList.stream()
                    .collect(Collectors.groupingBy(AttCalendarHolidayPO::getMonth));
            monthList = monthMap.get(month);
        }

        if (CollectionUtils.isEmpty(monthList)) {
            return result;
        }
        Map<String, AttCalendarHolidayPO> dayMap = monthList.stream().collect(Collectors.toMap(AttCalendarHolidayPO::getDayFormat, v -> v));
        LocalDateTime nowTime = LocalDateTime.now();

        LocalDateTime monthStartTime = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime monthEndTime = DateUtils.endOfMonth(monthStartTime);
        LocalDateTime printTime = DateUtils.newLocalDateTime(monthStartTime);

        List<LocalDateTime> workMonthList = new ArrayList<>();
        List<LocalDateTime> restMonthList = new ArrayList<>();
        int number = 1;
        do {
            AttCalendarHolidayPO holidayDay = dayMap.get(DateUtils.formatDate(printTime));
            if (holidayDay == null) {
                workMonthList.add(printTime);
            } else {
                if (holidayDay.getHolidayType() == AttConstants.oneInt || holidayDay.getHolidayType() == 2) {
                    restMonthList.add(printTime);
                } else if (holidayDay.getHolidayType() == 3 || holidayDay.getHolidayType() == AttConstants.zeroInt) {
                    workMonthList.add(printTime);
                }
            }
            printTime = DateUtils.offsetDay(printTime, AttConstants.oneInt);
            number++;
        } while (printTime.isBefore(monthEndTime) && number < 100);
        result.setWorkList(workMonthList);
        result.setRestList(restMonthList);

        if (year <= nowTime.getYear() || (year == nowTime.getYear() + 1 && nowTime.getMonthValue() >= 12)) {
            // 今年及以前的就缓存，或者当前是12月查询明年(如果是之后年的假期还没有更新没有意义）
            monthLegalCountMap.put(String.valueOf(year) + month, result);
        }

        return result;
    }

    public static AttLegalDayCountDTO getPeriodLegalWorkDay(Period period) {
        AttLegalDayCountDTO result = new AttLegalDayCountDTO();
        result.setWorkList(new ArrayList<>());
        result.setRestList(new ArrayList<>());
        if (period == null || period.getBegin() == null || period.getEnd() == null) {

            return result;
        }

        List<LocalDateTime> workList = new ArrayList<>();
        List<LocalDateTime> restList = new ArrayList<>();

        LocalDateTime starTime = DateUtils.newLocalDateTime(period.getBegin());
        LocalDateTime endTime = DateUtils.newLocalDateTime(period.getEnd());
        LocalDateTime print = DateUtils.newLocalDateTime(starTime);
        int number = AttConstants.oneInt;
        do {
            String mapKey = String.valueOf(print.getYear()) + (print.getMonthValue());
            AttLegalDayCountDTO dto;
            if (monthLegalCountMap.get(mapKey) != null) {
                dto = monthLegalCountMap.get(mapKey);
            } else {
                dto = AttLegalHolidayUtils.getMonthLegalWorkDay(print.getYear(), print.getMonthValue());
            }
            AttLegalHolidayUtils.handlerList(dto, starTime, endTime, restList, workList);
            print = DateUtils.offsetMonth(print, 1);
            number++;
        } while (print.getYear() <= endTime.getYear() && print.getMonthValue() <= endTime.getMonthValue() && number < 1000);

        result.setRestList(restList);
        result.setWorkList(workList);
        return result;
    }

    public static Object getWebMap(String year, String month) {
        if (StringUtils.isBlank(year) || StringUtils.isBlank(month)) {
            return null;
        }
        String key = AttConstants.attWebHolidaysRedisKey + year;
        Map<String, List<WebDateDTO>> yearMap = yearWebHolidayMap.get(key);
        // 如果存在就直接返回缓存的
        if (yearMap != null && !yearMap.isEmpty()) {
            return yearMap.get(month);
        }
        AttCalendarHolidayRepository attCalendarHolidayRepository = ApplicationContextHolder.getBean(AttCalendarHolidayRepository.class);
        List<AttCalendarHolidayPO> yearList = attCalendarHolidayRepository.list(Wrappers.<AttCalendarHolidayPO>lambdaQuery()
                                                                                        .eq(AttCalendarHolidayPO::getYear, Integer.parseInt(year))
                                                                               );
        if (CollectionUtils.isEmpty(yearList)) {
            yearList = attCalendarHolidayRepository.getThirdYearHolidayInsert(Integer.parseInt(year));
        }
        if (CollectionUtils.isEmpty(yearList)) {
            return null;
        }

        Map<String, List<AttCalendarHolidayPO>> monthMap = yearList.stream()
                .collect(Collectors.groupingBy(k -> String.valueOf(k.getMonth())));
        yearMap = new HashMap<>();
        Map<String, List<WebDateDTO>> finalYearMap = yearMap;
        monthMap.forEach((m, list) -> {
            List<WebDateDTO> webList = new ArrayList<>();
            WebDateDTO webAlmanacDto = new WebDateDTO();
            List<WebAlmanacDTO> almanacDTOList = new ArrayList<>();
            for (AttCalendarHolidayPO po : list) {
                WebAlmanacDTO dto = new WebAlmanacDTO();
                dto.setYear(year);
                dto.setMonth(m);
                dto.setDay(String.valueOf(po.getDayTime().getDayOfMonth()));
                String status = null;
                if (Boolean.TRUE.equals(po.getHoliday())) {
                    status = "1";
                } else if (Boolean.FALSE.equals(po.getHoliday())) {
                    status = "2";
                }
                dto.setStatus(status);
                almanacDTOList.add(dto);
            }
            webAlmanacDto.setAlmanac(almanacDTOList);
            webList.add(webAlmanacDto);
            finalYearMap.put(String.valueOf(m), webList);
        });
        LocalDateTime nowTime = LocalDateTime.now();
        if (Integer.parseInt(year) <= nowTime.getYear() ||
            (Integer.parseInt(year) == nowTime.getYear() + 1 && nowTime.getMonthValue() >= 12)) {
            yearWebHolidayMap.put(key, finalYearMap);
        }
        return finalYearMap.get(month);
    }

    public static void clearMapCache() {
        monthHolidayMap.clear();
        monthLegalCountMap.clear();
        yearWebHolidayMap.clear();
    }

    private static void handlerList(AttLegalDayCountDTO dto, LocalDateTime startTime, LocalDateTime endTime, List<LocalDateTime> restList,
                                    List<LocalDateTime> workList
                                   ) {
        if (dto != null && dto.getWorkList() != null) {
            List<LocalDateTime> tempList = dto.getWorkList().stream().map(w -> {
                if (DateUtils.isIn(w, startTime, endTime)) {
                    return w;
                }
                return null;
            }).filter(Objects::nonNull).toList();
            if (!tempList.isEmpty()) {
                workList.addAll(tempList);
            }
        }
        if (dto != null && dto.getRestList() != null) {
            List<LocalDateTime> tempList = dto.getRestList().stream().map(r -> {
                if (DateUtils.isIn(r, startTime, endTime)) {
                    return r;
                }
                return null;
            }).filter(Objects::nonNull).toList();
            if (!tempList.isEmpty()) {
                restList.addAll(tempList);
            }
        }
    }
}
