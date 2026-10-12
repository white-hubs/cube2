package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.cube.biz.ouaa.server.enumeration.IterationCategoryEnum;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/6 09:15
 * @Description:
 */
@Data
public class ApplicationIterationContentInfo {

    private Long iterationId;

    /**
     * 迭代类别
     */
    private IterationCategoryEnum category;
    /**
     * 迭代内容
     */
    private String content;
}
