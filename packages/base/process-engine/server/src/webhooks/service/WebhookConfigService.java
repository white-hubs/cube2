package com.guhecloud.cube.base.flow.webhooks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.webhooks.domain.WebhookConfig;
import com.guhecloud.cube.base.flow.webhooks.vo.WebhookConfigVO;

import java.util.List;

public interface WebhookConfigService extends IService<WebhookConfig> {

    void saveOrUpdate(String fromFlowId, List<WebhookConfigVO> webhookConfigVOList);

    List<WebhookConfigVO> getWebhookList(Long fromFlowId);
}
