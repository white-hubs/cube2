package com.guhecloud.cube.srv.gen.conferencemanage.server.third.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/18 16:11
 * @Description: 发布节目接口参数
 */

@Data
public class ProgramSendParam {
    private boolean sdk;

    private boolean reset;

    private Integer type;

    private List<String> devices;

    private List<ProgramParam> programs;
}
