package com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/6 17:12
 * @Description:
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserCenterParam extends Paging {

    /**
     * 用户名
     */
    private String keyword;

    /**
     * 员工类别
     */
    private String type;
}
