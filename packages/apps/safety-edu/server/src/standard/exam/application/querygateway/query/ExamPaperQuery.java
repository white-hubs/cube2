package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 试卷查询查询
 *
 * @author lanchun
 * @date 2023/4/19 11:11
 */
@Data
public class ExamPaperQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}
