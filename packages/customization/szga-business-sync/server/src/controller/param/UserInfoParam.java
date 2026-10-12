package com.guhecloud.cube.srv.gen.business.info.sync.server.controller.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/20 17:35
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoParam extends Paging {
    private Period period;
}
