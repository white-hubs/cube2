package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 安全培训(二维码中转页)查询参数
 * @date 2024/3/20 9:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SafetyCultivateQueryParam extends Paging {
    /**
     * 实体id
     */
    private Long objId;
    /**
     * 查询时间
     */
    private Period period;
}
