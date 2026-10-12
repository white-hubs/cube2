package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 培训人员
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskUserRecordQueryParam extends Paging {

    //培训任务主键
    private Long cultivateTaskId;
    //签到状态
    private String userSignStatus;
    //签到时间
    private Period period;
    //是否迟到
    private Boolean belated;
    //市场租户编号
    private Integer marketTenant;
    //主题主键
    private Long themeId;
    //培训形式
    private String cultivateMode;
}