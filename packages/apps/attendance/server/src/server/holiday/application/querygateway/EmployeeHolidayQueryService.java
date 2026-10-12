package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayConsumptionDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.EmployeeHolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayDeductDetailMapper;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Nullable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/11/21 15:33
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeHolidayQueryService {
    private final EmployeeHolidayRepository employeeHolidayRepository;
    private final HolidayDeductDetailMapper deductDetailMapper;

    public EmployeeHoliday getById(Long employeeId) {
        return employeeHolidayRepository.getByEmployeeId(employeeId);
    }

    /*
     * 批量查询员工假期消费汇总
     */
    public List<HolidayConsumptionDTO> getConsumptionSummaryByStaff(@NotEmpty @Valid Set<Long> staffIds, @Nullable Period period) {
        List<HolidayConsumptionDTO> result = new ArrayList<>(staffIds.size());
        Map<Long, HolidayConsumptionDTO> staffConsumptionMap = deductDetailMapper.selectConsumptionSummaryByStaff(staffIds, period)
                .stream().collect(Collectors.toMap(HolidayConsumptionDTO::getStaffId, v -> v));

        staffIds.forEach(staffId -> {
            if (staffConsumptionMap.containsKey(staffId)) {
                HolidayConsumptionDTO holidayConsumptionDTO = staffConsumptionMap.get(staffId);
                holidayConsumptionDTO.setCompassionateLeave(Math.round(holidayConsumptionDTO.getCompassionateLeave() / 8 * 10) / 10f);
                holidayConsumptionDTO.setSickLeave(Math.round(holidayConsumptionDTO.getSickLeave() / 8 * 10) / 10f);
                holidayConsumptionDTO.setCompensatoryLeave(Math.round(holidayConsumptionDTO.getCompensatoryLeave() / 8 * 10) / 10f);
                holidayConsumptionDTO.setAnnualLeave(Math.round(holidayConsumptionDTO.getAnnualLeave() / 8 * 10) / 10f);
                holidayConsumptionDTO.setMarriageLeave(Math.round(holidayConsumptionDTO.getMarriageLeave() / 8 * 10) / 10f);
                holidayConsumptionDTO.setMaternityLeave(Math.round(holidayConsumptionDTO.getMaternityLeave() / 8 * 10) / 10f);
                holidayConsumptionDTO.setPaternityLeave(Math.round(holidayConsumptionDTO.getPaternityLeave() / 8 * 10) / 10f);
                holidayConsumptionDTO.setBereavementLeave(Math.round(holidayConsumptionDTO.getBereavementLeave() / 8 * 10) / 10f);
                result.add(holidayConsumptionDTO);
            } else {
                result.add(
                        HolidayConsumptionDTO.builder()
                                .staffId(staffId)
                                .build()
                          );
            }
        });

        return result;
    }

}
