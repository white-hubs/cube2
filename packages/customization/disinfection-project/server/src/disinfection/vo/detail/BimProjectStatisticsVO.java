package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/22 17:17
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BimProjectStatisticsVO {

    /**
     * 项目总数
     */
    private Integer total;

    /**
     * 进行中数
     */
    private Integer inProgress;
    /**
     * 待开始数
     */
    private Integer toStart;
    /**
     * 已完成数
     */
    private Integer completed;
}
