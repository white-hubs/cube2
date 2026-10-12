package com.guhecloud.cube.srv.gen.personnel.management.client.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffNotInParam {
    /**
     * 部门id
     */
    private Long organizationId;
    private String staffName;
    /**
     * 需要排除的id
     */
    private List<Long> notInIds;
    private Long current = 1L;
    private Long size = 10L;
}
