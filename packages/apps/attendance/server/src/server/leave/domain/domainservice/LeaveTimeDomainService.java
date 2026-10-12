package com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.domainservice;

import com.alibaba.fastjson2.JSONArray;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.List;

public class LeaveTimeDomainService {

    public static LeaveFormDetail.LeaveTime getLeaveTime(String leaveTimeStr) {
        /*
         * 兼容另一种时间格式解析
         * 小时：["2022-01-01 09:00","2022-01-01 18:00"]
         * 天:  ["2022-01-01","2022-01-01"]
         * 上午：["2022-01-01,AM","2022-01-01,AM"]
         * 下午: ["2022-01-01,PM","2022-01-01,PM"]
         */
        if (StringUtils.isEmpty(leaveTimeStr)) {
            throw new ServiceException("请假时间不能为空");
        }
        List<String> dateTimeList = JSONArray.parseArray(leaveTimeStr, String.class);
        if (dateTimeList.get(0).matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return new LeaveFormDetail.LeaveTime(leaveTimeStr,
                                                 new LeaveFormDetail.LeaveTime.Time(LeaveFormDetail.LeaveTimeUnit.TIME_RANGE, dateTimeList.get(0)),
                                                 new LeaveFormDetail.LeaveTime.Time(LeaveFormDetail.LeaveTimeUnit.TIME_RANGE, dateTimeList.get(1)));
        }
        if (dateTimeList.get(0).matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return new LeaveFormDetail.LeaveTime(leaveTimeStr,
                                                 new LeaveFormDetail.LeaveTime.Time(LeaveFormDetail.LeaveTimeUnit.TIME_RANGE, dateTimeList.get(0)),
                                                 new LeaveFormDetail.LeaveTime.Time(LeaveFormDetail.LeaveTimeUnit.TIME_RANGE, dateTimeList.get(1)));
        } else if (dateTimeList.get(0).matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return new LeaveFormDetail.LeaveTime(leaveTimeStr,
                                                 new LeaveFormDetail.LeaveTime.Time(LeaveFormDetail.LeaveTimeUnit.DAY, dateTimeList.get(0)),
                                                 new LeaveFormDetail.LeaveTime.Time(LeaveFormDetail.LeaveTimeUnit.DAY, dateTimeList.get(1)));
        }

        if (dateTimeList.get(0).contains(LeaveFormDetail.LeaveTimeUnit.AM.name()) ||
            dateTimeList.get(0).contains(LeaveFormDetail.LeaveTimeUnit.PM.name())) {
            String[] startArray = dateTimeList.get(0).split(",");
            String[] endArray = dateTimeList.get(1).split(",");
            return new LeaveFormDetail.LeaveTime(
                    leaveTimeStr,
                    new LeaveFormDetail.LeaveTime.Time(LeaveFormDetail.LeaveTimeUnit.valueOf(startArray[1]), startArray[0]),
                    new LeaveFormDetail.LeaveTime.Time(LeaveFormDetail.LeaveTimeUnit.valueOf(endArray[1]), endArray[0])
            );
        }

        throw new ServiceException("请假格式有误");
    }
}
