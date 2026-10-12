package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.repository;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttCalendarHolidayMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttCalendarHolidayPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto.AttCalendarHolidayDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.dto.AttCalendarTypeDTO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttCalendarHolidayRepository extends ServiceImpl<AttCalendarHolidayMapper, AttCalendarHolidayPO> {

    /**
     * 获取第三方的法定节假日(全部日期)
     *
     * @param year 年份
     *
     * @return 集合
     */
    @Transactional(rollbackFor = Exception.class)
    public List<AttCalendarHolidayPO> getThirdYearHolidayInsert(Integer year) {
        List<AttCalendarHolidayPO> result = new ArrayList<>();
        if (year == null || year < AttConstants.zeroInt) {
            return result;
        }
        String data = null;
        try {
            data = HttpUtil.get(getUrl(year));
        } catch (Exception e) {
            throw new ServiceException("请检查外网环境！(无法获取法定节假日)");
        }
        if (StringUtils.isBlank(data)) {
            return result;
        }
        Map<String, Object> map = JsonUtils.deserialize(data, new TypeReference<>() {});
        if (map == null || map.isEmpty()) {
            return result;
        }
        Object codeObj = map.get("code");
        Object holidayObj = map.get("holiday");
        if (codeObj == null || holidayObj == null || !"0".equals(String.valueOf(codeObj))) {
            return result;
        }
        Map<String, Object> holidayMap = (Map<String, Object>) holidayObj;
        if (holidayMap.isEmpty()) {
            return result;
        }
        Object typeObj = map.get("type");
        Map<String, Object> typeObjMap = (Map<String, Object>) typeObj;

        if (holidayMap.isEmpty()) {
            return result;
        }
        LocalDateTime nowTime = LocalDateTime.now();
        List<String> removeList = new ArrayList<>();

        LocalDateTime yearPrintTime = DateUtils.parseDate(year + "-01-01");
        LocalDateTime yearEndTime = DateUtils.parseDate(year + "-12-31");
        int num = AttConstants.oneInt;
        do {
            Object value = holidayMap.get(DateUtils.format(yearPrintTime, "MM-dd"));
            if (value != null) {
                JSONObject v1 = JSON.parseObject(JSON.toJSONString(value));
                AttCalendarHolidayDTO dto = v1.toJavaObject(AttCalendarHolidayDTO.class);
                if (dto != null && StringUtils.isNotBlank(dto.getDate())) {
                    AttCalendarTypeDTO typeDTO = new AttCalendarTypeDTO();
                    Object typeValueObj = typeObjMap.get(dto.getDate());
                    if (typeValueObj != null) {
                        JSONObject t1 = JSON.parseObject(JSON.toJSONString(typeValueObj));
                        typeDTO = t1.toJavaObject(AttCalendarTypeDTO.class);
                    }
                    if (typeDTO == null) {
                        typeDTO = new AttCalendarTypeDTO();
                    }
                    Integer type = typeDTO.getType();
                    boolean holiday = type == 2 || type == 1;
                    LocalDateTime dateTime = DateUtils.parseDate(dto.getDate());
                    AttCalendarHolidayPO po = new AttCalendarHolidayPO();
                    po.setYear(dateTime.getYear());
                    po.setMonth(dateTime.getMonthValue());
                    po.setHoliday(holiday);
                    po.setDayTime(dateTime);
                    po.setDayFormat(dto.getDate());
                    po.setDay(dateTime.getDayOfMonth());
                    po.setWeek(typeDTO.getWeek());
                    po.setHolidayType(type);
                    po.setCreateTime(nowTime);
                    removeList.add(po.getDayFormat());
                    result.add(po);
                }
            } else {
                AttCalendarHolidayPO po = new AttCalendarHolidayPO();
                po.setYear(yearPrintTime.getYear());
                po.setMonth(yearPrintTime.getMonthValue());
                po.setHoliday(false);
                po.setDayTime(yearPrintTime);
                po.setDayFormat(DateUtils.formatDate(yearPrintTime));
                po.setDay(yearPrintTime.getDayOfMonth());
                po.setWeek(yearPrintTime.getDayOfWeek().getValue());
                po.setHolidayType(0);
                po.setCreateTime(nowTime);
                removeList.add(po.getDayFormat());
                result.add(po);
            }
            yearPrintTime = DateUtils.offsetDay(yearPrintTime, 1);
            num++;
        } while (yearEndTime.isAfter(yearPrintTime) && num < 400);

        // 今年及以前的就入库，或者当前是12月查询明年(如果是之后年的假期还没有更新没有意义）
        if (!CollectionUtils.isEmpty(result) &&
            (year <= nowTime.getYear() || (year == nowTime.getYear() + 1 && nowTime.getMonthValue() >= 11))) {
            this.saveBatch(result);
            this.remove(Wrappers.<AttCalendarHolidayPO>lambdaQuery()
                                .lt(AttCalendarHolidayPO::getCreateTime, DateUtils.newLocalDateTime(nowTime))
                                .in(AttCalendarHolidayPO::getDayFormat, removeList)
                       );
        }

        return result;
    }

    public static String getUrl(Integer year) {
        // week=Y 表示节假日包含周末
        // type=Y 表示返回对象内包含type对象
        return "https://timor.tech/api/holiday/year/" + year + "?type=Y&week=Y";
    }
}
