package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.MedicalRuleNotifyConfigMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleNotifyConfig;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.MedicalRuleNotifyConfigParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleNotifyConfigService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalRuleNotifyConfigServiceImpl extends ServiceImpl<MedicalRuleNotifyConfigMapper, MedicalRuleNotifyConfig> implements MedicalRuleNotifyConfigService {
    private final ApplicationClient applicationClient;
    private final UserClient userClient;
    private static final String RULE_ABNORMAL = "MEDICAL_OVERRUN_ALARM";//"OPERATING_ROOM_NORM_ALARM_NOTIFY";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(@Nonnull MedicalRuleNotifyConfigParam param) {
        MedicalRuleNotifyConfig medicalRuleNotifyConfig = this.getMedicalRuleNotifyConfig();
        boolean updateRule = false;
        if (Objects.nonNull(medicalRuleNotifyConfig)) {
            MedicalRuleNotifyConfig newMedicalRuleNotifyConfig = ObjectUtils.copying(param, MedicalRuleNotifyConfig.class);
            updateRule = !medicalRuleNotifyConfig.equals(newMedicalRuleNotifyConfig);
            medicalRuleNotifyConfig.setTimes(param.getTimes());
            medicalRuleNotifyConfig.setTimeUnit(param.getTimeUnit());
            medicalRuleNotifyConfig.setNotifyChannels(param.getNotifyChannels());
            this.baseMapper.updateById(medicalRuleNotifyConfig);
        } else {
            updateRule = true;
            medicalRuleNotifyConfig = ObjectUtils.copying(param, MedicalRuleNotifyConfig.class);
            this.baseMapper.insert(medicalRuleNotifyConfig);
        }
        return updateRule;
    }

    @Override
    public MedicalRuleNotifyConfig getMedicalRuleNotifyConfig() {
        return this.baseMapper.selectOne(null);
    }

    @Override
    public void sendMessage(Map<String, String> tmpParams, String messageRecordId, Set<Long> orgIds) {
        List<Integer> tenants = new ArrayList<>();
        tenants.add(0);
        tenants.add(SessionContextHolder.getRequester().getTenant());
        ApplicationDTO application = applicationClient.getApplicationByCode("OPERATING_ROOM");
        tenants.forEach(tenant -> {
            MessageSpecifyTmpSendRequest message = MessageSpecifyTmpSendRequest.builder()
                    .tenant(tenant)
                    .applicationCode(application.getCode())
                    .applicationName(application.getName())
                    .msgSceneCode(RULE_ABNORMAL)
                    .businessId(messageRecordId)
                    .tmpParams(tmpParams)
                    .messageNotifyTargets(this.getNotifyTargets(orgIds, tenant))
                    .sourceTenant(SessionContextHolder.getRequester().getTenant())
                    .build();
            MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
            MessageSendResponse messageSendResponse = null;
            try {
                if (tenant != 0) {
                    messageSendResponse = sendClient.sceneSend(message);
                } else {
                    message.setChannel(Collections.singletonList(MessageChannelRequest.builder()
                                                                         .channelCode(MessageChannelEnum.CHANNEL_SITE)
                                                                         .extendConfig("{\"terminals\":[\"kh-admin-web\"]}")
                                                                         .tenant(tenant)
                                                                         .build()));
                    messageSendResponse = sendClient.sceneSpecifyTmpSend(message);
                }
                log.info("{}:消息发送:{}结果:{}", tenant, messageRecordId, JSON.toJSONString(messageSendResponse));
            } catch (Exception e) {
                log.error("{}:消息发送失败:{}", tenant, messageRecordId, e);
            }
        });
    }

    /**
     * @description: 获取发送通知对象
     * @author: lijie
     * @date: 2024/9/19 13:46
     * @param: orgIds 部门id
     * @return: 通知对象具体信息
     **/
    private List<MessageNotifyTarget> getNotifyTargets(Set<Long> orgIds, Integer tenant) {
        List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
        MessageNotifyTarget messageNotifyTarget = MessageNotifyTarget.builder()
                .notifyTargetType(tenant.equals(0) ? MessageNotifyTargetEnum.USER : MessageNotifyTargetEnum.DEPARTMENT)
                .ids(tenant.equals(0) ? this.getUsers(tenant) : orgIds)
                .build();
        notifyTargets.add(messageNotifyTarget);
        return notifyTargets;
    }

    /**
     * @description: 获取租户下所有人员id
     * @author: lijie
     * @date: 2024/9/19 13:45
     * @param: tenant 租户code
     * @return: java.util.Set<java.lang.Long> 租户下所有人员id
     **/
    private Set<Long> getUsers(Integer tenant) {
        Set<Long> userIds = new HashSet<>();
        Integer total = 999;
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setPageNumber(1);
        userQueryParam.setPageSize(999);
        Pagination<UserDTO> pagination = null;
        do {
            total = userQueryParam.getPageNumber() * userQueryParam.getPageSize();
            pagination = userClient.listUserByTenant(tenant, userQueryParam);
            pagination.getRecords().forEach(userDTO -> userIds.add(userDTO.getId()));
            userQueryParam.setPageNumber(userQueryParam.getPageNumber() + 1);
        } while (pagination.getTotal() > total);
        return userIds;
    }
}
