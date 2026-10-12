package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo.HolidayTypeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayController {

    private final HolidayTypeMapper holidayTypeMapper;

    @GetMapping("/holiday/type/list")
    public List<HolidayTypeVO> getHolidays() {
        List<HolidayTypeVO> result = new ArrayList<>();

        // 获取请假规则
        List<HolidayTypePO> holidayTypes = holidayTypeMapper.selectList(Wrappers.emptyWrapper());
        holidayTypes.forEach(holidayTypePO -> {
            LeaveRule leaveRule = JSONObject.parseObject(holidayTypePO.getLeaveRule(), LeaveRule.class);
            HolidayTypeVO holidayTypeVO = HolidayTypeVO.builder()
                    .label(holidayTypePO.getType().getLabel())
                    .value(holidayTypePO.getType().name())
                    .id(holidayTypePO.getId())
                    .leaveRule(HolidayTypeVO.LeaveRule.builder()
                                       .minimumLeaveTimeUnit(leaveRule.getMinimumLeaveTimeUnitConvert().getMinimumLeaveTimeUnit()).build())
                    .build();
            result.add(holidayTypeVO);
        });
        return result;
    }
}
