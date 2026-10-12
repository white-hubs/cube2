package com.guhecloud.cube.biz.work.center.server.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/30 15:54
 * @Description: 接班参数
 */
@Data
public class HandTakeoverParam {

    /**
     * 交接记录ID
     */
    private Long id;

    /**
     * 接班人签名文件ID
     */
    private Long takeSignFileId;

}
