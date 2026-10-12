package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MemberGroupQueryParam extends Paging {
    private Long id;
    /**
     * 创建人id
     */
    private Long memberUserId;
    /**
     * 管理人id
     */
    private Long managerUserId;
    /**
     * 用户姓名条件
     */
    private String keyword;
    /**
     * 组名
     */
    private String name;
    /**
     * 创建时间
     */
    private Period createTime;
    /**
     * 排除自己(当前登陆人)
     */
    private Boolean notMe;
}
