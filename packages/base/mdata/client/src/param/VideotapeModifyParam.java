package com.guhecloud.cube.biz.mdata.client.param;

import lombok.Data;

/**
 * 视频录制参数
 *
 * @author lanchun
 * @date 2022/7/5 15:46
 */
@Data
public class VideotapeModifyParam {

    /**
     * token
     */
    private String token;

    /**
     * 开始时间年月日
     */
    private String start1;

    /**
     * 开始时间时分秒
     */
    private String start2;

    /**
     * 录像时长
     */
    private String duration;

    /**
     * 8 或者-8 如果没有此参数默认使用服务器所在的时区
     */
    private String tz;

    /**
     * 录像文件名，文件名不用带文件扩展
     */
    private String filename;

    /**
     * true 或者 false,默认 flase，是否永远创建新文件，如果没有该选项，如果录像正在进行，则会在原来的基础上延长对应的时长
     */
    private String alwayscreate;

    /**
     * 根路径
     */
    private String basePath;
}
