package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataPermissionVO {
    private Long id;
    private Long menuId;
    private String code;
    private String name;
    private int sortNo;
    private String businessKeys;

    public DataPermissionVO(Long menuId, String code, String businessKeys) {
        this.menuId = menuId;
        this.code = code;
        this.businessKeys = businessKeys;
    }
}
