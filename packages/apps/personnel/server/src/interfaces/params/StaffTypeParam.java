package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/5 20:20
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StaffTypeParam extends Paging {
    /**
     * 类型名称
     */
    private String name;
}
