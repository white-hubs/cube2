package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttClassesPageParam extends Paging {

    /**
     * 班次名称
     */
    private String name;
    /**
     * 部门id(建议应用范围)
     */
    private Long deptId;
    /**
     * 考勤组id
     */
    private Long groupId;
    /**
     * 是否需要建议应用名称
     */
    private Boolean deptWhere;

    public void validBusiness() {
        if (deptWhere == null) {
            deptWhere = false;
        }
    }

}
