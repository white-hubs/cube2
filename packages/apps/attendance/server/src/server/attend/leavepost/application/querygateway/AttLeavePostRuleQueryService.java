package com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.application.querygateway;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.mapper.AttLeavePostRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.po.AttLeavePostRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.param.AttLeavePostRulePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.result.AttLeavePostRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.interfaces.result.AttLeavePostRulePageResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 离岗规则查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostRuleQueryService {

    private final AttLeavePostRuleMapper attLeavePostRuleMapper;

    /**
     * 离岗规则分页列表
     *
     * @param param 条件对象
     *
     * @return 分页列表
     */
    public Pagination<AttLeavePostRulePageResult> leavePostRulePage(AttLeavePostRulePageParam param) {
        Page<AttLeavePostRulePO> page = attLeavePostRuleMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                  .<AttLeavePostRulePO>lambdaQuery()
                                                                                  .like(StringUtils.isNotBlank(param.getName()), AttLeavePostRulePO::getName, param.getName())
                                                                                  .orderByDesc(AttLeavePostRulePO::getCreateTime)
                                                                         );
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(
                po -> AttLeavePostRulePageResult.builder().id(po.getId()).name(po.getName()).calculationWay(po.getCalculationWay())
                        .beforeApply(po.getBeforeApply()).minAskUnit(po.getMinAskUnit()).updateTime(po.getUpdateTime()).build()
                                                                                                ).collect(Collectors.toList()));
    }

    /**
     * 离岗规则详情
     *
     * @param id 主键
     *
     * @return 规则详情
     */
    public AttLeavePostRuleDetailResult leavePostRuleDetail(Long id) {
        AttLeavePostRuleDetailResult result = new AttLeavePostRuleDetailResult();
        AttLeavePostRulePO postRulePO = attLeavePostRuleMapper.selectById(id);
        if (postRulePO != null) {
            BeanUtils.copyProperties(postRulePO, result);
        }
        return result;
    }
}
