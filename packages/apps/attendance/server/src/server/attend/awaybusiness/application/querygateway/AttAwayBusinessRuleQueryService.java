package com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.application.querygateway;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.mapper.AttAwayBusinessRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.po.AttAwayBusinessRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.param.AttAwayBusinessRulePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.result.AttAwayBusinessRuleDetailResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.interfaces.result.AttAwayBusinessRulePageResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 出差规则查询服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessRuleQueryService {
    private final AttAwayBusinessRuleMapper attAwayBusinessRuleMapper;

    /**
     * 离岗规则分页列表
     *
     * @param param 条件对象
     *
     * @return 分页列表
     */
    public Pagination<AttAwayBusinessRulePageResult> awayBusinessRulePage(AttAwayBusinessRulePageParam param) {
        Page<AttAwayBusinessRulePO> page = attAwayBusinessRuleMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                        .<AttAwayBusinessRulePO>lambdaQuery()
                                                                                        .like(StringUtils.isNotBlank(param.getName()), AttAwayBusinessRulePO::getName, param.getName())
                                                                                        .orderByDesc(AttAwayBusinessRulePO::getCreateTime)
                                                                               );
        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(
                po -> AttAwayBusinessRulePageResult.builder().id(po.getId()).name(po.getName()).calculationWay(po.getCalculationWay())
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
    public AttAwayBusinessRuleDetailResult awayBusinessRuleDetail(Long id) {
        AttAwayBusinessRuleDetailResult result = new AttAwayBusinessRuleDetailResult();
        AttAwayBusinessRulePO postRulePO = attAwayBusinessRuleMapper.selectById(id);
        if (postRulePO != null) {
            BeanUtils.copyProperties(postRulePO, result);
        }
        return result;
    }

}
