package com.guhecloud.cube.srv.gen.conferencemanage.server.third.response;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2020/5/26 10:54
 * @Description:
 */

@Data
public class ResultEntity {
    /**
     * 响应码
     */
    private String errorCode;
    /**
     * 响应消息
     */
    private String errorMsg;
    /**
     * 响应数据
     */
    private Object data;
}
