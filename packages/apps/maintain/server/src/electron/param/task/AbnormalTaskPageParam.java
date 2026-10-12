package com.guhecloud.cube.srv.gen.maintain.server.electron.param.task;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 异常任务查询参数
 * @date 2023/10/26 15:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AbnormalTaskPageParam extends Paging {
    /**
     * 确认状态
     */
    private Boolean affirm;
}
