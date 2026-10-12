package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * 培训任务
 *
 * @author lanchun
 * @date 2024/4/10 14:31
 */
@Data
public class HeadquartersCultivateTaskQueryParam extends Paging {
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

}