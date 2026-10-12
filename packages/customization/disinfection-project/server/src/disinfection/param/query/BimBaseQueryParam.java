package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionProjectStatusEnum;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/25 14:59
 * @Description:
 */
@Data
public class BimBaseQueryParam {
    /**
     * 任务状态
     */
    private DisinfectionProjectStatusEnum status;
    /**
     * 关键字
     */
    private String keyword;
}
