package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

/**
 * 抓拍返回参数
 *
 * @author lanchun
 * @date 2022/8/8 18:11
 */
@Data
public class CameraPtzPhotographVO {

    /**
     * 外网文件路径
     */
    private String extranetFilePath;

    /**
     * 内网文件路径
     */
    private String intranetFilePath;
}
