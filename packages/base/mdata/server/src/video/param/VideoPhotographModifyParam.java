package com.guhecloud.cube.biz.mdata.server.video.param;

import lombok.Data;

/**
 * 摄像机抓拍请求参数
 *
 * @author lanchun
 * @date 2022/7/5 14:15
 */
@Data
public class VideoPhotographModifyParam {

    /**
     * token不能为空
     */
    private String token;

    /**
     * 快照文件名，如果空，自动生成文件名，文件名不用带文件扩展
     */
    private String filename;

    /**
     * 开始时间年月日
     */
    private String start1;

    /**
     * 开始时间时分秒
     */
    private String start2;
}
