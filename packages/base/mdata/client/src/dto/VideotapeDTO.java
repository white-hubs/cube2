package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.Data;

/**
 * 录像返回参数
 *
 * @author lanchun
 * @date 2022/8/30 14:27
 */
@Data
public class VideotapeDTO {

    /**
     * 外网文件路径
     */
    private String extranetFilePath;

    /**
     * 内网文件路径
     */
    private String intranetFilePath;
}
