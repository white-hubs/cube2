package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import lombok.Data;

@Data
public class AlarmConfirmSituationVO {

    /**
     * 市场编号
     */
    private String marketTenant;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 未确认报警数量
     */
    private Integer totalNum;
}
