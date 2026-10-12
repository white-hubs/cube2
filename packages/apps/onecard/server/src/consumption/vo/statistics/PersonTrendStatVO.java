package com.guhecloud.cube.onecard.server.consumption.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonTrendStatVO {

    /**
     * 人数
     */
    private Integer personCount;

    /**
     * 时间
     */
    private String period;
}
