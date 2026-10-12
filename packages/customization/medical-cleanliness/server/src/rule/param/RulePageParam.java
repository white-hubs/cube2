package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-20  15:43
 * @Description:
 * @Version: 1.0
 */
@Data
public class RulePageParam extends Paging {
    private String keyword;
}
