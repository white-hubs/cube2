package com.guhecloud.cube.srv.gen.appraisals.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalPeriodTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.AppraisalRecordGenerateService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dao.FormAppraisalRuleMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRule;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.xxl.job.core.biz.model.ReturnT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormAppraisalGenerateJob {
    private final FormAppraisalRuleMapper ruleMapper;
    private final AppraisalRecordGenerateService generateService;

    /**
     * 执行表单考核记录生成任务
     * 遍历所有租户，为每个月度、季度、半年度周期生成考核记录
     *
     * @param param 任务参数（未使用）
     */
    @JobAnnotation(name = "formAppraisalGenerateJob", cron = "0 10 0 * * ?", desc = "表单考核记录生成任务")
    public ReturnT<String> execute(String param) {
        LocalDateTime nowTime = LocalDateTime.now();
        if (StringUtils.isNotBlank(param)) {
            try {
                nowTime = DateUtils.parse(param);
            } catch (Exception e) {
                log.error("表单考核记录时间异常：", e);
                throw new RuntimeException("表单考核记录时间异常");
            }
        }
        SecurityContextHolder.clearSecurityContext();
        SessionContextHolder.clearSessionContext();
        // 获取所有租户列表
        var tenantList = ruleMapper.selectList(Wrappers.<FormAppraisalRule>lambdaQuery().groupBy(FormAppraisalRule::getTenant));
        // 如果租户列表为空，直接返回
        if (CollectionUtils.isEmpty(tenantList)) {
            return ReturnT.SUCCESS;
        }
        // 遍历每个租户，为其生成考核记录
        LocalDateTime finalNowTime = nowTime;
        tenantList.forEach(rule -> {
            // 切换到当前租户的上下文环境
            SessionContextHolder.requestTenant(rule.getTenant());
            // 设置系统用户的安全上下文（ID=1, name=system, account=system）
            SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name("system").account("system").type(Principal.Type.CLIENT)
                                                       .build());
            // 为月度周期生成考核记录
            generateService.generateByPeriod(AppraisalPeriodTypeEnum.MONTHLY, finalNowTime);
            // 为季度周期生成考核记录
            generateService.generateByPeriod(AppraisalPeriodTypeEnum.QUARTERLY, finalNowTime);
            // 为半年度周期生成考核记录
            generateService.generateByPeriod(AppraisalPeriodTypeEnum.HALF_YEAR, finalNowTime);
        });
        return ReturnT.SUCCESS;
    }
}
