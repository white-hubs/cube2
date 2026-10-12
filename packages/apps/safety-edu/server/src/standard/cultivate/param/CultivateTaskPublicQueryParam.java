package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 培训任务
 *
 * @author lanchun
 * @date 2024/4/11 11:01
 */
@Data
public class CultivateTaskPublicQueryParam extends Paging {
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

    //唯一标识
    @NotBlank(message = "用户唯一标识不能为空")
    private String userIdentify;

    private Integer marketTenant;

}