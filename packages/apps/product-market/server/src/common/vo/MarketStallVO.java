package com.guhecloud.cube.srv.gen.product.market.server.common.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 档位管理
 * @date 2023-05-10 15:08:36
 */
@Data
public class MarketStallVO {

    //主键
    private Long id;
    //档位编号
    private String code;
    //档位名称
    private String name;
    //档位类型
    private String type;
    //档位区域
    private String area;
    //具体位置
    private String location;
    //建筑面积
    private String builtUpArea;
    //实用面积
    private String practicalArea;
    //部门主键
    private String deptId;
    //部门名称
    private String deptName;
    //出租类型
    private Boolean leaseType;
    //是否出租
    private Boolean leased;
    //档主ID
    private String bossId;
    //档主名称
    private String bossName;
    //档主身份证号码
    private String bossCardNo;
    //档主电话
    private String bossPhone;
    //备注说明
    private String remark;
    //模型绑定
    private Boolean bimBind;
    //是否启用
    private Boolean enabled;
}