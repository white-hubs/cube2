package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.impl;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/12/13
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkServiceImpl implements WorkService {

    @Override
    public List<ArrangeWorkVO.DateOfClasses> sortDateOfClasses(List<ArrangeWorkVO.DateOfClasses> dateOfClasses, String month,
                                                               Period period
                                                              ) {
        int intervalDays = dateOfClasses.size();
        LocalDateTime beginDate = LocalDateTime.now();
        if (StringUtils.isNotEmpty(month)) {
            // 按月查
            LocalDateTime monthDate = DateUtils.parse(month, "yyyy-MM");
            beginDate = DateUtils.beginOfMonth(monthDate);
            intervalDays = (int) DateUtils.between(beginDate, DateUtils.endOfMonth(monthDate), ChronoUnit.DAYS, false) + 1;
        } else {
            // 这里如果是按时间段查询，如果在数据库中查不到某些天的数据，那么空数据都要补充够
            if (Objects.isNull(period)) {
                return dateOfClasses.stream().sorted(Comparator.comparing(ArrangeWorkVO.DateOfClasses::getDate))
                        .collect(Collectors.toList());
            }
            ;
            // 前端时间段传值：2022-12-05,2022-12-11==》period解析2022-12-05 00:00:00,2022-12-11 23:59:59,计算后的天数为6，比我们实际查询的天数少一天；要求前端传时间段不带时分秒
            intervalDays = (int) DateUtils.between(period.getBegin(), period.getEnd(), ChronoUnit.DAYS, false) + 1;
            beginDate = period.getBegin();
        }
        // 如果不相等则说明数据不够，需要补充空数据
        if (dateOfClasses.size() != intervalDays) {
            List<ArrangeWorkVO.DateOfClasses> newClasses = new ArrayList<>();
            for (int i = 0; i < intervalDays; i++) {
                LocalDateTime finalDate = DateUtils.offsetDay(beginDate, i);
                ArrangeWorkVO.DateOfClasses dayClasses = dateOfClasses.stream()
                        .filter(cla -> DateUtils.compare(finalDate, cla.getDate()) == 0).findAny()
                        .orElse(new ArrangeWorkVO.DateOfClasses(finalDate));
                newClasses.add(dayClasses);
            }
            return newClasses;
        }
        return dateOfClasses.stream().sorted(Comparator.comparing(ArrangeWorkVO.DateOfClasses::getDate)).collect(Collectors.toList());
    }

}
