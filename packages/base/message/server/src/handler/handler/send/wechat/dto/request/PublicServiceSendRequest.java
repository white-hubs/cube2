package com.guhecloud.cube.base.message2.server.handler.handler.send.wechat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 模板消息发送参数
 *
 * @author lanchun
 * @date 2025/1/2 11:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicServiceSendRequest {

    /**
     * 接收者openid
     */
    private String touser;

    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 模板跳转链接（海外账号没有跳转能力）
     */
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private MiniProgram miniprogram;

    /**
     * 防重入id。对于同一个openid + client_msg_id, 只发送一条消息,10分钟有效,超过10分钟不保证效果。若无防重入需求，可不填
     */
    private String client_msg_id;

    /**
     * 模板数据
     */
    private Map<String, TemplateParam> data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TemplateParam {

        /**
         * 参数值
         */
        private String value;
    }

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    @Data
    public static class MiniProgram {

        /**
         * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
         */
        private String appid;

        /**
         * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
         */
        private String pagepath;
    }
}
