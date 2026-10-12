package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import lombok.Data;

/**
 * 报警状态分组统计
 *
 * @author lanchun
 * @date 2023/9/9 14:18
 */
@Data
public class AlarmStatusGroupVO {

    private String status;

    private String statusName;

    private Integer totalNum;
}
