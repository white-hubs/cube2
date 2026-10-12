package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class InsideSecurityDeviceParam extends Paging {

    private String keyword;

    //区域id(多级数组）
    private List<String> areaIds;

    //区域id
    private String areaId;

    private Boolean deviceStatus;

    //类型id
    private String genre;

    //应用
    private String applicationCode;
    //设id
    private List<Long> deviceIds;

    /**
     * 过滤的设备主键
     */
    private List<Long> filterDeviceIds;

    private List<String> tagCodes;
}
