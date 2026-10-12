package com.guhecloud.cube.biz.ouaa.server.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 租户信息
 *
 * @author Woody
 * @date 2021/7/14
 */
@Data
public class TenantVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 租户编号
     */
    private Integer code;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 用户主键
     */
    private Long userAdminId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所在地
     */
    private List<String> locations;

    /**
     * 楼栋数
     */
    private Integer building;

    /**
     * 占地面积
     */
    private BigDecimal areaCovered;

    /**
     * 企业logo信息
     */
    private FileVO logo;

    /**
     * 企业logo主键
     */
    private Long logoId;

    /**
     * 企业宣传图信息
     */
    private List<FileVO> files;

    /**
     * 企业宣传图主键
     */
    private List<String> fileIds;

    /**
     * 租户备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否最近登录
     */
    private Boolean recentLogin;

    /**
     * 是否为主企业
     */
    private Boolean mainEnterprise;

    /**
     * 是否是管理员
     */
    private Boolean admin;

    /**
     * 产品列表
     */
    private List<ProductVO> productVOS;
    /**
     * 占地总建筑面积
     */
    private BigDecimal floorArea;

    /**
     * 服务热线
     */
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
