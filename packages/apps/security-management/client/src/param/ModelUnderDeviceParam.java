package com.guhecloud.cube.srv.gen.securitymanagement.param;

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
    private List<Long> modelIds;
}
