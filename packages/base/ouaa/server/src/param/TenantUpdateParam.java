package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 租户更新信息
 *
 * @author lzy
 * @date 2021/10/14
 */
@Data
public class TenantUpdateParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    @Min(value = 1, message = "主键不能小于1")
    private Long id;

    /**
     * 备注
     */
    @Size(max = 200, message = "备注不能大于200")
    private String remark;

    /**
     * logo主键
     */
    private Long fileId;

    //租户名称
    private String name;
    //详细地址
    private String address;
    //所在地
    private String location;
    //楼栋数
    private Integer building;
    //占地面积
    @Size(min = 0, max = 10, message = "占地面积输入的范围不正确")
    private BigDecimal areaCovered;
    //宣传图
    private List<String> fileIds;
    //占地总建筑面积
    private BigDecimal floorArea;
    /**
     * 服务热线
     */
    @Size(min = 7, max = 15, message = "服务电话长度不正确")
    private String serviceHotLine;

    /**
     * 经纬度信息
     */
    private String lntAndLat;

    /**
     * 经纬度范围
     */
    private String lntAndLatScope;
    /**
     * 经纬度范围名称
     */
    private String lntAndLatScopeName;

    /**
     * 投诉电话
     */
    private String complaintHotLine;

}
