package com.guhecloud.cube.deliver.project.merchant.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 节假日与非工作日对象
 *
 * @author lanchun
 * @since 2020/9/26 10:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HolidayNoWorkDayDTO {

    private List<LocalDateTime> holidayDates;

    private List<LocalDateTime> noWorkDayDates;
}
