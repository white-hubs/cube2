package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wql
 * @describe 巡检结果统计列表下钻请求参数
 * @date 2023/4/14 17:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResultRunInHoleParam extends Paging {
    /**
     * @Author wql
     * @Description 任务id
     * @Date 15:34 2023/4/19
     **/
    private Long taskId;
    /**
     * 巡检不匹配项ids
     */
    private List<String> notMatchIds;
}
