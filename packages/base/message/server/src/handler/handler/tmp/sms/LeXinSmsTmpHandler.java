package com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.sms.LeXinSmsAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto.LeXinTemplate;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.cube.base.message2.server.handler.util.TemplateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LeXinSmsTmpHandler extends BaseSmsTmpHandler {

    private static final String ADD_TEMPLATE_URL = "/templat/addTemplat/v2";

    private static final String UPDATE_TEMPLATE_URL = "/templat/updateTemplat";

    private static final String DELETE_TEMPLATE_URL = "/templat/deleteTemplat";

    private final RestTemplate restTemplate;

    protected LeXinSmsTmpHandler(RestTemplate restTemplate) {
        super(ChannelEnum.CHANNEL_SMS, ManufacturerCodeEnum.DYNAMIC_THINKING_LE_XIN);
        this.restTemplate = restTemplate;
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        try {
            LeXinSmsAccountConfig leXinSmsAccountConfig = LeXinSmsAccountConfig.getLeXinConfig(increaseMessageTmp.getAccountInfo());
            String convertTemplateContent = convertTemplateContent(increaseMessageTmp.getContent());
            MultiValueMap<String, Object> req = new LinkedMultiValueMap<>();
            //默认通知类消息
            req.add("AppID", leXinSmsAccountConfig.getAppId());
            req.add("AppSecret", leXinSmsAccountConfig.getAppSecret());
            req.add("tempName", increaseMessageTmp.getTmpName());
            req.add("tempSmscontent", convertTemplateContent);
            req.add("sign", leXinSmsAccountConfig.getSignName());
            //构建请求体
            ResponseEntity<String> response = getStringResponseEntity(req, leXinSmsAccountConfig, ADD_TEMPLATE_URL);
            //解析响应结果
            return this.analysisResponse(response);
        } catch (Exception e) {
            log.error("调用乐信保存模板接口异常:", e);
            return TmpVO.failTmpVO(e.getMessage());
        }
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        try {
            LeXinSmsAccountConfig leXinSmsAccountConfig = LeXinSmsAccountConfig.getLeXinConfig(changeMessageTmp.getAccountInfo());
            LeXinTemplate leXinTemplate = JsonUtils.deserialize(changeMessageTmp.getTmpConfig(), LeXinTemplate.class);
            String convertTemplateContent = convertTemplateContent(changeMessageTmp.getContent());
            MultiValueMap<String, Object> req = new LinkedMultiValueMap<>();
            //默认通知类消息
            req.add("AppID", leXinSmsAccountConfig.getAppId());
            req.add("AppSecret", leXinSmsAccountConfig.getAppSecret());
            req.add("tempId", leXinTemplate.getTmpId());
            req.add("tempName", changeMessageTmp.getTmpName());
            req.add("tempSmscontent", convertTemplateContent);
            req.add("sign", leXinSmsAccountConfig.getSignName());
            ResponseEntity<String> response = getStringResponseEntity(req, leXinSmsAccountConfig, UPDATE_TEMPLATE_URL);
            //解析响应结果
            boolean succeed = false;
            if (HttpStatus.OK == response.getStatusCode()) {
                if ("\"1\"".equals(response.getBody())) {
                    succeed = true;
                }
            }
            return TmpVO.builder()
                    .succeed(succeed)
                    .tmpConfig(JsonUtils.serialize(leXinTemplate))
                    .build();
        } catch (Exception e) {
            log.error("调用乐信修改模板接口异常:", e);
            return TmpVO.failTmpVO(e.getMessage());
        }
    }

    private ResponseEntity<String> getStringResponseEntity(MultiValueMap<String, Object> req, LeXinSmsAccountConfig leXinSmsAccountConfig,
                                                           String mothedUrl
                                                          ) {
        //构建请求体
        HttpHeaders httpHeaders = this.getHeaders();
        HttpEntity<?> entity = new HttpEntity<>(req, httpHeaders);
        return this.restTemplate.exchange(buildRequestUrl(leXinSmsAccountConfig.getRequestBaseUrl(), mothedUrl), HttpMethod.POST, entity, String.class);
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        try {
            LeXinSmsAccountConfig leXinSmsAccountConfig = LeXinSmsAccountConfig.getLeXinConfig(deleteMessageTmp.getAccountInfo());
            LeXinTemplate leXinTemplate = JsonUtils.deserialize(deleteMessageTmp.getTmpConfig(), LeXinTemplate.class);
            MultiValueMap<String, Object> req = new LinkedMultiValueMap<>();
            req.add("AppID", leXinSmsAccountConfig.getAppId());
            req.add("AppSecret", leXinSmsAccountConfig.getAppSecret());
            req.add("tempId", leXinTemplate.getTmpId());
            ResponseEntity<String> response = getStringResponseEntity(req, leXinSmsAccountConfig, DELETE_TEMPLATE_URL);
            boolean succeed = false;
            if (HttpStatus.OK == response.getStatusCode()) {
                if ("\"1\"".equals(response.getBody())) {
                    succeed = true;
                }
            }
            if (!succeed) {
                log.error("乐信删除模版失败,相应信息:{}", response.getBody());
            }
        } catch (Exception e) {
            log.error("调用乐信删除模板接口异常:", e);
        }
    }

    @Override
    protected String convertTemplateContent(String content) {
        List<String> patternTemplateVariables = TemplateUtils.getPatternTemplateVariables(content);
        String convertTemplate = content;
        for (int i = 1; i <= patternTemplateVariables.size(); i++) {
            convertTemplate = convertTemplate.replace(
                    "${" + patternTemplateVariables.get(i - 1) + "}", "{%" + patternTemplateVariables.get(i - 1) + "%}");
        }
        return convertTemplate;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
        return headers;
    }

    private String buildRequestUrl(String baseUrl, String methodUrl) {
        return baseUrl + methodUrl;
    }

    private TmpVO analysisResponse(ResponseEntity<String> response) {
        boolean succeed = false;
        String errorMsg = "";
        LeXinTemplate leXinTemplate = new LeXinTemplate();
        if (HttpStatus.OK == response.getStatusCode()) {
            succeed = true;
            //解析返回对象
            Map<String, String> leXinResponse = JsonUtils.deserialize(response.getBody(), new TypeReference<Map<String, String>>() {
            });
            leXinTemplate.setTmpId(leXinResponse.get("fid"));
            leXinTemplate.setTmpCode(leXinResponse.get("templat_code"));
        } else {
            errorMsg = "unknow";
        }
        return TmpVO.builder()
                .succeed(succeed)
                .tmpConfig(JsonUtils.serialize(leXinTemplate))
                .errorMsg(errorMsg)
                .build();
    }
}
