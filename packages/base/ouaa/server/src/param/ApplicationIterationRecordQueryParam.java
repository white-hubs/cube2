package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/5 18:15
 * @Description:
 */
@Data
public class ApplicationIterationRecordQueryParam extends Paging {

    private String keyWord;

    /**
     * 应用ID
     */
    private Long applicationId;
}
