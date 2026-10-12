package com.guhecloud.cube.deliver.project.merchant.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.guhecloud.cube.base.bracing.api.AssemblyNoticeClient;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerProtocol;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestionnaire;
import com.guhecloud.cube.deliver.project.merchant.enums.WholesalerEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyUserMapper;
import com.guhecloud.cube.deliver.project.merchant.service.*;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.job.jobtask.service.JobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantJobHandler {

    private final JobService jobService;

    private final CompanyUserMapper companyUserMapper;
    private final CompanyMapper companyMapper;
    private final SpaceService spaceService;
    private final DeviceService deviceService;
    private final SafetyReportService safetyReportService;
    private final WholesalerProtocolService wholesalerProtocolService;
    private final WholesalerQuestionnaireService questionnaireService;
    private final MerchantCompanyService merchantCompanyService;
    private final AssemblyNoticeClient assemblyNoticeClient;

    private static final String BUSINESS_TYPE = "MERCHANT";
    private static final String SCENE_CODE = "BUSINESS_TERM_NOTICE";

    //    @JobAnnotation(name = "insertStallIdToCompanyUser", cron = "0 0 0,8 * * ?", desc = "insertStallIdToCompanyUser")
    public void insertStallIdToCompanyUser(String param) throws Exception {   // 在商户用户关联表中插入档位ID
        List<CompanyUser> companyUsers = companyUserMapper.selectList(null);
        companyUsers.forEach(companyUser -> {
            List<Space> spaces = spaceService.lambdaQuery().eq(Space::getCompanyId, companyUser.getCompanyId()).list();
            if (spaces.size() > 0) {
                companyUser.setStallId(spaces.get(0).getId());
                companyUserMapper.updateById(companyUser);
            }
        });
    }

    //    @JobAnnotation(name = "insertCompanyNameToSpace", cron = "0 0 0,8 * * ?", desc = "insertCompanyNameToSpace")
    public void insertCompanyNameToSpace(String param) throws Exception {  // 在空间表中插入商户名称
        List<Space> spaces = spaceService.lambdaQuery().ne(Space::getCompanyId, "").list();
        spaces.forEach(space -> {
            Company company = companyMapper.selectById(space.getCompanyId());
            if (company != null && !company.getName().equals(space.getCompanyName())) {
                space.setCompanyName(company.getName());
                spaceService.updateById(space);
            }
        });
    }

    //    @JobAnnotation(name = "updateCamOnLineJob", cron = "0 0 0,8 * * ?", desc = "updateCamOnLineJob")
    public void updateCamOnLineJob(String param) throws Exception {
        deviceService.updateCamOnLineJob();
    }

    //    @JobAnnotation(name = "generateSafetyReport", cron = "0 0 0,8 * * ?", desc = "generateSafetyReport")
    public void generateSafetyReport(String param) throws Exception {
        safetyReportService.generatorCrontab();
    }

    public void generateWholesalerProtocolValid(String param) throws Exception {
        WholesalerProtocol protocol = wholesalerProtocolService.getById(param);
        wholesalerProtocolService.lambdaUpdate().eq(WholesalerProtocol::getId, param)
                .set(WholesalerProtocol::getStatus, WholesalerEnum.PROTOCOL_STATUS_VALID.getCode()).update();
        String[] create = protocol.getXxlTaskIds().split(",");
        jobService.removeJob(create[0]);
    }

    public void generateWholesalerProtocolNotValid(String param) throws Exception {
        WholesalerProtocol protocol = wholesalerProtocolService.getById(param);
        wholesalerProtocolService.lambdaUpdate().eq(WholesalerProtocol::getId, param)
                .set(WholesalerProtocol::getStatus, WholesalerEnum.PROTOCOL_STATUS_NOT_VALID.getCode()).update();
        String[] taskIds = protocol.getXxlTaskIds().split(",");
        jobService.removeJob(taskIds[1]);
    }

    public void generateQuestionnaireValid(String param) throws Exception {
        WholesalerQuestionnaire questionnaire = questionnaireService.getById(param);
        questionnaireService.lambdaUpdate().eq(WholesalerQuestionnaire::getId, param)
                .set(WholesalerQuestionnaire::getStatus, WholesalerEnum.QUES_STATUS_VALID.getCode()).update();
        String[] create = questionnaire.getXxlTaskIds().split(",");
        jobService.removeJob(create[0]);
    }

    public void generateQuestionnaireNotValid(String param) throws Exception {
        WholesalerQuestionnaire questionnaire = questionnaireService.getById(param);
        questionnaireService.lambdaUpdate().eq(WholesalerQuestionnaire::getId, param)
                .set(WholesalerQuestionnaire::getStatus, WholesalerEnum.QUES_STATUS_NOT_VALID.getCode()).update();
        String[] taskIds = questionnaire.getXxlTaskIds().split(",");
        jobService.removeJob(taskIds[1]);
    }

    /**
     * 商户营业时间到期通知
     *
     * @param param param
     *
     * @return return
     *
     * @throws Exception e
     */
    //    @JobAnnotation(name = "businessTermNotice", cron = "0 0 0,8 * * ?", desc = "businessTermNotice")
    public void businessTermNotice(String param) {
        SessionContextHolder.clearSessionContext();
        DateTime nowTime = new DateTime();
        String[] params = param.split(",");
        SessionContextHolder.requestTenant(Integer.parseInt(params[0]));
        if (params.length > 1) {
            nowTime = DateUtil.beginOfDay(DateUtil.parse(params[1]));
        }
        // 获取需要通知的的商家
        List<Company> list = merchantCompanyService.lambdaQuery()
                .le(Company::getEndTime, nowTime)
                .eq(Company::getExpirationNotice, false)
                .list();
        if (ObjectUtil.isEmpty(list)) {
            return;
        }
        // 获取通知配置
        List<String> sceneCodeList = new ArrayList<>();
        sceneCodeList.add(SCENE_CODE);
        MessageSendSceneQueryPram queryPram = MessageSendSceneQueryPram
                .builder()
                .businessType(BUSINESS_TYPE)
                .sceneCodes(sceneCodeList)
                .build();
        List<MessageSendConfig> messageSendConfigList = assemblyNoticeClient.getSendMsgConfig(queryPram);
        // 遍历通知场景,遍历通知场景
        for (MessageSendConfig messageSendConfig : messageSendConfigList) {
            try {
                // 营业期限到期提醒
                if (SCENE_CODE.equals(messageSendConfig.getSceneCode())) {
                    list.forEach(ex -> {
                        // 发送场景通知
                        Map<String, String> map = new HashMap<>();
                        map.put("companyName", ex.getName());
                        MessageSendParam sendParam = MessageSendParam.builder()
                                .notifyId(messageSendConfig.getNotifyId())
                                .tempParams(map)
                                .build();
                        MessageSendResponse sendResponse = assemblyNoticeClient.sendMsg(sendParam);
                        log.info("商户营业时间到期通知结果{}", sendResponse.getMessage());
                        ex.setExpirationNotice(true);
                        Boolean bool = merchantCompanyService.updateById(ex);
                        log.info("更新商户营业时间到期通知结果：{}", bool);
                    });
                }
            } catch (Exception e) {
                log.error("商户营业时间到期通知失败！", e);
            }
        }
        SessionContextHolder.clearSessionContext();
    }

}
