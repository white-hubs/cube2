package com.guhecloud.cube.base.device.center.core.param;

import lombok.Data;

@Data
public class ExportTemplateParam {
    private String productId;//产品id
    private boolean filterExistDevice;//过滤已存在的设备
}