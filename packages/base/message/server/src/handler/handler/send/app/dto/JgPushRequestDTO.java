package com.guhecloud.cube.base.message2.server.handler.handler.send.app.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 极光app推送参数
 *
 * @author lanchun
 * @date 2021/11/17 17:51
 */
@Builder
@Data
public class JgPushRequestDTO {

    private String cid;

    private Object platform;

    private Map<String, Object> audience;

    private Map<String, Object> notification;

    private Map<String, Object> message;

}
