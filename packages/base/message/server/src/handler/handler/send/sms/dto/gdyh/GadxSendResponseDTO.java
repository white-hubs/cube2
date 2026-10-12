package com.guhecloud.cube.base.message2.server.handler.handler.send.sms.dto.gdyh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class GadxSendResponseDTO {

    /**
     * 0调用成功，-1失败
     */
    private Integer code;

    /**
     * ok调用成功，其他失败
     */
    private String msg;

    /**
     * 用户发送结果
     */
    private List<GadxSendUserResponse> data;

    public static GadxSendResponseDTO fail() {
        GadxSendResponseDTO responseDTO = new GadxSendResponseDTO();
        responseDTO.setCode(1);
        responseDTO.setMsg("fail");
        return responseDTO;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GadxSendUserResponse {

        /**
         * 发送状态：0-处理中，其他代表失败
         */
        private Integer code;

        /**
         * 手机号
         */
        private String mobile;

        /**
         * 发送状态描述
         */
        private String msg;

        /**
         * 订单号
         */
        private String orderNo;
    }
}
