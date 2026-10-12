package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param.BroadcastPlayRuleModifyParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param.BroadcastPlayRuleQueryParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po.BroadcastPlayRulePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRulePageVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRuleVO;
import com.guhecloud.platform.base.model.Pagination;

public interface BroadcastPlayRuleService extends IService<BroadcastPlayRulePO> {

    Long save(BroadcastPlayRuleModifyParam param);

    void update(Long id, BroadcastPlayRuleModifyParam param);

    void updateEnable(Long id, Boolean enabled);

    Pagination<BroadcastPlayRulePageVO> getPage(BroadcastPlayRuleQueryParam param);

    BroadcastPlayRuleVO getDetail(Long id);

    void removeById(Long id);
}
