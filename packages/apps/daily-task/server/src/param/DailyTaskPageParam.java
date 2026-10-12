package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DailyTaskPageParam extends Paging {

    /**
     * 业务类型
     */
    //@NotBlank(message = "业务类型不能为空")
    private String dailyType;

    /**
     * 任务类型
     */
    private Long taskCategoryId;

    /**
     * 任务名称
     */
    private String keyword;

    /**
     * 紧急程度
     */
    private Long urgencyId;

    /**
     * 任务状态
     */
    private String status;

    /**
     * 发布时间
     */
    private Period period;

    /**
     * 查询任务类型（主(MAIN)、子(SUB)）
     */
    private String queryType;

    private String userType;
    /**
     * 多业务类型(逗号分割)
     */
    private String dailyTypes;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 对象id
     *
     * @return
     */
    private Long objectId;

    /**
     * 任务ID
     *
     * @return
     */
    private List<Long> taskIdList;

}
