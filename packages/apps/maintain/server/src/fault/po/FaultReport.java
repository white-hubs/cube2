package com.guhecloud.cube.srv.gen.maintain.server.fault.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2022/5/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaultReport extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    //类型
    private String type;
    //名称
    private String name;
    //统计时间
    private LocalDateTime startTime;
    //统计结束时间
    private LocalDateTime endTime;
}
