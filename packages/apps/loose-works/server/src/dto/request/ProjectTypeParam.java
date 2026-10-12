package com.guhecloud.cube.srv.gen.loose.works.dto.request;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/8 16:39
 * @Description:
 */
@Data
public class ProjectTypeParam extends Paging {
    /**
     * 类型名称
     */
    private String name;
}
