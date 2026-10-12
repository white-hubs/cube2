package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeptAndDeviceTreeVO extends Tree.Simple<Long,DeptAndDeviceTreeVO>  {
    private Long id;
    private String code;
    private String name;
    /**
     * 设备标志
     */
    private Boolean deviceBool;
}
