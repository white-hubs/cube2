package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 手动指令使用次数统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualCommandUsageCountVO {
    /**
     * 驱蚊指令使用次数
     */
    private Long driveMosquitoCount;
    /**
     * 雾灌指令使用次数
     */
    private Long mistSprayCount;
}

