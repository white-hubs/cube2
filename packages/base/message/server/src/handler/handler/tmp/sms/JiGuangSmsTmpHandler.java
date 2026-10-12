package com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.JgSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.JgTemplate;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JiGuangSmsTmpHandler extends BaseSmsTmpHandler {

    @Autowired
    private RestTemplate restTemplate;

    protected JiGuangSmsTmpHandler() {
        super(ChannelEnum.CHANNEL_SMS, ManufacturerCodeEnum.JI_GUANG);
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        String jsTemplate = convertTemplateContent(increaseMessageTmp.getContent());
        Map<String, Object> req = new HashMap<>();
        //默认通知类消息
        req.put("type", 2);
        req.put("template", jsTemplate);
        ResponseEntity<String> response = null;
        try {
            //构建请求体
            HttpHeaders httpHeaders = this.getJgHeaders(increaseMessageTmp.getAccountInfo());
            HttpEntity<?> entity = new HttpEntity<>(req, httpHeaders);
            response = this.restTemplate.exchange("https://api.sms.jpush.cn/v1/templates", HttpMethod.POST, entity, String.class);
            //解析响应结果
            return this.analysisResponse(response);
        } catch (Exception e) {
            log.error("调用极光保存模板接口异常:" + e.getMessage(), e);
            return TmpVO.failTmpVO(e.getMessage());
        }
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        String jsTemplate = convertTemplateContent(changeMessageTmp.getContent());
        JgTemplate jgTemplate = JsonUtils.deserialize(changeMessageTmp.getTmpConfig(), JgTemplate.class);
        //获取模板详细信息
        Map<String, Object> req = new HashMap<>();
        req.put("type", 2);//默认通知类消息
        req.put("template", jsTemplate);
        req.put("temp_id", Integer.valueOf(jgTemplate.getTmpId()));
        ResponseEntity<String> response = null;
        try {
            //构建请求体
            HttpHeaders httpHeaders = this.getJgHeaders(changeMessageTmp.getAccountInfo());
            HttpEntity<?> entity = new HttpEntity<>(req, httpHeaders);
            response = this.restTemplate.exchange(
                    "https://api.sms.jpush.cn/v1/templates/" + jgTemplate.getTmpId(), HttpMethod.PUT, entity, String.class);
            //解析响应结果
            return this.analysisResponse(response);
        } catch (Exception e) {
            log.error("调用极光修改模板接口异常:" + e.getMessage(), e);
            return TmpVO.failTmpVO(e.getMessage());
        }
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        //暂不实现
        throw new UnsupportedOperationException();
    }

    private HttpHeaders getJgHeaders(String config) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + JgSmsAccountConfig.authorizationChecker(config));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private TmpVO analysisResponse(ResponseEntity<String> response) {
        Boolean succeed = false;
        String errorMsg = "";
        JgTemplate jgTemplate = new JgTemplate();
        if (HttpStatus.OK == response.getStatusCode()) {
            succeed = true;
            //解析返回对象
            Map<String, String> jgResponse = JsonUtils.deserialize(response.getBody(), new TypeReference<Map<String, String>>() {
            });
            jgTemplate.setTmpId(jgResponse.get("temp_id"));
        } else {
            //解析返回对象
            Map<String, String> jgResponse = JsonUtils.deserialize(response.getBody(), new TypeReference<Map<String, String>>() {
            });
            errorMsg = jgResponse.get("error");
        }
        return TmpVO.builder()
                .succeed(succeed)
                .tmpConfig(JsonUtils.serialize(jgTemplate))
                .errorMsg(errorMsg)
                .build();
    }

    @Override
    protected String convertTemplateContent(String content) {
        String jsTemplate = content.replace("${", "{{");
        return jsTemplate.replace("}", "}}");
    }
}
