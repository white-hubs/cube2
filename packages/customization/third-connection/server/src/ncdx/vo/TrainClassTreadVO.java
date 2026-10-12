package com.guhecloud.cube.srv.gen.third.connection.server.ncdx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainClassTreadVO {
    /**
     * 年份
     */
    private Integer year;
    /**
     * 班次数量
     */
    private Integer classNumber;
    /**
     * 培训数量
     */
    private Integer trainNumber;
}
