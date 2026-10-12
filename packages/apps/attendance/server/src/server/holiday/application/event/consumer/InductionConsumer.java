package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.event.consumer;

import com.alibaba.fastjson2.JSON;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.EmployeeHolidayCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.GrantHolidayCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayTypeGrantConfigDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.FixedHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.WorkingYearHolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.personnel.management.client.message.IncreaseStaffMessage;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "HolidayInductionConsumerGroup",
        topic = IncreaseStaffMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InductionConsumer implements RocketMQListener<IncreaseStaffMessage> {
    private final HolidayTypeMapper holidayTypeMapper;
    private final EmployeeHolidayCommandService employeeHolidayCommandService;

    @Override
    public void onMessage(IncreaseStaffMessage increaseStaffMessage) {

        // 读取此租户的所有假期类型
        List<HolidayTypeGrantConfigDTO> holidayTypeGrantConfigs = holidayTypeMapper.selectListWithGrantConfig();
        holidayTypeGrantConfigs.forEach(holidayTypeGrantConfigDTO -> {
            HolidayGrantRule.GrantTime grantTime = HolidayGrantRule.GrantTime.valueOf(holidayTypeGrantConfigDTO.getGrantTime());
            if (grantTime == HolidayGrantRule.GrantTime.INDUCTION) {
                List<HolidayGrantQuota> grantQuotas = JSON.parseArray(holidayTypeGrantConfigDTO.getGrantQuota(), HolidayGrantQuota.class);
                // 固定额度
                List<HolidayGrantQuota> fixed = grantQuotas.stream()
                        .filter(holidayGrantQuota -> holidayGrantQuota instanceof FixedHolidayGrantQuota).toList();
                if (CollectionUtils.isNotEmpty(fixed)) {
                    HolidayGrantQuota holidayGrantQuota = fixed.getFirst();
                    // 执行发放
                    GrantHolidayCommand grantHolidayCommand = new GrantHolidayCommand(
                            increaseStaffMessage.getEmployeeId(),
                            holidayGrantQuota.getQuota(),
                            holidayTypeGrantConfigDTO.getHolidayType()
                    );
                    employeeHolidayCommandService.grantHoliday(grantHolidayCommand);
                }

                // 按社会工龄
                List<HolidayGrantQuota> workingYear = grantQuotas.stream()
                        .filter(holidayGrantQuota -> holidayGrantQuota instanceof WorkingYearHolidayGrantQuota)
                        .toList();
                if (CollectionUtils.isNotEmpty(workingYear)) {
                    workingYear.forEach(holidayGrantQuota -> {
                        // 获取社会工龄满足的员工
                        WorkingYearHolidayGrantQuota workingYearHolidayGrantQuota = (WorkingYearHolidayGrantQuota) holidayGrantQuota;
                        LocalDateTime localDateTime = increaseStaffMessage.getPaySocialInsuranceFirstDate();
                        LocalDateTime now = LocalDateTime.now();
                        long year = localDateTime.until(now, ChronoUnit.YEARS);
                        if (year >= workingYearHolidayGrantQuota.getThreshold()) {
                            // 执行发放
                            GrantHolidayCommand grantHolidayCommand = new GrantHolidayCommand(
                                    increaseStaffMessage.getEmployeeId(),
                                    // todo 获取计算时间
                                    holidayGrantQuota.getQuota(),
                                    holidayTypeGrantConfigDTO.getHolidayType()
                            );
                            employeeHolidayCommandService.grantHoliday(grantHolidayCommand);
                        }
                    });
                }
            }
        });
    }
}
