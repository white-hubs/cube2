package com.guhecloud.cube.srv.gen.cabbage.server.vo.dto;

import lombok.Data;

/**
 * 大白菜挡位对象
 */
@Data
public class DbcStoreDTO {
    // 档位编号
    private String storefrontCode;
    //档位名称
    private String storefrontName;
    //档位别名
    private String alias;
    //所属区域序号
    private String areaCode;
    //所属区域名称
    private String areaName;
    //档位类型序号
    private String storefrontTypeCode;
    //档位类型名称
    private String storefrontTypeName;
    //所属部门序号
    private String departmentCode;
    //所属部门名称
    private String departmentName;
    //档主名称
    private String customerName;
    //档主联系手机号
    private String customerMobile;
    //市场序号
    private Integer marketId;
    //市场名称
    private String marketName;
}
