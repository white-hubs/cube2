package com.guhecloud.cube.srv.gen.property.management.server.risk.source.param;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-31  15:20
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceRecordParam {
    /**
     * 分类编码
     */
    private List<String> typeCodes;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
}
