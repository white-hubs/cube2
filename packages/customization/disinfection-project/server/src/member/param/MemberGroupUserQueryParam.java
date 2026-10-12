package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroupUserQueryParam extends Paging {
    private Long groupId;
    // 关键字
    private String keyword;
    // 时间段
    private String createTime;
    // 排除自己(当前登陆人)
    private Boolean notMe;
}
