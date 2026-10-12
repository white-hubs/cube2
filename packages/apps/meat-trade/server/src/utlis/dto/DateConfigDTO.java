package com.guhecloud.cube.biz.meattrade.server.utlis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateConfigDTO {

    /**
     * 开始时间
     */
    private int startHour;

    /**
     * 结束时间
     */
    private int endHour;
}
