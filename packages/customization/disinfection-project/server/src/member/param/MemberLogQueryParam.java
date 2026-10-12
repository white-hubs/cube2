package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.cube.srv.gen.disinfection.server.member.enums.MemberLogTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberLogQueryParam extends Paging {
    @NotNull(message = "查询类型不能为空")
    private MemberLogTypeEnum type;
}
