package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleDataOrrectionPageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleDataOrrectionParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleDataOrrectionVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-21  14:58
 * @Description:
 * @Version: 1.0
 */
public interface RuleDataOrrectionService {
    List<RuleDataOrrectionVO> getAll();

    Pagination<RuleDataOrrectionVO> getPage(RuleDataOrrectionPageParam param);

    Boolean insert(RuleDataOrrectionParam param);

    Boolean updateStatus(Long id);
}
