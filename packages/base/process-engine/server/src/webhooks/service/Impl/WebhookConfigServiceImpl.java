package com.guhecloud.cube.base.flow.webhooks.service.Impl;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.webhooks.domain.WebhookConfig;
import com.guhecloud.cube.base.flow.webhooks.mapper.WebhookConfigMapper;
import com.guhecloud.cube.base.flow.webhooks.service.WebhookConfigService;
import com.guhecloud.cube.base.flow.webhooks.vo.WebhookConfigVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebhookConfigServiceImpl extends ServiceImpl<WebhookConfigMapper, WebhookConfig> implements WebhookConfigService {
    private final WebhookConfigMapper webhookConfigMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(String fromFlowId, List<WebhookConfigVO> webhookConfigVOList) {
        if (CollectionUtils.isEmpty(webhookConfigVOList)) {
            return;
        }
        webhookConfigVOList = webhookConfigVOList.stream().filter(t -> StringUtils.isNotBlank(t.getUrl())).collect(Collectors.toList());
        List<WebhookConfig> updateList = new ArrayList<>();
        List<WebhookConfig> insertList = new ArrayList<>();
        for (WebhookConfigVO webhookConfigVO : webhookConfigVOList) {
            Long id = webhookConfigVO.getId();
            if (Objects.nonNull(id)) {
                WebhookConfig webhookConfig = this.getById(id);
                if (Objects.nonNull(webhookConfig)) {
                    webhookConfig.setFromFlowId(webhookConfigVO.getFromFlowId());
                    webhookConfig.setUrl(webhookConfigVO.getUrl());
                    webhookConfig.setTriggerNode(webhookConfigVO.getTriggerNode());
                    webhookConfig.setToken(webhookConfigVO.getToken());
                    webhookConfig.setFromFlowId(fromFlowId);
                    updateList.add(webhookConfig);
                }
            } else {
                WebhookConfig webhookConfig = ObjectUtils.copying(webhookConfigVO, WebhookConfig.class);
                webhookConfig.setFromFlowId(fromFlowId);
                insertList.add(webhookConfig);
            }
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }
        if (CollectionUtils.isNotEmpty(insertList)) {
            this.saveBatch(insertList);
        }

    }

    @Override
    public List<WebhookConfigVO> getWebhookList(Long fromFlowId) {
        List<WebhookConfig> webhookConfigs = webhookConfigMapper.selectList(Wrappers.<WebhookConfig>lambdaQuery()
                                                                                    .eq(WebhookConfig::getFromFlowId, fromFlowId.toString()));
        if (CollectionUtils.isEmpty(webhookConfigs)) {
            return new ArrayList<>();
        }
        return ObjectUtils.copying(webhookConfigs, WebhookConfigVO.class);
    }
}
