package com.guhecloud.cube.srv.gen.attendance.management.server.clock.interfaces.job;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.commandgateway.ClockCommonService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 更新每日的打卡状态
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateClockStatusJob {

    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;
    private final ClockCommonService clockCommonService;

    /**
     * @param param
     *
     * @return
     */
    public void execute(String param) {
        if (log.isDebugEnabled()) {
            log.debug("更新每日打卡任务开始执行，额外参数:{}", param);
        }
        // 1、查询今日内结束的班次，下班时间在今日的，如果有下班卡在次日的就不算
        // 2、更新没有打卡的为缺卡（没有超出下班打卡范围的不更新，如：定时任务是在00点执行，此时有23:30下班的班次，但是打卡范围是到00:30,则当前就不更新缺卡状态）
        // 考虑：在打上班卡的时候也要检查上个班次的卡是否正常，如果有未打卡的直接更新为缺卡
        var date = LocalDateTime.now();
        if (StringUtils.isNotEmpty(param)) {
            try {
                date = DateUtils.endOfDay(DateUtils.parseDate(param));
            } catch (Exception e) {
                log.debug("更新每日打卡任务参数转换失败");
                ReturnT<String> fail = ReturnT.FAIL;
                throw new ServiceException("参数格式错误，时间格式示例：2023-02-01");
            }
        }
        SessionContextHolder.clearSessionContext();
        var period = new Period(DateUtils.beginOfDay(date), date);
        var endWorkTimeInTodayVOS = arrangeWorkClassesMapper.getUserClassesByPeriod(null, false, true, period);
        var finalDate = date;

        endWorkTimeInTodayVOS.forEach(vo -> {
            SessionContextHolder.requestTenant(vo.getTenant());
            clockCommonService.updateClockToNoCardJob(vo, finalDate, null);
        });
    }

}
