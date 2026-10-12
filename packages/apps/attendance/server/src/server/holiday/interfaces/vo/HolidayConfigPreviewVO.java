package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import lombok.Builder;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/11/21 16:58
 * @description
 */
@Data
@Builder
public class HolidayConfigPreviewVO {
    private Long id;
    private String name;
    private Holiday.HolidayType type;
    private String leaveTimeUnit;
}
