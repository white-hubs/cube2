package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/24 17:11
 * @Description:
 */
@Data
public class VisitorInviteRecordParam extends Paging {
    private String phone;
}
