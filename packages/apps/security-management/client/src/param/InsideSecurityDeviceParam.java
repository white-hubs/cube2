package com.guhecloud.cube.srv.gen.securitymanagement.param;

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

    //状态 NORMAL（正常），OFFLINE（离线），TROUBLE（故障），ALARM（报警）
    private String status;

    //状态true:在线,false:离线
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

    /**
     * 标签参数
     */
    private List<String> tagCodes;
}
