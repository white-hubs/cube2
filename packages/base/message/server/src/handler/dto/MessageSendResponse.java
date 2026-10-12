package com.guhecloud.cube.base.message2.server.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * 消息发送返回信息
 *
 * @author lanchun
 * @date 2023/3/13 13:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSendResponse {

    /**
     * 发送记录主键
     */
    private Long id;
    /**
     * 消息ID
     */
    private Set<String> msgIds;
    /**
     * 是否成功
     */
    protected Boolean succeed;

    /**
     * 结果描述
     */
    protected String message;

    /**
     * 成功的用户
     */
    protected Set<String> succeedReceiver;

    /**
     * 失败用户
     */
    protected List<Receiver> failReceivers;

    /**
     * 用户信息
     */
    @Data
    public static class Receiver {

        /**
         * 接受者
         */
        private String receiver;

        /**
         * 错误信息
         */
        private String errorDescribe;

        public Receiver(String receiver, String errorDescribe) {
            this.receiver = receiver;
            this.errorDescribe = errorDescribe;
        }
    }
}
