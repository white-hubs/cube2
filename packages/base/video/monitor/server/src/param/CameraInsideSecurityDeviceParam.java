package com.guhecloud.cube.biz.vmonitor.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class CameraInsideSecurityDeviceParam extends Paging {

    private String keyword;

    /**
     * 区域集合
     */
    private List<String> areaId;

    /**
     * 状态 NORMAL（正常），OFFLINE（离线），TROUBLE（故障），ALARM（报警）
     */
    private String status;

    /**
     * 类型
     */
    private String genre;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 设备集合
     */
    private List<Long> deviceIds;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 是否关联
     */
    private Boolean associated;
}
