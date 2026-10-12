package com.guhecloud.cube.srv.gen.loose.works.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wql
 * @describe 工程进度查询
 * @date 2023/9/14 19:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectScheduleQuery extends Paging {
    /**
     * 工程状态
     */
    private String status;
    /**
     * 监管部门
     */
    private Long depId;
    /**
     * 创建时间
     */
    private Period period;

    /**
     * 完成时间
     */
    private Period completePeriod;
    /**
     * 发起人员
     */
    private Long startUser;
    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 工程类型
     */
    private Long typeId;
    /**
     * 负责人
     */
    private List<Long> leaderList;

    /**
     * id集合
     */
    private List<Long> idList;
}
