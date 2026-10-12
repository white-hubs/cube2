package com.guhecloud.cube.customized.guhecloudwebsite.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * 构建记录分页查询参数
 */
@Data
public class BuildRecordQueryParam extends Paging {

    /**
     * 构建状态：1=成功，2=失败
     */
    private String status;

    /**
     * 产物文件名（模糊查询）
     */
    private String artifactName;

    /**
     * 发布状态
     */
    private String releaseStatus;

}
