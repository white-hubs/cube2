package com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 试卷考题规则查询
 */
@Data
public class ExamQuestionRuleQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}
