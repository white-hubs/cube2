package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 调度设备-分组
 * @date 2022-07-26 10:08:36
 */
@Data
public class DispatchGroupQueryParam extends Paging {
    //关键字
    private String keyword;
    //组类型
    private String type;
    //租户
    private Integer tenant;
    //设备在线状态
    private Boolean deviceOnline;
    //设备类型
    private String deviceType;
}