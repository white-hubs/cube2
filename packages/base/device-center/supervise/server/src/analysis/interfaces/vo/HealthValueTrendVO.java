package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author pengjian
 * @date 2022/8/1 19:17
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthValueTrendVO implements Comparable<HealthValueTrendVO> {
    private String date;
    private Integer healthValue;

    @Override
    public int compareTo(HealthValueTrendVO o) {
        LocalDate date = LocalDate.parse(this.date);
        LocalDate parse = LocalDate.parse(o.date);
        if (date.isAfter(parse)) {
            return 1;
        } else {
            return -1;
        }
    }
}
