package com.guhecloud.cube.srv.gen.attendance.management.server.holiday;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool.StatutoryHolidayResponse;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool.StatutoryHolidays;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * 法定节假日测试
 */
@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
public class StatutoryHolidaysTest {

    @Test
    public void getStatutoryHolidays() {
        List<StatutoryHolidayResponse> statutoryHolidayResponses = StatutoryHolidays.getThisYear();
        System.out.println(statutoryHolidayResponses);
    }

    @Test
    public void getStatutoryHolidaysByPeriod() {
        RedisTemplate redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        // 查询单个日期
        String dateStr = "2022-10-02";
        DateTime dateTime = DateUtil.parse(dateStr, "yyyy-MM-dd");
        Set statutoryHolidays = redisTemplate.opsForZSet().rangeByScore("statutoryHolidays", dateTime.getTime(), dateTime.getTime());
        System.out.println(statutoryHolidays);
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "/" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            int cot = 0;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    static Map totalMap = new TreeMap();

    public static void main(String[] args) {

        int startYear = 2022;//开始的年份
        int year = 1;         //统计的年份

        for (int i = 0; i <= (year - 1); i++) {
            for (int j = 1; j <= 12; j++) {
                loop(startYear + i + "", j + "");
            }
        }

        Iterator it = totalMap.keySet().iterator();
        while (it.hasNext()) {
            Object next = it.next();
            System.out.println(next + "--" + totalMap.get(next));
        }

    }

    public static void loop(String year, String month) {
        String s = HttpUtil.get("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=" + year + "%E5%B9%B4" + month +
                                "%E6%9C%88&co=&resource_id=39043&t=1617089428269&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=wisetpl&cb=jQuery110203576901702188473_1617089118772&_=1617089118776");

        s = s.substring(s.indexOf("("));
        s = s.substring(1, s.length() - 2);
        Map<String, Object> map = (Map<String, Object>) JSONObject.parse(s);

        List list = (List) map.get("data");
        Map data = (Map) list.get(0);
        List<Map> almanac = (List<Map>) data.get("almanac");
        if (almanac == null || almanac.size() == 0) {
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
