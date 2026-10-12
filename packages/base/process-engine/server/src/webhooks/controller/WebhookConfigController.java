package com.guhecloud.cube.base.flow.webhooks.controller;

import com.guhecloud.cube.base.flow.webhooks.service.WebhookConfigService;
import com.guhecloud.cube.base.flow.webhooks.vo.WebhookConfigVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebhookConfigController {
    private final WebhookConfigService webhookConfigService;

    @PostMapping("/webhook-config/save")
    public void saveOrUpdate(@Valid @RequestBody List<WebhookConfigVO> webhookConfigVOList) {
    }

    @GetMapping("/webhook-config/list")
    public List<WebhookConfigVO> getWebhookList(@Valid @RequestBody Long fromFlowId) {
        return webhookConfigService.getWebhookList(fromFlowId);
    }
}
