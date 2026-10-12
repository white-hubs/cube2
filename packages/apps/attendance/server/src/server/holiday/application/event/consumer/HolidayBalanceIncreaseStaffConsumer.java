package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.event.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.EmployeeHolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceListPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.service.HolidayBalanceListService;
import com.guhecloud.cube.srv.gen.personnel.management.client.message.IncreaseStaffMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = IncreaseStaffMessage.TOPIC + "_HOLIDAY_BALANCE_GROUP",
        topic = IncreaseStaffMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayBalanceIncreaseStaffConsumer implements RocketMQListener<IncreaseStaffMessage> {

    private final HolidayBalanceListService holidayBalanceListService;
    private final EmployeeHolidayQueryService employeeHolidayQueryService;
    private final HolidayTypeMapper holidayTypeMapper;

    @Override
    public void onMessage(IncreaseStaffMessage increaseStaffMessage) {
        log.info("假期模块收到新增员工【 {} 】消息", increaseStaffMessage.getEmployeeId());
        // 判断所有假期是否具有配置为所有人，如果有就直接新增
        EmployeeHoliday employeeHoliday = employeeHolidayQueryService.getById(increaseStaffMessage.getEmployeeId());
        Set<Holiday.HolidayType> holidayTypes = employeeHoliday.getHolidayTypes();

        Map<Holiday.HolidayType, Long> holidayTypeMap = holidayTypeMapper.selectList(
                        Wrappers.<HolidayTypePO>lambdaQuery()
                                .in(HolidayTypePO::getType, holidayTypes)
                                                                                    ).stream()
                .collect(Collectors.toMap(HolidayTypePO::getType, HolidayTypePO::getId));

        List<HolidayBalanceListPO> holidayBalanceListPOS = new ArrayList<>();
        holidayTypes.forEach(holidayType -> {
            HolidayBalanceListPO balanceListPO = HolidayBalanceListPO.builder()
                    .employeeName(increaseStaffMessage.getName())
                    .employeeOrganization(increaseStaffMessage.getOrganizationId())
                    .holidayTypeId(holidayTypeMap.get(holidayType))
                    .timeOfInduction(increaseStaffMessage.getInductionDate())
                    .userId(increaseStaffMessage.getEmployeeId())
                    .build();
            holidayBalanceListPOS.add(balanceListPO);
        });

        holidayBalanceListService.saveBatch(holidayBalanceListPOS);
    }
}
