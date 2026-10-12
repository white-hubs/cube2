package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class ModelUnderDeviceParam extends Paging {
    private String keyword;
    //区域id(多级数组）
    private List<String> areaIds;
    //应用
    private String applicationCode;
    //设id
    private List<Long> deviceProdIds;

    private String areaId;
    // 设备(client里面有这个条件，但是service没有，所以加上。 )
    private List<Long> modelIds;
}
