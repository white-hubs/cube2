
package com.guhecloud.cube.srv.gen.appraisals.server.rule.service;

import com.guhecloud.cube.srv.gen.appraisals.server.rule.param.FormAppraisalRulePageParam;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.vo.FormAppraisalRuleDetailVO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.vo.FormAppraisalRulePageVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 考核规则查询服务接口
 * 提供考核规则的查询操作
 */
public interface FormAppraisalRuleQueryService {
    /**
     * 根据 ID 获取考核规则详情
     *
     * @param id 规则 ID
     * @return 考核规则详情 VO
     */
    FormAppraisalRuleDetailVO getById(Long id);

    /**
     * 分页获取考核规则列表
     *
     * @param param 分页查询参数
     * @return 考核规则分页数据
     */
    Pagination<FormAppraisalRulePageVO> getPage(FormAppraisalRulePageParam param);
}
