package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.uniview;

import lombok.Data;

import java.util.List;

/**
 * 门禁设备列表
 */
@Data
public class UniViewPage {
    private int totalcount;
    private int size;
    private List<ThirdPartyDeviceBean> data;
    private int page;
}
