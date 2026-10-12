package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmConfigVO {

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 是否超时
     */
    private Boolean timeout;
}
