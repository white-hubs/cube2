package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskQueryParam extends Paging {
    //关键字
    private String keyword;
    //培训主题
    private Long themeId;

    private Period period;
    //培训形式(线上、线下)
    private String cultivateMode;
    //培训对象(商户、员工)
    private String cultivateTarget;
    //培训任务状态
    private String cultivateTaskStatus;

    //时间段类型
    private String periodType;

    private Long userId;

    //市场租户编号
    private Integer marketTenant;

    //租户编号
    private Integer tenant;
}