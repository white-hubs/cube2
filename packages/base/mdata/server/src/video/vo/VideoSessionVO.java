package com.guhecloud.cube.biz.mdata.server.video.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 摄像机session返回参数
 *
 * @author lanchun
 * @date 2022/9/28 9:33
 */
@Data
@Builder
public class VideoSessionVO {

    /**
     * 会话信息
     */
    private String session;

    /**
     * 过期时间
     */
    private Long expireTimes;
}
