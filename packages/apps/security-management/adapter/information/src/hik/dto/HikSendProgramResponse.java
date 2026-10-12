package com.guhecloud.cube.srv.gen.adapter.information.server.hik.dto;

import lombok.Data;

/**
 * 插播内容的返回
 */
@Data
public class HikSendProgramResponse {
    /**
     * 结果类型:0 成功；-1错误，系统未处理；-2异常，系统已处理
     */
    private Integer type;

    /**
     * 返回码(0表示成功，其他表示失败)
     */
    private String code;

    /**
     * 返回提示信息
     */
    private String msg;

    /**
     * 本次操作任务ID，可根据任务ID查询任务进度
     */
    private String data;
}
