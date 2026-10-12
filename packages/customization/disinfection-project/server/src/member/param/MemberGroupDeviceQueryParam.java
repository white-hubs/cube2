package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroupDeviceQueryParam extends Paging {
    private Long groupId;
    private String keyword;

    /**
     * 产品id
     */
    private Long productId;
}
