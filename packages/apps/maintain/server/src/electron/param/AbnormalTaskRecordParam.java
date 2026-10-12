package com.guhecloud.cube.srv.gen.maintain.server.electron.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 任务异常记录查询参数
 * @date 2024/1/25 18:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AbnormalTaskRecordParam extends Paging {
    /**
     * 时间范围
     */
    private Period period;
}
