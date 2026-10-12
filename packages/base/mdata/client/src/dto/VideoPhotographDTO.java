package com.guhecloud.cube.biz.mdata.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抓拍返回参数
 *
 * @author lanchun
 * @date 2022/8/30 14:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPhotographDTO {

    /**
     * 外网文件路径
     */
    private String extranetFilePath;

    /**
     * 内网文件路径
     */
    private String intranetFilePath;
}
