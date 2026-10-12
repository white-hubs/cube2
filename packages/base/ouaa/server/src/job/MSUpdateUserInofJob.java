package com.guhecloud.cube.biz.ouaa.server.job;

import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.biz.ouaa.server.service.organization.MSOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.user.MSUserService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.job.jobtask.enums.JobParamEnums;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-06  10:52
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MSUpdateUserInofJob {
    private final MSUserService userService;
    private final MSOrganizationService organizationService;
    private final SysDictionaryClient sysDictionaryClient;

    /**
     * 用户中心-眉山-人员信息同步
     *
     * @param param 租户id
     */
    @JobAnnotation(name = "mSUpdateUserInof", cron = "0,30 0,30 0 * * ?", desc = "用户中心-眉山-人员信息同步")
    public void MSUpdateUserInof(String param) {
        /* 获取job参数 */
        SessionContextHolder.clearSessionContext();
        SysDictionaryDTO sysDictionaryDTO = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(JobParamEnums.JOB_PARAM_ITEM_CODE.name(), "mSUpdateUserInof");
        Assert.isParameterError(sysDictionaryDTO == null ||
                                StringUtils.isBlank(sysDictionaryDTO.getVariable()), "请填写租户id(字典JOB_PARAM_ITEM_CODE的mSUpdateUserInof的variable)");
        SessionContextHolder.requestTenant(Integer.parseInt(sysDictionaryDTO.getVariable()));
        organizationService.organization();
        userService.user();
        SessionContextHolder.clearSessionContext();
    }
}
