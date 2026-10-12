package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DisinfectionDeviceAutomaticQueryParam extends Paging {
    private Long id;
    private Long groupId;
    private String name;
}
