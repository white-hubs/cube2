package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberUserQueryParam extends Paging {
    private String keyword;
    private Period createTime;
}
