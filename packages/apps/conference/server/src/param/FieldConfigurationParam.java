package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/18 19:07
 * @Description:
 */
@Data
public class FieldConfigurationParam extends Paging {
    /**
     * 字段名
     */
    private String fieldName;

}
