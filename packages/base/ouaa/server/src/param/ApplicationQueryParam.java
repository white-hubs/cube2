package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 应用查询参数
 *
 * @author Woody
 * @date 2021/7/14
 */
@Data
public class ApplicationQueryParam extends Paging {
    /**
     * 应用组主键
     */
    @Min(value = 1, message = "应用组主键不能小于1")
    private Long applicationGroupId;

    /**
     * 应用ID
     */
    private String applicationId;

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 是否公开展示
     */
    private Boolean publicAccess;

    /**
     * 应用ID
     */
    private List<Long> applicationIdList;
}
