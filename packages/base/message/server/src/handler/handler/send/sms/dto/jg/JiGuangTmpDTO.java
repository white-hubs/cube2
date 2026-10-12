package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.jg;

import lombok.Data;

/**
 * 激光模板信息
 *
 * @author lanchun
 * @date 2023/5/11 11:09
 */
@Data
public class JiGuangTmpDTO {

    /**
     * 模板主键
     */
    private String tmpId;
    /**
     * 模板内容
     */
    private String tmpContent;
}
