package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/*
 * 法定节假日
 */
public class StatutoryHolidays {

    private static Map<Object, Object> totalMap = new TreeMap<>();
    private static final String cacheKey = "statutoryHolidays";

    /**
     * 获取今年的节假日列表
     */
    public static List<StatutoryHolidayResponse> getThisYear() {
        int thisYear = LocalDateTime.now().getYear();
        return get(thisYear);
    }

    /**
     * 获取一段时间存在的节假日
     *
     * @param period 时间段
     *
     * @return 节假日列表
     */
    public static List<StatutoryHolidayResponse> getByPeriod(Period period) {
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        LocalDateTime begin = period.getBegin();
        LocalDateTime end = period.getEnd();
        Boolean hasKey = redisTemplate.hasKey(cacheKey);
        if (!hasKey) {
            getThisYear();
        }
        Set<Object> statutoryHolidays = redisTemplate.opsForZSet()
                .rangeByScore(cacheKey, DateUtils.toTimestamp(begin) - 1f, DateUtils.toTimestamp(end));
        List<StatutoryHolidayResponse> statutoryHolidayResponses = new ArrayList<>();
        if (statutoryHolidays != null && !statutoryHolidays.isEmpty()) {
            statutoryHolidays.forEach(value -> {
                StatutoryHolidayResponse statutoryHolidayResponse = JSONObject.parseObject(value.toString(), StatutoryHolidayResponse.class);
                statutoryHolidayResponses.add(statutoryHolidayResponse);
            });
        }
        return statutoryHolidayResponses;
    }

    /**
     * 获取指定年份的节假日
     *
     * @param startYear 如2022
     */
    public static List<StatutoryHolidayResponse> get(int startYear) {
        List<StatutoryHolidayResponse> statutoryHolidayResponses = new ArrayList<>();
        // 缓存
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        Boolean hasKey = redisTemplate.hasKey(cacheKey);
        if (hasKey) {
            Set<Object> statutoryHolidays = redisTemplate.opsForZSet().range(cacheKey, 0, -1);
            if (statutoryHolidays != null && !statutoryHolidays.isEmpty()) {
                statutoryHolidays.forEach(value -> {
                    StatutoryHolidayResponse statutoryHolidayResponse = JSONObject.parseObject(value.toString(), StatutoryHolidayResponse.class);
                    statutoryHolidayResponses.add(statutoryHolidayResponse);
                });
            }
            return statutoryHolidayResponses;
        } else {
            for (int i = 0; i <= (0); i++) {
                for (int j = 1; j <= 12; j++) {
                    loop(startYear + i + "", j + "");
                }
            }
            Iterator<Object> it = totalMap.keySet().iterator();
            while (it.hasNext()) {
                Object next = it.next();
                StatutoryHolidayResponse.Status status =
                        Integer.parseInt(totalMap.get(next).toString()) == 1 ? StatutoryHolidayResponse.Status.DAY_OFF
                                                                             : StatutoryHolidayResponse.Status.WORKING;
                StatutoryHolidayResponse statutoryHolidayResponse = new StatutoryHolidayResponse((String) next, status);
                statutoryHolidayResponses.add(statutoryHolidayResponse);
                // 将字符串转为时间戳
                LocalDateTime dateTime = DateUtils.parse(next.toString(), "yyyy-MM-dd");
                redisTemplate.opsForZSet()
                        .add(cacheKey, JSONObject.toJSONString(statutoryHolidayResponse), DateUtils.toTimestamp(dateTime));
                // 计算当前时间距离下一年还有多久
                LocalDate thisYearStart = LocalDate.of(dateTime.getYear(), 12, 22);
                LocalDate nextYearStart = thisYearStart.plusYears(1);
                long days = nextYearStart.toEpochDay() - LocalDate.now().toEpochDay();
                redisTemplate.expire(cacheKey, days - 1, TimeUnit.DAYS);
            }
        }
        return statutoryHolidayResponses;
    }

    private static void loop(String year, String month) {
        String s = HttpUtil.get("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=" + year + "%E5%B9%B4" + month +
                                "%E6%9C%88&co=&resource_id=39043&t=1617089428269&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=wisetpl&cb=jQuery110203576901702188473_1617089118772&_=1617089118776");

        s = s.substring(s.indexOf("("));
        s = s.substring(1, s.length() - 2);
        Map<String, Object> map = JSONObject.parse(s);

        List list = (List) map.get("data");
        Map data = (Map) list.get(0);
        List<Map> almanac = (List<Map>) data.get("almanac");
        if (CollectionUtils.isEmpty(almanac)) {
            return;
        }

        for (int i = 1; i < almanac.size(); i++) {
            String key = ((String) almanac.get(i).get("oDate")).substring(0, 10);
            String status = (String) almanac.get(i - 1).get("status");
            if ("1".equals(status) || "2".equals(status)) {
                totalMap.put(key, status);
            }
        }

    }
}
