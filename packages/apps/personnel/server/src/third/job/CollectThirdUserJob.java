package com.guhecloud.cube.srv.gen.personnel.management.server.third.job;

import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.srv.gen.personnel.management.server.third.ThirdUserStaffService;
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
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectThirdUserJob {

    private final ThirdUserStaffService thirdUserStaffService;
    private final SysDictionaryClient sysDictionaryClient;

    /**
     * 采集成农批用户信息job
     *
     * @param param
     */
    @JobAnnotation(name = "collectThirdUser", cron = "0 0 2 * * ?", desc = "采集成农批用户信息job",
                   param = "http://cap.cdhjx.cn:9087/api/user/list"/* 这里是默认的地址,以便配置丢失时可以从这里找回*/
                   )
    public void execute(String param) {
        /* 获取job参数 */
        SessionContextHolder.clearSessionContext();
        SysDictionaryDTO sysDictionaryDTO = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(JobParamEnums.JOB_PARAM_ITEM_CODE.name(), "mSUpdateUserInof");
        String variable;
        if (sysDictionaryDTO == null || StringUtils.isBlank(sysDictionaryDTO.getVariable())) {
            variable = param;
        } else {
            variable = sysDictionaryDTO.getVariable();
        }
        Assert.isParameterError(StringUtils.isBlank(variable), "参数必传，用户列表接口地址(字典JOB_PARAM_ITEM_CODE的collectThirdUser的variable)");
        // variable：成农批用户列表接口地址
        if (log.isDebugEnabled()) {
            log.debug("收集第三方用户信息job开始，额外参数{}", variable);
        }
        // 获取成农批用户数据
        thirdUserStaffService.collectCnpUsers(variable);
    }
}
