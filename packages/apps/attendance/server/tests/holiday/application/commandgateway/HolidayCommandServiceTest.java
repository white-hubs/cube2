package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.AttendanceManagementServer;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.ChangeHolidayConfigCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.HolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayDetailDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.HolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantRule;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(classes = AttendanceManagementServer.class)
@RunWith(SpringRunner.class)
public class HolidayCommandServiceTest {
    @Autowired
    private HolidayRepository holidayRepository;
    @Autowired
    private HolidayQueryService holidayQueryService;
    @Autowired
    private HolidayCommandService holidayCommandService;

    @Before
    public void before() {
        SessionContextHolder.requestTenant(9578171);
        SecurityContextHolder.setPrincipal(Principal.builder().account("1").id(1L).type(Principal.Type.CLIENT).build());
    }

    @Test
    public void changeHolidayExpiryDate() {
        // 查询假期规则配置详情
        Holiday beforeHoliday = holidayRepository.getByType(Holiday.HolidayType.COMPENSATORY_LEAVE);
        HolidayDetailDTO beforeDetailDTO = holidayQueryService.getById(beforeHoliday.getId());

        // 修改假期规则配置
        ChangeHolidayConfigCommand changeHolidayConfigCommand = ObjectUtils.copying(beforeDetailDTO, ChangeHolidayConfigCommand.class);
        changeHolidayConfigCommand.setExpiryDateConfig(HolidayDetailDTO.ExpiryDateConfig.builder().type(ExpiryDate.ExpiryDateType.FULL)
                                                               .value(1).timeUnit(ExpiryDate.TimeUnit.MONTH.name()).build());
        holidayCommandService.changeHolidayConfig(changeHolidayConfigCommand);

        // 查询假期规则配置详情
        HolidayDetailDTO afterDetailDTO = holidayQueryService.getById(beforeHoliday.getId());

        Asserts.check(afterDetailDTO.getExpiryDateConfig().getType() == ExpiryDate.ExpiryDateType.FULL, "修改假期规则配置失败");
    }

    @Test
    public void changeHolidayGrantRule() {
        // 查询假期规则配置详情
        Holiday beforeHoliday = holidayRepository.getByType(Holiday.HolidayType.COMPENSATORY_LEAVE);
        HolidayDetailDTO beforeDetailDTO = holidayQueryService.getById(beforeHoliday.getId());

        // 修改假期规则配置
        ChangeHolidayConfigCommand changeHolidayConfigCommand = ObjectUtils.copying(beforeDetailDTO, ChangeHolidayConfigCommand.class);
        changeHolidayConfigCommand.setGrantRuleConfig(HolidayDetailDTO.GrantRuleConfig.builder()
                                                              .quotaType("FIXED")
                                                              .fixedQuota(5)
                                                              .grantTime(HolidayGrantRule.GrantTime.JANUARY_FIRST_OF_ANNUALLY)
                                                              .grantWay(HolidayGrantRule.GrantWay.AUTO_OF_ANNUALLY)
                                                              .build());
        holidayCommandService.changeHolidayConfig(changeHolidayConfigCommand);

        // 查询假期规则配置详情
        HolidayDetailDTO afterDetailDTO = holidayQueryService.getById(beforeHoliday.getId());

        Asserts.check(afterDetailDTO.getGrantRuleConfig().getQuotaType().equals("FIXED"), "修改假期规则配置失败");
    }
}
