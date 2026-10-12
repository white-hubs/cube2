package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @date 2022/5/16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultCaseSummaryVO {

    /**
     * 故障案例总数
     */
    private Integer total;

}
